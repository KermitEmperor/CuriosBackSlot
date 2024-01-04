package com.kermitemperor.curiosbackslot;

import com.kermitemperor.curiosbackslot.capability.XYZPosAndRotation;
import com.kermitemperor.curiosbackslot.capability.XYZPosAndRotationProvider;
import com.kermitemperor.curiosbackslot.client.BackWeaponConfigurationScreen;
import com.kermitemperor.curiosbackslot.client.KeyBinding;
import com.kermitemperor.curiosbackslot.config.ClientConfig;
import com.kermitemperor.curiosbackslot.network.PacketChannel;
import com.kermitemperor.curiosbackslot.network.packet.ResyncWithMePacket;
import com.kermitemperor.curiosbackslot.network.packet.SwitchPacket;
import com.kermitemperor.curiosbackslot.render.GuiRenderer;
import com.kermitemperor.curiosbackslot.util.CuriosBackSlotHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.event.CurioEquipEvent;

@Mod(CuriosBackSlot.MOD_ID)
public class CuriosBackSlot {

    public static final String MOD_ID = "curiosbackslot";

    @SuppressWarnings("unused")
    public static final Logger LOGGER = LogUtils.getLogger();


    public CuriosBackSlot() {

        IEventBus ModEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEventBus.addListener(this::setup);
        ModEventBus.addListener(this::enqueue);
        ModEventBus.addListener(this::clientSetup);
        ModEventBus.addListener(this::onRegisterCapabilities);

        IEventBus ForgeEventBus = MinecraftForge.EVENT_BUS;
        ForgeEventBus.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ClientConfig.SPEC, MOD_ID + "-client.toml");
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketChannel.register();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        OverlayRegistry.registerOverlayTop(CuriosBackSlotHandler.SLOT_ID, new GuiRenderer());
        ClientRegistry.registerKeyBinding(KeyBinding.SWITCHING_KEY);
        ClientRegistry.registerKeyBinding(KeyBinding.SWITCHING_KEY);

    }

    private void enqueue(final InterModEnqueueEvent evt) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE,
                () -> new SlotTypeMessage
                        .Builder(CuriosBackSlotHandler.SLOT_ID)
                        .icon(new ResourceLocation(CuriosApi.MODID,"slot/empty_"+CuriosBackSlotHandler.SLOT_ID+"_slot"))
                        .priority(1)
                        .size(1)
                        .build()
        );
    }

    public void onRegisterCapabilities(final RegisterCapabilitiesEvent event) {
        LOGGER.info("registered");
        event.register(XYZPosAndRotation.class);
    }

    @SubscribeEvent
    public void onEquip(CurioEquipEvent event) {
        if (event.getSlotContext().identifier().equals(CuriosBackSlotHandler.SLOT_ID)) {
            event.setResult(Event.Result.ALLOW);
        }
    }
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        final Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (KeyBinding.SWITCHING_KEY.consumeClick()) {
            PacketChannel.sendToServer(new SwitchPacket());
        } else if (KeyBinding.XYZ_KEY.consumeClick()) {
            Minecraft.getInstance().pushGuiLayer(new BackWeaponConfigurationScreen());
        }

        if (mc.screen instanceof BackWeaponConfigurationScreen) {
            mc.mouseHandler.releaseMouse();
        }
    }

    @SubscribeEvent
    public void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {

            LazyOptional<XYZPosAndRotation> existingCapability = player.getCapability(XYZPosAndRotationProvider.PLAYER_BACK_WEAPON_XYZ);

            final XYZPosAndRotationProvider provider = new XYZPosAndRotationProvider();

            if (!existingCapability.isPresent()) {
                LOGGER.info("attached, is Client: " + player.level.isClientSide());
                event.addCapability(new ResourceLocation(MOD_ID, "properties"), provider);

                //This event is called twice, one on Server and on Client, we have to ask the server to
                //send a packet about the player's capability
                //and to set it in our side

                if (player.level.isClientSide) {

                    //We have to use the execute method, otherwise the PacketChannel will be called too soon

                    Minecraft.getInstance().execute(() -> PacketChannel.sendToServer(new ResyncWithMePacket()));
                }
            } else {
                LOGGER.info("XYZPosAndRotation capability already present");
            }
        }
    }

    @SuppressWarnings("CodeBlock2Expr")
    @SubscribeEvent
    public void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().getCapability(XYZPosAndRotationProvider.PLAYER_BACK_WEAPON_XYZ).ifPresent(oldStore -> {
                event.getOriginal().getCapability(XYZPosAndRotationProvider.PLAYER_BACK_WEAPON_XYZ).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }


    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.side != null) {
            event.player.getCapability(XYZPosAndRotationProvider.PLAYER_BACK_WEAPON_XYZ).ifPresent(xyzPosAndRotation -> {
                    //LOGGER.info(String.valueOf(xyzPosAndRotation.getX()) + event.side);
            });
        }
    }

}

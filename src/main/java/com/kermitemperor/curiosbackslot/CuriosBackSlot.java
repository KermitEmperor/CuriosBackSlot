package com.kermitemperor.curiosbackslot;

import com.kermitemperor.curiosbackslot.client.KeyBinding;
import com.kermitemperor.curiosbackslot.config.ClientConfig;
import com.kermitemperor.curiosbackslot.network.PacketChannel;
import com.kermitemperor.curiosbackslot.network.packet.SwitchPacket;
import com.kermitemperor.curiosbackslot.render.GuiRenderer;
import com.kermitemperor.curiosbackslot.util.CuriosBackSlotHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
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

import java.util.Set;

@Mod(CuriosBackSlot.MOD_ID)
public class CuriosBackSlot {

    public static final String MOD_ID = "curiosbackslot";
    @SuppressWarnings("unused")
    public static final Logger LOGGER = LogUtils.getLogger();


    public CuriosBackSlot() {

        IEventBus ModEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEventBus.addListener(this::setup);
        ModEventBus.addListener(this::clientSetup);
        ModEventBus.addListener(this::enqueue);

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

    public static boolean canEquip(Item item) {
        if (ClientConfig.DISALLOW_CURIO_ITEMS.get()) {
            Set<String> curioTags = CuriosApi.getCuriosHelper().getCurioTags(item);
            return curioTags.isEmpty() || curioTags.contains(CuriosBackSlotHandler.SLOT_ID);
        }
        return true;
    }

    @SubscribeEvent
    public void onEquip(CurioEquipEvent event) {
        if (event.getSlotContext().identifier().equals(CuriosBackSlotHandler.SLOT_ID)) {
            if (!canEquip(event.getStack().getItem())) {
                event.setResult(Event.Result.DENY);
                return;
            }
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
        }
    }
}

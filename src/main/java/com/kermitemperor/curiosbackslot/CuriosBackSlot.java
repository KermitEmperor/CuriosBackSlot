package com.kermitemperor.curiosbackslot;

import com.kermitemperor.curiosbackslot.client.KeyBinding;
import com.kermitemperor.curiosbackslot.config.ClientConfig;
import com.kermitemperor.curiosbackslot.network.PacketChannel;
import com.kermitemperor.curiosbackslot.network.packet.SwitchPacket;
import com.kermitemperor.curiosbackslot.render.GuiRenderer;
import com.kermitemperor.curiosbackslot.util.CuriosBackSlotHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
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
        ModEventBus.addListener(this::onRegisterOverlay);


        IEventBus ForgeEventBus = MinecraftForge.EVENT_BUS;
        ForgeEventBus.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ClientConfig.SPEC, MOD_ID + "-client.toml");
    }


    private void setup(final FMLCommonSetupEvent event) {
        PacketChannel.register();
    }

    private void onRegisterOverlay(final RegisterGuiOverlaysEvent event) {
        event.registerAboveAll(CuriosBackSlotHandler.SLOT_ID + "_overlay", GuiRenderer.overlay);
    }

    public static boolean canEquip(ItemStack itemStack) {
        if (ClientConfig.DISALLOW_CURIO_ITEMS.get()) {
            Set<String> validCurioSlots = CuriosApi.getItemStackSlots(itemStack).keySet();
            return validCurioSlots.isEmpty() || validCurioSlots.contains(CuriosBackSlotHandler.SLOT_ID);
        }
        return true;
    }


    @SubscribeEvent
    public void onEquip(CurioEquipEvent event) {
        if (event.getSlotContext().identifier().equals(CuriosBackSlotHandler.SLOT_ID)) {
            if (!canEquip(event.getStack())) {
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

    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientProxy {
        @SubscribeEvent
        public static void onRegisterKeys(final RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.SWITCHING_KEY);
        }
    }
}

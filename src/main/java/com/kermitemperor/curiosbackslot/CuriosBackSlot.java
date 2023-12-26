package com.kermitemperor.curiosbackslot;

import com.kermitemperor.curiosbackslot.client.KeyBinding;
import com.kermitemperor.curiosbackslot.config.ClientConfig;
import com.kermitemperor.curiosbackslot.render.GuiRenderer;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
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
    public static final String SLOT_ID = "back_weapon";

    public static final Logger LOGGER = LogUtils.getLogger();

    public CuriosBackSlot() {

        IEventBus ModEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEventBus.addListener(this::setup);
        ModEventBus.addListener(this::enqueue);
        ModEventBus.addListener(this::clientSetup);

        IEventBus ForgeEventBus = MinecraftForge.EVENT_BUS;
        ForgeEventBus.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ClientConfig.SPEC, MOD_ID + "-client.toml");
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        OverlayRegistry.registerOverlayTop(SLOT_ID, new GuiRenderer());
        ClientRegistry.registerKeyBinding(KeyBinding.SWITCHING_KEY);
    }

    private void enqueue(final InterModEnqueueEvent evt) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE,
                () -> new SlotTypeMessage
                        .Builder(SLOT_ID)
                        .icon(new ResourceLocation(CuriosApi.MODID,"slot/empty_"+SLOT_ID+"_slot"))
                        .priority(1)
                        .size(1)
                        .build()
        );
    }

    @SubscribeEvent
    public void onEquip(CurioEquipEvent event) {
        if (event.getSlotContext().identifier().equals(SLOT_ID)) {
            event.setResult(Event.Result.ALLOW);
        }
    }

}

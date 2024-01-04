package com.kermitemperor.curiosbackslot.client;

import com.kermitemperor.curiosbackslot.network.PacketChannel;
import com.kermitemperor.curiosbackslot.network.packet.RenderInfoCapabilityPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

import static com.kermitemperor.curiosbackslot.CuriosBackSlot.LOGGER;

public class BackWeaponConfigurationScreen extends Screen {
    public BackWeaponConfigurationScreen() {

        super(new TextComponent("yeah"));
    }

    protected Minecraft mc = super.minecraft;

    private double x;
    private double y;

    //TODO make a usable stylish screen
    //TODO show what are the current values

    @Override
    public void init() {
        this.addRenderableWidget(new Button(100, 100 ,18 ,20, new TextComponent("1+"), (pButton) -> {
            LOGGER.info("pressed");
            this.x += 1;
        }));
        this.addRenderableWidget(new Button(100, 122 ,18 ,20, new TextComponent("0.1+"), (pButton) -> {
            LOGGER.info("pressed");
            this.x += 0.1;
        }));

        this.addRenderableWidget(new Button(120, 100 ,18 ,20, new TextComponent("1+"), (pButton) -> {
            LOGGER.info("pressed");
            this.y += 1;
        }));
        this.addRenderableWidget(new Button(120, 122 ,18 ,20, new TextComponent("0.1+"), (pButton) -> {
            LOGGER.info("pressed");
            this.y += 0.1;
        }));

        this.addRenderableWidget(new Button(100, 144 ,18 ,20, new TextComponent("Save"), (pButton) -> {
            LOGGER.info("saving");
            PacketChannel.sendToServer(new RenderInfoCapabilityPacket(x, y, 0, 0, 0, 0));
        }));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }


}

package hxckdms.hxccore.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import hxckdms.hxccore.configs.Configuration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.EventListener;

public class EventDebugOverlay implements EventListener {
    @SubscribeEvent
    public void onDrawDebugText(RenderGameOverlayEvent.Text event) {
        if (Minecraft.getMinecraft().gameSettings.showDebugInfo || !Configuration.showPingOutsideF3Menu) event.right.add("");
        if (Minecraft.getMinecraft().gameSettings.showDebugInfo || Configuration.showPingOutsideF3Menu) event.right.add("Ping: " + ((GuiPlayerInfo) Minecraft.getMinecraft().getNetHandler().playerInfoMap.get(Minecraft.getMinecraft().thePlayer.getDisplayName())).responseTime);
    }
}

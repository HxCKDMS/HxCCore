package hxckdms.hxccore.event;

import hxckdms.hxccore.configs.Configuration;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.EventListener;

public class EventDebugOverlay implements EventListener {
    @SubscribeEvent
    public void onDrawDebugText(RenderGameOverlayEvent.Text event) {
        try {
            /*if (Minecraft.getMinecraft().gameSettings.showDebugInfo || !Configuration.showPingOutsideF3Menu)
                event.getRight().add("");*/
            if (Minecraft.getMinecraft().gameSettings.showDebugInfo || Configuration.showPingOutsideF3Menu)
                event.getRight().add("Ping: " + Minecraft.getMinecraft().thePlayer.connection.getPlayerInfo(Minecraft.getMinecraft().thePlayer.getUniqueID()).getResponseTime());
        } catch (Exception ignored) { }
    }
}

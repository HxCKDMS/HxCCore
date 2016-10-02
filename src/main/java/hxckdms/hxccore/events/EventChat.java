package hxckdms.hxccore.events;

import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.EventListener;

public class EventChat implements EventListener {
    @SubscribeEvent
    public void onServerChatEvent(ServerChatEvent event) {
        System.out.println("test");
    }
}

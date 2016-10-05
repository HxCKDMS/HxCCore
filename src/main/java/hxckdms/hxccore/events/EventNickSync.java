package hxckdms.hxccore.events;

import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.network.MessageNameTagSync;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.EventListener;

public class EventNickSync implements EventListener {
    private int counter = 0;

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if ((counter++) == 200) GlobalVariables.network.sendToAll(new MessageNameTagSync(GlobalVariables.server.getPlayerList().getPlayerList()));
            if (counter > 200) counter = 0;
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        GlobalVariables.network.sendToAll(new MessageNameTagSync(GlobalVariables.server.getPlayerList().getPlayerList()));
    }
}

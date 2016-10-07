package hxckdms.hxccore.event;

import hxckdms.hxccore.api.event.EmoteEvent;
import hxckdms.hxccore.utilities.ColorHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.EventListener;

public class EventChat implements EventListener {
    @SubscribeEvent
    public void onServerChatEvent(ServerChatEvent event) {
        event.setComponent(ColorHelper.handleChat(event.getMessage(), event.getPlayer()));
    }

    @SubscribeEvent
    public void onEmoteEvent(EmoteEvent event) {
        event.setComponent(new TextComponentTranslation("* ").appendSibling(ColorHelper.handleNick((EntityPlayerMP) event.getSender(), false)).appendText(" ").appendSibling(ColorHelper.handleMessage(event.getMessage(), 'f')));
    }
}

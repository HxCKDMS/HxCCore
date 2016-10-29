package hxckdms.hxccore.event;

import hxckdms.hxccore.api.event.EmoteEvent;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.EventListener;

public class EventChat implements EventListener {
    @SubscribeEvent
    public void onServerChatEvent(ServerChatEvent event) {
        NBTTagCompound mutedPlayers = GlobalVariables.customWorldData.getTagCompound("mutedPlayers", new NBTTagCompound());
        if (mutedPlayers.getBoolean(event.getPlayer().getUniqueID().toString())) {
            event.getPlayer().addChatMessage(ServerTranslationHelper.getTranslation(event.getPlayer(), "chat.error.muted").setStyle(new Style().setColor(TextFormatting.RED)));
            event.setCanceled(true);
        }

        event.setComponent(ColorHelper.handleChat(event.getMessage(), event.getPlayer()));
    }

    @SubscribeEvent
    public void onEmoteEvent(EmoteEvent event) {
        if (event.getSender() instanceof EntityPlayerMP) {
            NBTTagCompound mutedPlayers = GlobalVariables.customWorldData.getTagCompound("mutedPlayers", new NBTTagCompound());
            if (mutedPlayers.getBoolean(((EntityPlayerMP) event.getSender()).getUniqueID().toString())) {
                event.getSender().addChatMessage(ServerTranslationHelper.getTranslation(event.getSender(), "chat.error.muted").setStyle(new Style().setColor(TextFormatting.RED)));
                event.setCanceled(true);
            }
        }

        event.setComponent(new TextComponentTranslation("* ").appendSibling(ColorHelper.handleNick((EntityPlayerMP) event.getSender(), false)).appendText(" ").appendSibling(ColorHelper.handleMessage(event.getMessage(), 'f')));
    }
}

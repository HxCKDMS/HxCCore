package hxckdms.hxccore.event;

import hxckdms.hxccore.api.event.EmoteEvent;
import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.PermissionHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.*;
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

        if (Configuration.herobrineMessages) {
            if (event.getMessage().toLowerCase().contains("herobrine") || event.getMessage().toLowerCase().contains("my lord")) {
                HxCPlayerInfoHandler.setBoolean(event.getPlayer(), "Herobrine", true);
                GlobalVariables.server.getPlayerList().sendChatMsg(ColorHelper.handleMessage("<&4Herobrine&f> &4What is your request mortal?", 'f'));
            } else if (HxCPlayerInfoHandler.getBoolean(event.getPlayer(), "Herobrine") && (event.getMessage().toLowerCase().contains("die") || event.getMessage().toLowerCase().contains("kill") || event.getMessage().toLowerCase().contains("misery") || event.getMessage().toLowerCase().contains("suffer") || event.getMessage().toLowerCase().contains("torment"))) {
                event.getPlayer().attackEntityFrom(new DamageSource("command_hxc_kill." + event.getPlayer().world.rand.nextInt(35)) {
                    @Override
                    public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
                        return ServerTranslationHelper.getTranslation(event.getPlayer(), "death.attack." + damageType, entityLivingBaseIn.getDisplayName());
                    }
                }.setDamageAllowedInCreativeMode().setDamageIsAbsolute().setDamageBypassesArmor(), Float.MAX_VALUE);
            } else if (HxCPlayerInfoHandler.getBoolean(event.getPlayer(), "Herobrine")) {
                GlobalVariables.server.getPlayerList().sendChatMsg(ColorHelper.handleMessage("<&4Herobrine&f> &4Mortals annoy me.", 'f'));
                HxCPlayerInfoHandler.setBoolean(event.getPlayer(), "Herobrine", false);
            }
        }
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

        event.setComponent(new TextComponentTranslation("* ").appendSibling(ColorHelper.handleNick((EntityPlayerMP) event.getSender(), false)).appendText(" ").appendSibling(Configuration.coloredChatMinimumPermissionLevel <= PermissionHandler.getPermissionLevel(event.getSender()) ? ColorHelper.handleMessage(event.getMessage(), 'f') : event.getComponent()));
    }
}

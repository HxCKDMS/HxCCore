package hxckdms.hxccore.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
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
import net.minecraft.util.*;
import net.minecraftforge.event.ServerChatEvent;

import java.util.EventListener;

public class EventChat implements EventListener {
    @SubscribeEvent
    public void onServerChatEvent(ServerChatEvent event) {
        NBTTagCompound mutedPlayers = GlobalVariables.customWorldData.getTagCompound("mutedPlayers", new NBTTagCompound());
        if (mutedPlayers.getBoolean(event.player.getUniqueID().toString())) {
            event.player.addChatMessage(ServerTranslationHelper.getTranslation(event.player, "chat.error.muted").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            event.setCanceled(true);
        }

        event.component = ColorHelper.handleChat(event.message, event.player);

        System.out.println(event.component.getUnformattedTextForChat());

        if (Configuration.herobrineMessages) {
            if (event.message.toLowerCase().contains("herobrine") || event.message.toLowerCase().contains("my lord")) {
                HxCPlayerInfoHandler.setBoolean(event.player, "Herobrine", true);
                GlobalVariables.server.getConfigurationManager().sendChatMsg(ColorHelper.handleMessage("<&4Herobrine&f> &4What is your request mortal?", 'f'));
            } else if (HxCPlayerInfoHandler.getBoolean(event.player, "Herobrine") && (event.message.toLowerCase().contains("die") || event.message.toLowerCase().contains("kill") || event.message.toLowerCase().contains("misery") || event.message.toLowerCase().contains("suffer") || event.message.toLowerCase().contains("torment"))) {
                event.player.attackEntityFrom(new DamageSource("command_hxc_kill." + event.player.worldObj.rand.nextInt(35)) {
                    @Override
                    public IChatComponent func_151519_b(EntityLivingBase entityLivingBaseIn) {
                        return ServerTranslationHelper.getTranslation(event.player, "death.attack." + damageType, entityLivingBaseIn.getCommandSenderName());
                    }
                }.setDamageAllowedInCreativeMode().setDamageIsAbsolute().setDamageBypassesArmor(), Float.MAX_VALUE);
            } else if (HxCPlayerInfoHandler.getBoolean(event.player, "Herobrine")) {
                GlobalVariables.server.getConfigurationManager().sendChatMsg(ColorHelper.handleMessage("<&4Herobrine&f> &4Mortals annoy me.", 'f'));
                HxCPlayerInfoHandler.setBoolean(event.player, "Herobrine", false);
            }
        }
    }

    @SubscribeEvent
    public void onEmoteEvent(EmoteEvent event) {
        if (event.getSender() instanceof EntityPlayerMP) {
            NBTTagCompound mutedPlayers = GlobalVariables.customWorldData.getTagCompound("mutedPlayers", new NBTTagCompound());
            if (mutedPlayers.getBoolean(((EntityPlayerMP) event.getSender()).getUniqueID().toString())) {
                event.getSender().addChatMessage(ServerTranslationHelper.getTranslation(event.getSender(), "chat.error.muted").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                event.setCanceled(true);
            }
        }

        event.setComponent(new ChatComponentTranslation("* ").appendSibling(ColorHelper.handleNick((EntityPlayerMP) event.getSender(), false)).appendText(" ").appendSibling(Configuration.coloredChatMinimumPermissionLevel <= PermissionHandler.getPermissionLevel(event.getSender()) ? ColorHelper.handleMessage(event.getMessage(), 'f') : event.getComponent()));
    }
}

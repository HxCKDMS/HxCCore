package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Utils.ColorHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EventListener;
import java.util.UUID;
@SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
public class EventChat implements EventListener {

    @SubscribeEvent
    public void onServerChatEvent(ServerChatEvent event) {
        try {
            event.component = ColorHelper.handleChat(event.message, event.player);
        } catch (Exception unhandled) {
            event.setCanceled(true);
        }

        UUID UUID = event.player.getUniqueID();
        File worldData = new File(HxCCore.HxCCoreDir, "HxCWorld.dat");
        if (!worldData.exists()) worldData.mkdirs();
        try {
            NBTTagCompound mutes = NBTFileIO.getNbtTagCompound(worldData, "mutedPlayers");
            if (mutes.getBoolean(UUID.toString())) {
                event.player.addChatMessage(new ChatComponentText("\u00a76You're muted, attempting to speak is futile."));
                event.setCanceled(true);
            }
        } catch (Exception ignored) {}
    }


    @SubscribeEvent
    public void commandEvent(CommandEvent event) {
        if (event.sender instanceof EntityPlayerMP && event.sender.getCommandSenderName() != null) {
            if (event.command.getCommandName().equals("gamerule")) {
                HxCCore.updateGamerules();
            }
            String cmd = event.command.getCommandName() + " " + Arrays.asList(event.parameters).toString().replace(",", "").substring(1, Arrays.asList(event.parameters).toString().replace(",", "").length() - 1);

            CommandsConfig.BannedCommands.keySet().forEach(c -> {
                if (CommandsConfig.BannedCommands.get(c) == 0 && c.equalsIgnoreCase(cmd)) {
                    event.setCanceled(true);
                    throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.bannedCommand"));
                } else if (CommandsConfig.BannedCommands.get(c) == 1 && cmd.startsWith(c)) {
                    event.setCanceled(true);
                    throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.bannedCommand"));
                } else if (CommandsConfig.BannedCommands.get(c) == 2 && cmd.contains(c)) {
                    event.setCanceled(true);
                    throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.bannedCommand"));
                }
            });

            if (HxCCore.instance.HxCRules.get("LogCommands").equals("true"))
                if (!CommandsConfig.IgnoredCommands.contains(event.command.getCommandName().toLowerCase())) {
                    String time = "[" + String.valueOf(Calendar.getInstance().getTime()) + "] : ";
                    HxCCore.instance.logCommand(time + event.sender.getCommandSenderName() + " tried to execute command [/" + cmd + "]");
                }

            if (HxCCore.instance.HxCRules.get("ReportCommands").equals("true"))
                CommandsConfig.ReportedCommands.forEach(c -> {
                    if (cmd.startsWith(c) && !PermissionsHandler.getPlayersWithPermissionLevel(Configurations.Permissions.size() - 1).contains(event.sender)) {
                        ChatComponentText t = new ChatComponentText(event.sender.getCommandSenderName() + " has attempted to use command /" + cmd);
                        t.getChatStyle().setColor(EnumChatFormatting.GOLD);
                        PermissionsHandler.getPlayersWithPermissionLevel(Configurations.Permissions.size() - 1).forEach(p -> p.addChatMessage(t));
                    }
                });
        }
    }
}
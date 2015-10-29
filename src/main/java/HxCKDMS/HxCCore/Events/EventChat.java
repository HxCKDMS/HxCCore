package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.lib.References;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EventListener;
import java.util.UUID;

import static HxCKDMS.HxCCore.Handlers.NickHandler.getMessageHeader;
import static HxCKDMS.HxCCore.lib.References.CC;

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored", "SuspiciousMethodCalls"})
public class EventChat implements EventListener {

    @SubscribeEvent
    public void onServerChatEvent(ServerChatEvent event) {
        UUID UUID = event.player.getUniqueID();
        File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID.toString() + ".dat");
        if (!CustomPlayerData.exists()) return;

        File worldData = new File(HxCCore.HxCCoreDir, "HxCWorld.dat");
        if (!worldData.exists()) worldData.mkdirs();
        try {
            NBTTagCompound mutes = NBTFileIO.getNbtTagCompound(worldData, "mutedPlayers");
            if (mutes.getBoolean(UUID.toString())) {
                event.player.addChatMessage(new ChatComponentText("\u00a76You're muted, atempting to speak is futile."));
                event.setCanceled(true);
            }
        } catch (Exception ignored) {}

        String playerColor = NBTFileIO.getString(CustomPlayerData, "Color");

        String ChatColor;
        if (playerColor.equalsIgnoreCase("") || playerColor.equalsIgnoreCase("f"))
            ChatColor = CC + "f";
        else
            ChatColor = CC + playerColor;
//Going to soon make this a better system, but for now it's functional
        String[] tmp = event.message.split(" ");
        String tmp2 = "";
        for (String str : tmp) {
            tmp2 = tmp2 + " ";
            if (str.startsWith("&")) {
                String[] mg = str.split("&");
                for (String str2 : mg) {
                    if (str2.length() >= 2) {
                        ChatColor = ChatColor + CC + str2.charAt(0);
                        str2 = str2.substring(1);
                        tmp2 = tmp2 + ChatColor + str2;
                    } else {
                        if (!str2.trim().isEmpty())
                            ChatColor = ChatColor + CC + str2.charAt(0);
                    }
                }
            } else tmp2 = tmp2 + " " + ChatColor + str;
        }
        if (!tmp2.replaceAll(References.CC, "").trim().isEmpty())
            event.component = new ChatComponentTranslation(String.format(Configurations.formats.get("ChatFormat"), getMessageHeader(event.player), tmp2.trim().replaceAll("%", "%%")));
    }


    @SubscribeEvent
    public void commandEvent(CommandEvent event) {
        if (event.sender instanceof EntityPlayerMP) {
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

            if (!CommandsConfig.IgnoredCommands.contains(event.command.getCommandName().toLowerCase())) {
                String time = "[" + String.valueOf(Calendar.getInstance().getTime()) + "] : ";
                HxCCore.logCommand(time + event.sender.getCommandSenderName() + " tried to execute command [/" + cmd + "]");
            }

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
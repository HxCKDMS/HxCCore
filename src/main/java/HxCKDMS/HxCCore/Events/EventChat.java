package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
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
import java.util.*;

import static HxCKDMS.HxCCore.Handlers.NickHandler.getMessageHeader;
import static HxCKDMS.HxCCore.lib.References.CC;

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored", "SuspiciousMethodCalls"})
public class EventChat implements EventListener {
    List<Character> colours = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9');

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

        char CurrentColor = 'f';

        String ChatFormatting = "";
        if (!playerColor.equalsIgnoreCase("") &! playerColor.equalsIgnoreCase("f"))
            CurrentColor = playerColor.charAt(0);

        String[] tmp = event.message.split(" ");
        String tmp2 = "";
        for (String str : tmp) {
            tmp2 = tmp2 + " ";
            if (str.contains("//www.youtube.com/")) {
                str = str.replace("https", "http");
                //Replaces long youtube link with shortened version
                str = str.replace("//www.youtube.com/watch?v=", "//youtu.be/");
                //Tested and works.... http://puu.sh/l3L5U.png
                //http://youtu.be/X1a71UcM1wQ
            }
            if (str.startsWith("&")) {
                String[] mg = str.split("&");
                for (String str2 : mg) {
                    if (!str2.isEmpty()) {
                        if (colours.contains(str2.charAt(0))) {
                            CurrentColor = str2.charAt(0);
                            str2 = str2.substring(1).trim();
                        } else if (str2.charAt(0) == 'r') {
                            ChatFormatting = CC + "r";
                            str2 = str2.substring(1).trim();
                        } else {
                            if (!ChatFormatting.contains(CC + str2.charAt(0)))
                                ChatFormatting = ChatFormatting + CC + str2.charAt(0);
                            else
                                ChatFormatting = ChatFormatting.replace(CC + str2.charAt(0), "") + CC + str2;
                            str2 = str2.substring(1).trim();
                        }
                    }
                    tmp2 = tmp2 + ChatFormatting + CC + CurrentColor + str2;
                }
            } else tmp2 = tmp2 + ChatFormatting + CC + CurrentColor + str;
        }
        if (!tmp2.replaceAll(CC, "").trim().isEmpty())
            event.component = new ChatComponentTranslation(Configurations.formats.get("ChatFormat").replace("HEADER", getMessageHeader(event.player)).replace("MESSAGE", tmp2.trim().replaceAll("%", "%%")));
        else
            event.component = new ChatComponentTranslation(Configurations.formats.get("ChatFormat").replace("HEADER", getMessageHeader(event.player)).replace("MESSAGE", event.message.replaceAll("%", "%%")));
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
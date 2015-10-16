package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.api.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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

        String[] tmp = event.message.split(" ");
        String tmp2 = "";
        for (String str : tmp) {
            str = " " + str;
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
            event.setComponent(new ChatComponentTranslation(String.format(Configurations.formats.get("ChatFormat"), getMessageHeader(event.player), tmp2.trim().replaceAll("%", "%%"))));
    }


    @SubscribeEvent
    public void commandEvent(CommandEvent event) throws WrongUsageException{
        if (event.sender instanceof EntityPlayerMP) {
            String cmd = event.command.getCommandName() + " " + Arrays.asList(event.parameters).toString().replace(",", "").substring(1, Arrays.asList(event.parameters).toString().replace(",", "").length() - 1);

            CommandsConfig.BannedCommands.keySet().forEach(c -> {
                if (CommandsConfig.BannedCommands.get(c) == 0 && c.contains(cmd)) {
                    event.setCanceled(true);
//                    throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.bannedCommand"));
                } else if (CommandsConfig.BannedCommands.get(c) == 1 && cmd.startsWith(c)) {
                    event.setCanceled(true);
//                    throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.bannedCommand"));
                } else if (CommandsConfig.BannedCommands.get(c) == 2) {
                    String[] tmp0 = c.split("##"), tmp2 = tmp0[1].split(" ");
                    int tmp1 = tmp0[0].length() - 1, tmp3 = tmp2[0].length() - 1;
                    if (Integer.getInteger(cmd.substring(tmp1, tmp3)) != null) {
                        event.setCanceled(true);
//                        throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.bannedCommand"));
                    }
                }//SPECIAL CIRCUMSTANCE TESTING MAY NOT GO ANY FURTHER
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
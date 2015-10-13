package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Utils.LogHelper;
import HxCKDMS.HxCCore.lib.References;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;

import java.io.File;
import java.util.Arrays;
import java.util.EventListener;
import java.util.UUID;

import static HxCKDMS.HxCCore.Handlers.NickHandler.getMessageHeader;
import static HxCKDMS.HxCCore.lib.References.CC;

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
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
            event.component = new ChatComponentTranslation(String.format(Configurations.formats.get("ChatFormat"), getMessageHeader(event.player), tmp2.trim().replaceAll("%", "%%")));
    }

    @SubscribeEvent
    public void commandEvent(CommandEvent event) {
        String cmd = event.command.getCommandName() + " " + Arrays.asList(event.parameters).toString().replace(",", "").substring(1, Arrays.asList(event.parameters).toString().replace(",", "").length()-1);
        if (CommandsConfig.BannedCommands.containsKey(cmd))
            event.setCanceled(true);
        LogHelper.info(event.sender.getCommandSenderName() + " tried to execute command [/" + cmd + "]", References.MOD_NAME);
    }
}
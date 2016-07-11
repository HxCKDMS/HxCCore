package HxCKDMS.HxCCore.api.Handlers;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.util.UUID;

import static HxCKDMS.HxCCore.lib.References.*;
import static HxCKDMS.HxCCore.Configs.Configurations.*;

public class NickHandler {
    public static String getMessageHeader(EntityPlayerMP player) {
        UUID UUID = player.getUniqueID();

        File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
        NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
        int SenderPermLevel = Permissions.getInteger(player.getDisplayName());

        String rawGroup, formattedString = "<" + getColouredNick(player) + ">";

        rawGroup = CC + PERM_COLOURS[SenderPermLevel] + PERM_NAMES[SenderPermLevel];

        if (EnableGroupTagInChat)
            formattedString = String.format(formats.get("GroupFormat"), rawGroup) + formattedString;

        if (EnableHxCTagInChat && HxCCore.HxCLabels.containsKey(UUID))
            formattedString = String.format(formats.get("HxCFormat"), CC + HxCCore.HxCLabels.get(UUID)) + formattedString;
        String gm = "";
        if (Configurations.GameMode)
            gm = "(" + (player.capabilities.isCreativeMode ? "&6Creative" : player.capabilities.allowEdit ? "" : "&bAdventure") + "&f)";

        if (!gm.isEmpty() && !gm.equals("(&f)"))
            formattedString = gm + formattedString;

        return formattedString.replaceAll("&", CC);
    }

    public static String getColouredNick(EntityPlayerMP player) {
        UUID UUID = player.getUniqueID();
        File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID.toString() + ".dat");
        String nick = NBTFileIO.getString(CustomPlayerData, "nickname"); //canSendCommands
        boolean isOpped = player.mcServer.getConfigurationManager().func_152596_g(player.getGameProfile());

        String DrZed = "";
        if (UUID.toString().equalsIgnoreCase("f636c1c4-a2c5-4b4d-b43a-5e419eb48bfb"))
            DrZed = "&3DrZed";

        String tmp = !nick.isEmpty() ? nick : (DrZed.isEmpty() ? player.getDisplayName() : DrZed);

        if (nick.isEmpty() && isOpped) tmp = CC + '4' + tmp;
        else if (nick.isEmpty()) tmp = CC + 'f' + tmp;

        for (Character bannedChar : Configurations.bannedColorCharacters)
            if (tmp.contains(CC + bannedChar))
                tmp = tmp.replaceAll(CC + bannedChar, "");

        return tmp + CC + 'f';
    }
}

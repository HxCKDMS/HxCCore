package HxCKDMS.HxCCore.Handlers;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.util.UUID;

public class NickHandler {
    public final static String CC = "\u00A7";

    public static String getPlayerNickName(EntityPlayerMP player){
        UUID UUID = player.getUniqueID();
        File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID.toString() + ".dat");
        String nick = NBTFileIO.getString(CustomPlayerData, "nickname");

        File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
        NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
        int SenderPermLevel = Permissions.getInteger(player.getDisplayName());

        //Color Code

        String rawGroup;
        switch(SenderPermLevel){
            case 1:
                rawGroup = CC + Configurations.PermLevelColor.get(1) + Configurations.PermLevelName.get(1);
                break;
            case 2:
                rawGroup = CC + Configurations.PermLevelColor.get(2) + Configurations.PermLevelName.get(2);
                break;
            case 3:
                rawGroup = CC + Configurations.PermLevelColor.get(3) + Configurations.PermLevelName.get(3);
                break;
            case 4:
                rawGroup = CC + Configurations.PermLevelColor.get(4) + Configurations.PermLevelName.get(4);
                break;
            case 5:
                rawGroup = CC + Configurations.PermLevelColor.get(5) + Configurations.PermLevelName.get(5);
                break;
            default:
                rawGroup = CC + Configurations.PermLevelColor.get(0) + Configurations.PermLevelName.get(0);
                break;
        }

        String tag = "";
        if(HxCCore.coders.contains(UUID))
            tag = String.format(Configurations.TagFormat, "&bHxC");
        else if(HxCCore.supporters.contains(UUID))
            tag = String.format(Configurations.TagFormat, "&4HxC Supporter");
        else if(HxCCore.helpers.contains(UUID))
            tag = String.format(Configurations.TagFormat, "&aHxC Helper");
        else if(HxCCore.artists.contains(UUID))
            tag = String.format(Configurations.TagFormat, "&cHxC Artist");

        boolean isOpped = player.mcServer.getConfigurationManager().func_152596_g(player.getGameProfile());

        nick = CC + "f" + nick;

        String opped = CC + "4" + player.getDisplayName() + CC + "f";
        String name = player.getDisplayName();
        String nicked = nick;

        if (Configurations.GroupInChat) {
            opped = String.format(Configurations.GroupFormat, rawGroup) + opped;
            name = String.format(Configurations.GroupFormat, rawGroup) + name;
            nicked = String.format(Configurations.GroupFormat, rawGroup) + nicked;
        }

        if(Configurations.TagInChat && !tag.equalsIgnoreCase("")){
            opped = tag + opped;
            name = tag + name;
            nicked = tag + nicked;
        }

        nicked = nicked.replace("&", CC) + CC + "f";
        opped = opped.replace("&", CC) + CC + "f";
        name = name.replace("&", CC) + CC + "f";

        if(isOpped && nick.equals(CC + "f"))
            return opped;
        else if(nick.equals(CC + "f"))
            return name;
        else
            return nicked;
    }
}

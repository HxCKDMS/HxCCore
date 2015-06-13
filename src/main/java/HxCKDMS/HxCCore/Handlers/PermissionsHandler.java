package HxCKDMS.HxCCore.Handlers;

import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;

@SuppressWarnings("unused")
public class PermissionsHandler {
    public static boolean canUseCommand (int RequiredLevel, EntityPlayer player) {
        File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
        NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
        int SenderPermLevel = Permissions.getInteger(player.getDisplayNameString());
        boolean isopped = HxCCore.server.getConfigurationManager().canSendCommands(player.getGameProfile());
        return (isopped || SenderPermLevel >= RequiredLevel);
    }
    public static int permLevel (EntityPlayer player) {
        File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
        NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
        return Permissions.getInteger(player.getDisplayNameString());
    }
}

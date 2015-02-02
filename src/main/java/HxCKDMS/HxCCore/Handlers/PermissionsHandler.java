package HxCKDMS.HxCCore.Handlers;

import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;

public class PermissionsHandler {
    public static boolean canUseCommand (int RequiredLevel, EntityPlayer player) {
        File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
        NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
        int SenderPermLevel = Permissions.getInteger(player.getDisplayName());
        boolean isopped = HxCCore.server.getConfigurationManager().func_152596_g(player.getGameProfile());
        return (isopped || SenderPermLevel >= RequiredLevel);
    }
    public static int permLevel (EntityPlayer player) {
        File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
        NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
        return Permissions.getInteger(player.getDisplayName());
    }
}
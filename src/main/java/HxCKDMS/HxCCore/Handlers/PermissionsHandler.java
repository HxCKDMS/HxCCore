package HxCKDMS.HxCCore.Handlers;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "unchecked"})
public class PermissionsHandler {
    public static boolean canUseCommand(int RequiredLevel, EntityPlayer player) {
        File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
        NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
        int SenderPermLevel = Permissions.getInteger(player.getDisplayNameString());
        boolean isopped = HxCCore.server.getConfigurationManager().canSendCommands(player.getGameProfile());
        return (isopped || SenderPermLevel >= RequiredLevel);
    }

    public static int permLevel(EntityPlayer player) {
        File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
        NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
        boolean isopped = HxCCore.server.getConfigurationManager().canSendCommands(player.getGameProfile());
        return isopped ? Configurations.Permissions.size() - 1 : Permissions.getInteger(player.getDisplayNameString());
    }

    public static List<EntityPlayerMP> getPlayersWithPermissionLevel(int PermissionLevel) {
        File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
        NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
        return (((List<EntityPlayerMP>) HxCCore.server.getEntityWorld().playerEntities).stream().filter(p ->
                Permissions.getInteger(p.getCommandSenderName()) == PermissionLevel).map(p -> p).collect(Collectors.toList()));
    }

    public static List<EntityPlayerMP> getPlayersWithAndAbovePermissionLevel(int PermissionLevel) {
        File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
        NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
        return (((List<EntityPlayerMP>) HxCCore.server.getEntityWorld().playerEntities).stream().filter(p ->
                Permissions.getInteger(p.getCommandSenderName()) >= PermissionLevel).map(p -> p).collect(Collectors.toList()));
    }
}

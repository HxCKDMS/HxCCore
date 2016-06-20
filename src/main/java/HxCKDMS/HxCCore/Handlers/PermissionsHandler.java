package HxCKDMS.HxCCore.Handlers;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "unchecked"})
public class PermissionsHandler {
    private static NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(HxCCore.PermissionsData, "Permissions");
    public static boolean canUseCommand(int RequiredLevel, EntityPlayer player) {
        return (HxCCore.server.getConfigurationManager().canSendCommands(player.getGameProfile()) ||
                permLevel(player) >= RequiredLevel);
    }

    public static boolean hasHighestPermissions(EntityPlayer player) {
        return (HxCCore.server.getConfigurationManager().canSendCommands(player.getGameProfile()) ||
                permLevel(player) == Configurations.Permissions.size()-1 || player.capabilities.isCreativeMode);
    }

    public static int permLevel(EntityPlayer player) {
        return HxCCore.server.getConfigurationManager().canSendCommands(player.getGameProfile()) ?
                Configurations.Permissions.size() - 1 : (Permissions.getInteger(player.getDisplayName()) > Configurations.Permissions.size()-1) ? Configurations.Permissions.size()-1 : Permissions.getInteger(player.getDisplayName());
    }

    public static List<EntityPlayerMP> getPlayersWithPermissionLevel(int PermissionLevel) {
        return (((List<EntityPlayerMP>) HxCCore.server.getEntityWorld().playerEntities).stream().filter(p ->
                Permissions.getInteger(p.getCommandSenderName()) == PermissionLevel).map(p -> p).collect(Collectors.toList()));
    }

    public static List<EntityPlayerMP> getPlayersWithAndAbovePermissionLevel(int PermissionLevel) {
        return (((List<EntityPlayerMP>) HxCCore.server.getEntityWorld().playerEntities).stream().filter(p ->
                Permissions.getInteger(p.getCommandSenderName()) >= PermissionLevel).map(p -> p).collect(Collectors.toList()));
    }
}

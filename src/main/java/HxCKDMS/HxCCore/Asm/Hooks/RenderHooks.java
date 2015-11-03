package HxCKDMS.HxCCore.Asm.Hooks;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.UUID;

import static HxCKDMS.HxCCore.lib.References.CC;

@SuppressWarnings("unused")
public class RenderHooks {
    public static HashMap<String, String> nameNicks = new HashMap<>();
    public static HashMap<String, Boolean> isPlayerOp = new HashMap<>();

    public static String getName(String name, Entity entity) {
        UUID UUID = entity.getUniqueID();

        try {
            if (isPlayerOp.get(UUID.toString()))
                name = References.CC + "4" + name;
        } catch (NullPointerException ignored) {}

        String nick = nameNicks.getOrDefault(UUID.toString(), "");

        if (nick != null && !nick.equals("")) {
            name = nick;
        }

        name = name.replace("&", CC) + CC + "f";

        if (entity instanceof EntityPlayer)
            return Configurations.formats.get("PlayerNametagFormat").replace("HXC", getHxCTag(UUID)).replace("GROUP", getGroupTag(UUID, entity.getCommandSenderName())).replace("NAME", name);

        return name;
    }

    private static String getHxCTag(UUID UUID) {
        if(HxCCore.HxCLabels.containsKey(UUID))
            return CC + "r" + "[" + CC + HxCCore.HxCLabels.get(UUID) + CC + "r]";
        return "";
    }

    private static String getGroupTag(UUID UUID, String name) {
        return CC + "r" + "[" + CC + References.PERM_NAMES[PermissionsHandler.permLevel(HxCCore.server.getConfigurationManager().getPlayerByUsername(name))] + CC + "r]";
    }
}

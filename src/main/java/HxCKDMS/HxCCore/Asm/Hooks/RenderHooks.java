package HxCKDMS.HxCCore.Asm.Hooks;

import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.entity.Entity;

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

        if(HxCCore.coders.contains(UUID))
            return CC + "b[HxC] " + CC + "r" + name;
        else if(HxCCore.supporters.contains(UUID))
            return CC + "4[HxC Supporter] " + CC + "r" + name;
        else if(HxCCore.helpers.contains(UUID))
            return CC + "a[HxC Helper] " + CC + "r" + name;
        else if(HxCCore.artists.contains(UUID))
            return CC + "c[HxC Artist] " + CC + "r" + name;
        else
            return name;
    }
}

package HxCKDMS.HxCCore.Asm.Hooks;

import HxCKDMS.HxCCore.Handlers.NickHandler;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.entity.Entity;

import java.util.HashMap;
import java.util.UUID;

@SuppressWarnings("unused")
public class RenderHooks {
    public static HashMap<String, String> nameNicks = new HashMap<>();
    public static HashMap<String, Boolean> isPlayerOp = new HashMap<>();

    public static String getName(String name, Entity entity) {
        UUID UUID = entity.getUniqueID();

        try {
            if (isPlayerOp.get(UUID.toString()))
                name = NickHandler.CC + "4" + name;
        } catch (NullPointerException ignored) {}

        String nick;
        try {
            nick = nameNicks.get(UUID.toString());
        } catch (NullPointerException unhandled) {
            nick = "";
        }

        if (nick != null && !nick.equals("")) {
            name = nick;
        }

        name = name.replace("&", NickHandler.CC) + NickHandler.CC + "f";

        if(HxCCore.coders.contains(UUID))
            return NickHandler.CC + "b[HxC] " + NickHandler.CC + "r" + name;
        else if(HxCCore.supporters.contains(UUID))
            return NickHandler.CC + "4[HxC Supporter] " + NickHandler.CC + "r" + name;
        else if(HxCCore.helpers.contains(UUID))
            return NickHandler.CC + "a[HxC Helper] " + NickHandler.CC + "r" + name;
        else if(HxCCore.artists.contains(UUID))
            return NickHandler.CC + "c[HxC Artist] " + NickHandler.CC + "r" + name;
        else
            return name;
    }
}

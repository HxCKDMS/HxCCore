package HxCKDMS.HxCCore.Asm.Hooks;

import HxCKDMS.HxCCore.Events.EventChat;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.entity.Entity;

import java.util.HashMap;
import java.util.UUID;

@SuppressWarnings("unused")
public class RenderHooks {
    public static HashMap<String, String> nameNicks = new HashMap<String, String>();
    public static HashMap<String, Boolean> isPlayerOp = new HashMap<String, Boolean>();

    public static String getName(String name, Entity entity) {
        UUID UUID = entity.getUniqueID();

        try {
            if (isPlayerOp.get(UUID.toString()))
                name = EventChat.CC + "4" + name;
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

        name = name.replace("&", EventChat.CC) + EventChat.CC + "f";

        if(HxCCore.coders.contains(UUID))
            return EventChat.CC + "b[HxC] " + EventChat.CC + "r" + name;
        else if(HxCCore.supporters.contains(UUID))
            return EventChat.CC + "4[HxC Supporter] " + EventChat.CC + "r" + name;
        else if(HxCCore.helpers.contains(UUID))
            return EventChat.CC + "a[HxC Helper] " + EventChat.CC + "r" + name;
        else if(HxCCore.artists.contains(UUID))
            return EventChat.CC + "c[HxC Artist] " + EventChat.CC + "r" + name;
        else
            return name;
    }
}

package HxCKDMS.HxCCore.Asm.Hooks;

import HxCKDMS.HxCCore.Events.EventChat;
import net.minecraft.entity.Entity;

import java.util.HashMap;

public class RenderHooks {
    public static HashMap<String, String> nameNicks = new HashMap<String, String>();
    public static HashMap<String, Boolean> isPlayerOp = new HashMap<String, Boolean>();

    public static String getName(String name, Entity entity){
        String UUID = entity.getUniqueID().toString();

        try{
            if(isPlayerOp.get(entity.getUniqueID().toString()))
                name = EventChat.CC + "4" + name;
        }catch(NullPointerException ignored){}

        String nick;
        try{
            nick = nameNicks.get(UUID);
        }catch(NullPointerException unhandled){
            nick = "";
        }

        if(nick != null && !nick.equals("")){
            name = nick;
        }

        name = name.replace("&", EventChat.CC) + EventChat.CC + "f";

        return name;
    }
}

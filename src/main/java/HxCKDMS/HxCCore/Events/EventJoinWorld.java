package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Configs.Config;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.Utils.LogHelper;
import HxCKDMS.HxCCore.lib.References;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.io.File;
import java.io.IOException;
import java.util.EventListener;

@SuppressWarnings("unused")
public class EventJoinWorld implements EventListener {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.entity;
            
            // Player data file
            try {
                String UUID = player.getUniqueID().toString();
                File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
                boolean success;
                if (!CustomPlayerData.exists()) {
                    success = CustomPlayerData.createNewFile();
                    NBTFileIO.setFloat(CustomPlayerData, "Soul", 1f);
                    NBTFileIO.setInteger(CustomPlayerData, "SoulTimer", 12000);
                    if (Config.DebugMode) {
                        LogHelper.debug("File HxC-" + UUID + ".dat" + (success ? " was created." : " could not be created."), References.MOD_NAME);
                    }
                }
                NBTFileIO.setString(CustomPlayerData, "username", player.getDisplayName());
            } catch (IOException exceptions) {
                exceptions.printStackTrace();
            }

            File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
            NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
            NBTFileIO.setNbtTagCompound(PermissionsData, "Permissions", Permissions);
            int pl = Permissions.getInteger(player.getDisplayName());

            if (pl == 0) {
                Permissions.setInteger(player.getDisplayName(), 0);
            }
            NBTFileIO.setNbtTagCompound(PermissionsData, "Permissions", Permissions);
        }
    }
}

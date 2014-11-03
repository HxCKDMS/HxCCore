package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.*;
import java.util.List;

public class CommandSetHome implements ISubCommand {
    public static CommandSetHome instance = new CommandSetHome();

    @Override
    public String getCommandName() {
        return "setHome";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        EntityPlayerMP player = (EntityPlayerMP)sender;
        String UUID = player.getUniqueID().toString();
        File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
        try{
            NBTTagCompound playerData = CompressedStreamTools.read(CustomPlayerData);
            NBTTagCompound home = playerData.getCompoundTag("home");
            NBTTagCompound homeDir = new NBTTagCompound();

            String hName = args.length == 1 ? "default" : args[1];

            int x = (int)player.posX;
            int y = (int)player.posY;
            int z = (int)player.posZ;
            int dim = player.dimension;

            homeDir.setInteger("x", x);
            homeDir.setInteger("y", y);
            homeDir.setInteger("z", z);
            homeDir.setInteger("dim", dim);

            home.setTag(hName, homeDir);
            playerData.setTag("home", home);

            CompressedStreamTools.write(playerData, CustomPlayerData);

        }catch(Exception e){

            NBTTagCompound home = new NBTTagCompound();
            NBTTagCompound homeDir = new NBTTagCompound();
            NBTTagCompound playerData = player.getEntityData();

            String hName = args.length == 1 ? "default" : args[1];

            int x = (int)player.posX;
            int y = (int)player.posY;
            int z = (int)player.posZ;
            int dim = player.dimension;

            homeDir.setInteger("x", x);
            homeDir.setInteger("y", y);
            homeDir.setInteger("z", z);
            homeDir.setInteger("dim", dim);

            home.setTag(hName, homeDir);
            playerData.setTag("home", home);

            try {
                CompressedStreamTools.write(playerData, CustomPlayerData);
            }catch(IOException exception){
                exception.printStackTrace();
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}

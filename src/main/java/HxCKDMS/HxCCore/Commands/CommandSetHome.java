package HxCKDMS.HxCCore.Commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class CommandSetHome implements ISubCommand {
    public static CommandSetHome instance = new CommandSetHome();

    @Override
    public String getCommandName() {
        return "setHome";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        EntityPlayer player = (EntityPlayer)sender;
        try{
            NBTTagCompound playerData = player.getEntityData();
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

        }catch(NullPointerException e){

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
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}

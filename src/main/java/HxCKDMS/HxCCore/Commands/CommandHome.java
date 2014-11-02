package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Utils.Teleporter;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;


public class CommandHome implements ISubCommand {
    public static CommandHome instance = new CommandHome();

    @Override
    public String getCommandName() {
        return "home";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        EntityPlayerMP player = (EntityPlayerMP)sender;
        NBTTagCompound playerData = player.getEntityData();
        String hName = args.length == 1 ? "default" : args[1];
        try{
            NBTTagCompound homeDir = (NBTTagCompound)playerData.getTag("home");
            if(!homeDir.hasKey(hName)){
                throw new WrongUsageException("the home named: '" + hName + "' does not exist.");
            }
            NBTTagCompound home = (NBTTagCompound)homeDir.getTag(hName);
            Teleporter.transferPlayerToDimension(player, home.getInteger("dim"), player.mcServer.getConfigurationManager(), home.getInteger("x"), home.getInteger("y"), home.getInteger("z"));
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}

package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.util.List;

public class CommandSetWarp implements ISubCommand {
    public static CommandSetWarp instance = new CommandSetWarp();

    @Override
    public String getCommandName() {
        return "setWarp";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP)sender;
            File HxCWorldData = new File(HxCCore.HxCCoreDir, "HxCWorld.dat");

            NBTTagCompound warp = NBTFileIO.getNbtTagCompound(HxCWorldData, "warp");
            NBTTagCompound warpDir = new NBTTagCompound();

            String wName = args.length == 1 ? "default" : args[1];

            int x = (int)player.posX;
            int y = (int)player.posY;
            int z = (int)player.posZ;
            int dim = player.dimension;

            warpDir.setInteger("x", x);
            warpDir.setInteger("y", y);
            warpDir.setInteger("z", z);
            warpDir.setInteger("dim", dim);

            warp.setTag(wName, warpDir);

            NBTFileIO.setNbtTagCompound(HxCWorldData, "warp", warp);
        }else{
            sender.addChatMessage(new ChatComponentText("\u00A74This command can only be executed by a player."));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}

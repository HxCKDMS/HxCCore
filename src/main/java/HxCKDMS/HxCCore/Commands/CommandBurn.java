package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.util.List;

public class CommandBurn implements ISubCommand {
    public static CommandBurn instance = new CommandBurn();

    @Override
    public String getCommandName() {
        return "burn";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        switch(args.length){
            case 1: {
                if(sender instanceof EntityPlayerMP){
                    EntityPlayerMP player = (EntityPlayerMP)sender;
                    boolean isopped = HxCCore.server.getConfigurationManager().func_152596_g(player.getGameProfile());
                    File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
                    NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
                    int SenderPermLevel = Permissions.getInteger(player.getDisplayName());
                    if (SenderPermLevel >= 3 || isopped) {
                        player.setFire(750000000);
                    } else {
                        sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
                    }
                }else{
                    sender.addChatMessage(new ChatComponentText("\u00A74This command without parameters can only be executed by a player."));
                }
            }
            break;
            case 2: {
                EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
                player2.setFire(750000000);
            }
            case 3: {
                EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
                player2.setFire(Integer.parseInt(args[2]));
            }
            break;
            default: {
                throw new WrongUsageException("Correct usage is: /"+getCommandName()+" [player] [time]");

            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 2){
            return net.minecraft.command.CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}

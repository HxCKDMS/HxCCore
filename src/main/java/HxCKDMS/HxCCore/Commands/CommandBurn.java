package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
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
    public String getName() {
        return "burn";
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws WrongUsageException, PlayerNotFoundException {
        switch(args.length){
            case 1: {
                if(sender instanceof EntityPlayerMP){
                    EntityPlayerMP player = (EntityPlayerMP)sender;
                    boolean isopped = HxCCore.server.getConfigurationManager().canSendCommands(player.getGameProfile());
                    File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
                    NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
                    int SenderPermLevel = Permissions.getInteger(player.getName());
                    if (SenderPermLevel >= 3 || isopped) {
                        player.addChatMessage(new ChatComponentText("\u00A79You suddenly feel warmer."));
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
                player2.addChatMessage(new ChatComponentText("\u00A79You suddenly feel warmer."));
                player2.setFire(750000000);
                sender.addChatMessage(new ChatComponentText(player2.getName() + " \u00A74has been set on fire for 750000000 ticks."));
            }
            case 3: {
                EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
                player2.addChatMessage(new ChatComponentText("\u00A79You suddenly feel warmer."));
                player2.setFire(Integer.parseInt(args[2]));
                sender.addChatMessage(new ChatComponentText(player2.getName() + " \u00A74has been set on fire for " + Integer.parseInt(args[2]) + " ticks."));
            }
            break;
            default: {
                throw new WrongUsageException("Correct usage is: /"+getName()+" [player] [time]");

            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 2){
            return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}

package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.util.List;

public class CommandFly implements ISubCommand {
    public static CommandFly instance = new CommandFly();

    @Override
    public String getName() {
        return "fly";
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws PlayerNotFoundException, WrongUsageException {
        switch(args.length){
            case 1: {
                if(sender instanceof EntityPlayer){
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
                    NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
                    int SenderPermLevel = Permissions.getInteger(player.getName());
                    boolean isopped = HxCCore.server.getConfigurationManager().canSendCommands(player.getGameProfile());
                    if (SenderPermLevel >= 3 || isopped) {
                        player.capabilities.allowFlying = !player.capabilities.allowFlying;
                        player.capabilities.isFlying = !player.capabilities.isFlying;
                        player.sendPlayerAbilities();
                        player.addChatComponentMessage(new ChatComponentText((player.capabilities.allowFlying ? "\u00A76Enabled" : "\u00A76Disabled")+" flight."));
                    } else {
                        sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
                    }

                }else{
                    sender.addChatMessage(new ChatComponentText("\u00A74This command without parameters can only be executed by a player."));
                }
            }
            break;
            case 2: {
                EntityPlayerMP player = (EntityPlayerMP) sender;
                EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
                player2.capabilities.allowFlying = !player2.capabilities.allowFlying;
                player2.capabilities.isFlying = !player2.capabilities.isFlying;
                player2.sendPlayerAbilities();
                player2.addChatMessage(new ChatComponentText(player2.capabilities.allowFlying ? "\u00A7bYou feel lighter." : "\u00A7bYou feel heavier."));
                player.addChatComponentMessage(new ChatComponentText((player2.capabilities.allowFlying ? "\u00A76Enabled" : "\u00A76Disabled") + " flight, for player " + player2.getName() + "."));

            }
            break;
            default: {
                throw new WrongUsageException("Correct usage is: /"+getName()+" [player]");

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

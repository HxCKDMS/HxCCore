package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.util.List;

public class CommandFeed implements ISubCommand {

    public static CommandFeed instance = new CommandFeed();

    @Override
    public String getCommandName() {
        return "feed";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        switch(args.length){
            case 1: {
                if(sender instanceof EntityPlayer){
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
                    NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
                    int SenderPermLevel = Permissions.getInteger(player.getDisplayName());
                    boolean isopped = HxCCore.server.getConfigurationManager().func_152596_g(player.getGameProfile());
                    if (SenderPermLevel >= 2 || isopped) {
                        float plf = player.getFoodStats().getSaturationLevel() + player.getFoodStats().getFoodLevel();
                        float nf = 40 - plf;
                        player.getFoodStats().addStats(20, 20F);
                        player.addChatMessage(new ChatComponentText("\u00A7bYou suddenly feel well fed."));
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
                float plf = player2.getFoodStats().getSaturationLevel() + player2.getFoodStats().getFoodLevel();
                float nf = 40 - plf;
                player2.getFoodStats().addStats(20, 20F);
                player2.addChatMessage(new ChatComponentText("\u00A7bYou suddenly feel well fed."));
                sender.addChatMessage(new ChatComponentText("\u00A7eYou have shoved " + nf + "oz. of food down " + player2.getDisplayName() + "'s\u00A7e throat."));
            }
            break;
            default: {
                throw new WrongUsageException("Correct usage is: /"+getCommandName()+" [player]");

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

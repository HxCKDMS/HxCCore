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
import net.minecraft.util.DamageSource;

import java.io.File;
import java.util.List;

public class CommandKill implements ISubCommand {
    public static CommandKill instance = new CommandKill();

    @Override
    public String getCommandName() {
        return "kill";
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
                    if (SenderPermLevel >= 5 || isopped) {
                        player.attackEntityFrom(new DamageSource("command_kill").setDamageBypassesArmor().setDamageAllowedInCreativeMode(), Float.MAX_VALUE);
                    } else {
                        sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
                    }
                }else{
                    sender.addChatMessage(new ChatComponentText("The kill command without arguments can only be executed from a player."));
                }
            }
            break;
            case 2: {
                EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
                player2.attackEntityFrom(new DamageSource("command_kill").setDamageBypassesArmor().setDamageAllowedInCreativeMode(), Float.MAX_VALUE);
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

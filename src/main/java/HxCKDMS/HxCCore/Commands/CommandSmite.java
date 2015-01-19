package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.util.List;

public class CommandSmite implements ISubCommand {
    public static CommandSmite instance = new CommandSmite();

    @Override
    public String getCommandName() {
        return "smite";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) throws PlayerNotFoundException {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP) sender;
            File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
            NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
            int SenderPermLevel = Permissions.getInteger(player.getName());
            boolean isopped = HxCCore.server.getConfigurationManager().canSendCommands(player.getGameProfile());
            if (SenderPermLevel >= 3 || isopped) {
                if (args.length == 2) {
                    EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
                    smite(player2);
                } else {
                    smite(player);
                }
            } else {
                sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
            }
        } else {
            if (args.length == 2) {
                EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
                smite(player2);
            } else {
                sender.addChatMessage(new ChatComponentText("\u00A74This command without parameters can only be executed by a player."));
            }
        }
    }

    public void smite(EntityPlayer target) {
        target.worldObj.spawnEntityInWorld(new EntityLightningBolt(target.worldObj, target.posX, target.posY, target.posZ));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 2){
            return net.minecraft.command.CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}

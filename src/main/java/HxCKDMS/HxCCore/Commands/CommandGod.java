package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CommandGod implements ISubCommand {
    public static CommandGod instance = new CommandGod();

    @Override
    public String getCommandName() {
        return "god";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        switch(args.length){
            case 1: {
                if(sender instanceof EntityPlayer) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    String UUID = player.getUniqueID().toString();
                    File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");

                    NBTFileIO.setBoolean(CustomPlayerData, "god", !NBTFileIO.getBoolean(CustomPlayerData, "god"));
                    player.addChatComponentMessage(new ChatComponentText("turned " + (NBTFileIO.getBoolean(CustomPlayerData, "god") ? "on" : "off") + " god mode"));
                }else {
                    sender.addChatMessage(new ChatComponentText("the god command without arguments can only be executed from a player."));
                }
            }
            break;
            case 2: {
                EntityPlayerMP player = (EntityPlayerMP) sender;
                EntityPlayerMP player2 = net.minecraft.command.CommandBase.getPlayer(sender, args[1]);
                String UUID = player2.getUniqueID().toString();
                File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");

                NBTFileIO.setBoolean(CustomPlayerData, "god", !NBTFileIO.getBoolean(CustomPlayerData, "god"));
                player.addChatComponentMessage(new ChatComponentText("turned " + (NBTFileIO.getBoolean(CustomPlayerData, "god") ? "on" : "off") + " god mode"));
            }
            break;
            default: {
                throw new WrongUsageException("Correct usage is: /"+getCommandName()+" [player]");
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 2){
            return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}

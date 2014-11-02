package HxCKDMS.HxCCore.Commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

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
                    NBTTagCompound playerData  = player.getEntityData();
                    try{
                        playerData.setBoolean("god", !playerData.getBoolean("god"));
                    }catch(NullPointerException e){
                        playerData.setBoolean("god", true);
                    }
                    player.addChatComponentMessage(new ChatComponentText("turned " + (playerData.getBoolean("god") ? "on" : "off") + " god mode"));
                }else {
                    sender.addChatMessage(new ChatComponentText("the god command without arguments can only be executed from a player."));
                }
            }
            break;
            case 2: {
                EntityPlayerMP player = (EntityPlayerMP) sender;
                EntityPlayerMP player2 = net.minecraft.command.CommandBase.getPlayer(sender, args[1]);
                NBTTagCompound playerData2  = player2.getEntityData();
                try{
                    playerData2.setBoolean("god", !playerData2.getBoolean("god"));
                }catch(NullPointerException e){
                    playerData2.setBoolean("god", true);
                }
                player.addChatComponentMessage(new ChatComponentText("turned " + (playerData2.getBoolean("god") ? "on" : "off") + " god mode"));
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

package HxCKDMS.HxCCore.Commands;

import net.minecraft.command.*;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class CommandPlayerInfo implements ISubCommand {
    public static CommandPlayerInfo instance = new CommandPlayerInfo();

    @Override
    public String getCommandName() {
        return "playerInfo";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        EntityPlayerMP player2 = net.minecraft.command.CommandBase.getPlayer(sender, args[1]);

        sender.addChatMessage(new ChatComponentText(String.format("Player: %1$s is at x: %2$d, y: %3$d, z: %4$d in dimension: %5$d with gamemode: %6$s.", player2.getCommandSenderName(), (int)player2.posX, (int)player2.posY, (int)player2.posZ, player2.dimension, player2.theItemInWorldManager.getGameType().getName())));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    }
}

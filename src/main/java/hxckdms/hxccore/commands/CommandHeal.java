package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractMultiCommand;
import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.CommandState;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandHeal extends AbstractSubCommand {
    {
        permissionLevel = 2;
        state = CommandState.ENABLED;
    }

    @Override
    public String getCommandName() {
        return "heal";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (args.size() == 0 && !(sender instanceof EntityPlayerMP)) throw new PlayerNotFoundException();

        if (args.size() == 0 && sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            player.setHealth(player.getMaxHealth());

            TextComponentTranslation msg = new TextComponentTranslation("commands.heal.self");
            msg.getStyle().setColor(TextFormatting.GREEN);
            sender.addChatMessage(msg);
        } else {
            if (Arrays.asList(GlobalVariables.server.getPlayerList().getAllUsernames()).contains(args.get(0))) {
                EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.removeFirst());

                target.setHealth(target.getMaxHealth());

                TextComponentTranslation msgS = new TextComponentTranslation("commands.heal.other.sender");
                msgS.getStyle().setColor(TextFormatting.YELLOW);
                sender.addChatMessage(msgS);

                TextComponentTranslation msgT = new TextComponentTranslation("commands.heal.other.target");
                msgT.getStyle().setColor(TextFormatting.GOLD);
                sender.addChatMessage(msgT);
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return  args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames()) : null;
    }

    @Override
    public Class<? extends AbstractMultiCommand> getParentCommand() {
        return CommandHxC.class;
    }
}

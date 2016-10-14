package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.*;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandFly extends AbstractSubCommand {
    {
        permissionLevel = 1;
        state = CommandState.ENABLED;
    }

    @Override
    public String getCommandName() {
        return "fly";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    togglePlayerFlight(player);

                    TextComponentTranslation msg = ServerTranslationHelper.getTranslation(player, player.capabilities.allowFlying ? "commands.fly.self.enabled" : "commands.fly.self.disabled");
                    msg.getStyle().setColor(player.capabilities.allowFlying ? TextFormatting.GREEN : TextFormatting.YELLOW);

                    sender.addChatMessage(msg);
                }
                break;
            case 1:
                EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));

                TextComponentTranslation msgS = ServerTranslationHelper.getTranslation(sender, target.capabilities.allowFlying ? "commands.fly.other.sender.enabled" : "commands.fly.other.sender.disabled", sender.getDisplayName());
                msgS.getStyle().setColor(TextFormatting.YELLOW);
                sender.addChatMessage(msgS);

                TextComponentTranslation msgT = ServerTranslationHelper.getTranslation(target, target.capabilities.allowFlying ? "commands.fly.other.target.enabled" : "commands.fly.other.target.disabled", target.getDisplayName());
                msgT.getStyle().setColor(target.capabilities.allowFlying ? TextFormatting.GOLD : TextFormatting.RED);
                target.addChatMessage(msgT);

                break;
        }
    }

    private static void togglePlayerFlight(EntityPlayerMP player) throws CommandException {
        if (player.capabilities.isCreativeMode) throw new CommandException(ServerTranslationHelper.getTranslation(player, "commands.error.creativeMode").getUnformattedText());

        player.capabilities.allowFlying = !player.capabilities.allowFlying;
        player.capabilities.isFlying = !player.capabilities.isFlying;
        HxCPlayerInfoHandler.setBoolean(player, "AllowFlying", player.capabilities.allowFlying);
        player.sendPlayerAbilities();
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames()) : Collections.emptyList();
    }

    @Override
    public Class<? extends AbstractMultiCommand> getParentCommand() {
        return CommandHxC.class;
    }
}
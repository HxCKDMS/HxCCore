package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandFly extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 1;
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

                    sender.addChatMessage(ServerTranslationHelper.getTranslation(player, player.capabilities.allowFlying ? "commands.fly.self.enabled" : "commands.fly.self.disabled").setStyle(new Style().setColor(player.capabilities.allowFlying ? TextFormatting.GREEN : TextFormatting.YELLOW)));
                }
                break;
            case 1:
                EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, target.capabilities.allowFlying ? "commands.fly.other.sender.enabled" : "commands.fly.other.sender.disabled", sender.getDisplayName()).setStyle(new Style().setColor(TextFormatting.GRAY)));
                target.addChatMessage(ServerTranslationHelper.getTranslation(target, target.capabilities.allowFlying ? "commands.fly.other.target.enabled" : "commands.fly.other.target.disabled", target.getDisplayName()).setStyle(new Style().setColor(target.capabilities.allowFlying ? TextFormatting.GOLD : TextFormatting.RED)));
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
}

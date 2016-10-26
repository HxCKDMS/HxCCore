package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractMultiCommand;
import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.event.CommandEvents;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import hxckdms.hxccore.utilities.TeleportHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandTPA extends AbstractSubCommand {
    {
        permissionLevel = 1;
    }

    @Override
    public String getCommandName() {
        return "TPA";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (args.size() < 1) throw new TranslatedCommandException(sender, "commands.TPA.usage");

        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;

            switch (args.get(0)) {
                case "accept":
                    if (!CommandEvents.TPAList.containsKey(sender)) throw new TranslatedCommandException(sender, "commands.error.TPA.noRequest");
                    EntityPlayerMP requester = CommandEvents.TPAList.remove(sender).getRequester();

                    TeleportHelper.teleportEntityToDimension(requester, player.posX, player.posY, player.posZ, player.dimension);
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.TPA.accepted.sender", requester.getDisplayName()));
                    requester.addChatMessage(ServerTranslationHelper.getTranslation(requester, "commands.TPA.accepted.requester", sender.getDisplayName()));
                    break;
                case "deny":
                    if (!CommandEvents.TPAList.containsKey(sender)) throw new TranslatedCommandException(sender, "commands.error.TPA.noRequest");
                    requester = CommandEvents.TPAList.remove(sender).getRequester();
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.TPA.denied.sender", requester.getDisplayName()));
                    requester.addChatMessage(ServerTranslationHelper.getTranslation(requester, "commands.TPA.denied.requester", sender.getDisplayName()));
                    break;
                case "cancel":
                    if (CommandEvents.TPAList.entrySet().stream().noneMatch(entry -> entry.getValue().getRequester() == sender)) throw new TranslatedCommandException(sender, "commands.error.TPA.sender.notTeleporting");
                    EntityPlayerMP removed = null;

                    for (EntityPlayerMP playerMP : CommandEvents.TPAList.keySet()) {
                        removed = playerMP;
                        if (CommandEvents.TPAList.compute(playerMP, (key, value) -> value.getRequester() == sender ? null : value) == null) break;
                    }

                    if (removed == null) throw new NullPointerException("This wasn't supposed to happen.");

                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.TPA.canceled.sender"));
                    removed.addChatMessage(ServerTranslationHelper.getTranslation(removed, "commands.TPA.canceled.target"));

                    break;
                default:
                    EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));

                    if (CommandEvents.TPAList.entrySet().stream().anyMatch(entry -> entry.getValue().getRequester() == sender)) throw new TranslatedCommandException(sender, "commands.error.TPA.sender.teleporting");
                    if (CommandEvents.TPAList.putIfAbsent(target, new CommandEvents.TPARequest(player, 600)) != null) throw new TranslatedCommandException(sender, "commands.error.TPA.target.busy");

                    target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.TPA.request.target", player.getDisplayName()));
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.TPA.request.sender", target.getDisplayName()));
                    break;
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return new LinkedList<String>() {{
            if (CommandEvents.TPAList.keySet().contains(sender)) addAll(Arrays.asList("accept", "deny"));
            if (CommandEvents.TPAList.entrySet().stream().anyMatch(entry -> entry.getValue().getRequester() == sender)) add("cancel");
            addAll(CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames()));
        }};
    }

    @Override
    public Class<? extends AbstractMultiCommand> getParentCommand() {
        return CommandHxC.class;
    }
}

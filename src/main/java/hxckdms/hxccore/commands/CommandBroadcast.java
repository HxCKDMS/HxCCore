package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@HxCCommand
public class CommandBroadcast extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 4;
    }

    @Override
    public String getCommandName() {
        return "broadcast";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        boolean isPlayer = sender instanceof EntityPlayerMP;
        String message = args.stream().collect(Collectors.joining(" "));
        GlobalVariables.server.getPlayerList().sendChatMsg(ColorHelper.handleMessage(String.format(Configuration.broadcastLayout.replace("SENDER", "%1$s").replace("MESSAGE", "%2$s"), isPlayer ? getPlayerNameOrNick((EntityPlayerMP) sender) : sender.getName(), message), 'f'));
    }

    private static String getPlayerNameOrNick(EntityPlayerMP player) {
        if (HxCPlayerInfoHandler.getString(player, "NickName") == null || "".equals(HxCPlayerInfoHandler.getString(player, "NickName")))
            return Arrays.asList(GlobalVariables.server.getPlayerList().getOppedPlayerNames()).contains(player.getName()) ? "&4" + player.getName() : "&f" + player.getName();
        else return HxCPlayerInfoHandler.getString(player, "NickName");
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return Collections.emptyList();
    }
}

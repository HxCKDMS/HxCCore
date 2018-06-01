package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

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
    public String getName() {
        return "broadcast";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, LinkedList<String> args) throws CommandException {
        boolean isPlayer = sender instanceof EntityPlayerMP;
        String message = args.stream().collect(Collectors.joining(" "));
        server.getPlayerList().sendMessage(ColorHelper.handleMessage(String.format(Configuration.broadcastLayout.replace("SENDER", "%1$s").replace("MESSAGE", "%2$s"), isPlayer ? getPlayerNameOrNick((EntityPlayerMP) sender) : sender.getName(), message), 'f'));
    }

    private static String getPlayerNameOrNick(EntityPlayerMP player) {
        if (HxCPlayerInfoHandler.getString(player, "NickName") == null || "".equals(HxCPlayerInfoHandler.getString(player, "NickName")))
            return Arrays.asList(player.mcServer.getPlayerList().getOppedPlayerNames()).contains(player.getName()) ? "&4" + player.getDisplayNameString() : "&f" + player.getDisplayNameString();
        else return HxCPlayerInfoHandler.getString(player, "NickName");
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, LinkedList<String> args, @Nullable BlockPos targetPos) {
        return Collections.emptyList();
    }
}

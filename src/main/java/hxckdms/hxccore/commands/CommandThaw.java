package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

@HxCCommand
public class CommandThaw extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 4;
    }

    @Override
    public String getCommandName() {
        return "thaw";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP)) throw new TranslatedCommandException(sender, "commands.exception.playersOnly");
        EntityPlayerMP player = (EntityPlayerMP) sender;
        final int r = args.size() < 1 ? CommandBase.parseInt(args.get(0)) : 8;
        final Predicate<BlockPos> predicate = pos -> player.world.getBlockState(pos).getBlock() == Blocks.ICE || player.world.getBlockState(pos).getBlock() == Blocks.SNOW_LAYER;

        StreamSupport.stream(BlockPos.getAllInBoxMutable(new BlockPos(player.posX - r, player.posY - r, player.posZ - r), new BlockPos(player.posX + r, player.posY + r, player.posZ + r)).spliterator(), false).filter(predicate).forEach(pos -> {
            if (player.world.getBlockState(pos).getBlock() == Blocks.ICE) player.world.setBlockState(pos, Blocks.WATER.getDefaultState(), 3);
            else if (player.world.getBlockState(pos).getBlock() == Blocks.SNOW_LAYER) player.world.setBlockToAir(pos);
        });
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return args.size() == 1 ? Collections.singletonList(Integer.toString(8)) : Collections.emptyList();
    }
}

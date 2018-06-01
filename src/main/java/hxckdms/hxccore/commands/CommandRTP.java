package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.TeleportHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandRTP extends AbstractSubCommand<CommandHxC> {
    private static DecimalFormat posFormat = new DecimalFormat("#.###");

    {
        permissionLevel = 0;
    }

    @Override
    public String getName() {
        return "RTP";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    int tps = HxCPlayerInfoHandler.getInteger(player, "RTPUses");
                    if (tps >= Configuration.maxRTPUses)
                        throw new TranslatedCommandException(sender, "commands.error.maxRTPs");
                    else
                        HxCPlayerInfoHandler.setInteger(player, "RTPUses", tps + 1);
                    boolean negativex = player.world.rand.nextBoolean();
                    boolean negativez = player.world.rand.nextBoolean();

                    int x = player.world.rand.nextInt(Configuration.maxRTPRandom) + Configuration.minRTPRandom;
                    int z = player.world.rand.nextInt(Configuration.maxRTPRandom) + Configuration.minRTPRandom;
                    if (negativex) x = Math.negateExact(x);
                    if (negativez) z = Math.negateExact(z);

                    Chunk chunk;
                    if ((chunk = player.getServerWorld().getChunkProvider().getLoadedChunk((int)Math.round(player.posX + x) >> 4, (int)Math.round(player.posX + z) >> 4)) == null) {
                        chunk = player.getServerWorld().getChunkProvider().provideChunk((int)Math.round(player.posX + x) >> 4, (int)Math.round(player.posX + z) >> 4);
                    }

                    TeleportHelper.teleportEntityToDimension(player, player.posX + x,chunk.getHeight(new BlockPos(player.posX, 64, player.posX)) + 1, player.posZ + z, 0);
                }
                break;
            case 1:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    int tps = HxCPlayerInfoHandler.getInteger(player, "RTPUses");
                    if (tps >= Configuration.maxRTPUses)
                        throw new TranslatedCommandException(sender, "commands.error.maxRTPs");
                    else
                        HxCPlayerInfoHandler.setInteger(player, "RTPUses", tps + 1);
                    boolean negativex = Boolean.parseBoolean(args.removeFirst());
                    boolean negativez = player.world.rand.nextBoolean();

                    int x = player.world.rand.nextInt(Configuration.maxRTPRandom) + Configuration.minRTPRandom;
                    int z = player.world.rand.nextInt(Configuration.maxRTPRandom) + Configuration.minRTPRandom;
                    if (negativex) x = Math.negateExact(x);
                    if (negativez) z = Math.negateExact(z);

                    Chunk chunk;
                    if ((chunk = player.getServerWorld().getChunkProvider().getLoadedChunk((int)Math.round(player.posX + x) >> 4, (int)Math.round(player.posX + z) >> 4)) == null) {
                        chunk = player.getServerWorld().getChunkProvider().provideChunk((int)Math.round(player.posX + x) >> 4, (int)Math.round(player.posX + z) >> 4);
                    }

                    TeleportHelper.teleportEntityToDimension(player, player.posX + x,chunk.getHeight(new BlockPos(player.posX, 64, player.posX)) + 1, player.posZ + z, 0);
                }
                break;
            case 2:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    int tps = HxCPlayerInfoHandler.getInteger(player, "RTPUses");
                    if (tps >= Configuration.maxRTPUses)
                        throw new TranslatedCommandException(sender, "commands.error.maxRTPs");
                    else
                        HxCPlayerInfoHandler.setInteger(player, "RTPUses", tps + 1);
                    boolean negativex = Boolean.parseBoolean(args.removeFirst());
                    boolean negativez = Boolean.parseBoolean(args.removeFirst());

                    int x = player.world.rand.nextInt(Configuration.maxRTPRandom) + Configuration.minRTPRandom;
                    int z = player.world.rand.nextInt(Configuration.maxRTPRandom) + Configuration.minRTPRandom;
                    if (negativex) x = Math.negateExact(x);
                    if (negativez) z = Math.negateExact(z);

                    Chunk chunk;
                    if ((chunk = player.getServerWorld().getChunkProvider().getLoadedChunk((int)Math.round(player.posX + x) >> 4, (int)Math.round(player.posX + z) >> 4)) == null) {
                        chunk = player.getServerWorld().getChunkProvider().provideChunk((int)Math.round(player.posX + x) >> 4, (int)Math.round(player.posX + z) >> 4);
                    }

                    TeleportHelper.teleportEntityToDimension(player, player.posX + x,chunk.getHeight(new BlockPos(player.posX, 64, player.posX)) + 1, player.posZ + z, 0);
                }
            break;
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, LinkedList<String> args, @Nullable BlockPos targetPos) {
        return Collections.emptyList();
    }
}

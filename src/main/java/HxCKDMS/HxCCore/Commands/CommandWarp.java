package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.api.Utils.Teleporter;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@SuppressWarnings({"unchecked", "unused"})
@HxCCommand(defaultPermission = 0, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandWarp implements ISubCommand {
    private static CommandWarp instance = new CommandWarp();

    @Override
    public String getCommandName() {
        return "Warp";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, -1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws WrongUsageException {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get("Warp"), player);
            if (CanSend) {
                int oldx = (int)player.posX, oldy = (int)player.posY, oldz = (int)player.posZ, olddim = player.dimension;
                String UUID = player.getUniqueID().toString();
                File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");

                String wName = args.length == 1 ? "default" : args[1];
                NBTTagCompound warpDir = NBTFileIO.getNbtTagCompound(HxCCore.CustomWorldData, "warp");
                if(warpDir.getKeySet().isEmpty()) throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.noWarps"));
                if(!warpDir.hasKey(wName)) throw new WrongUsageException("The warp named: '" + wName + "' does not exist.");
                NBTTagCompound warp = warpDir.getCompoundTag(wName);
                if(player.dimension != warp.getInteger("dim")) {
                    Teleporter.transferPlayerToDimension(player, warp.getInteger("dim"), new BlockPos(warp.getInteger("x"), warp.getInteger("y"), warp.getInteger("z")));
                    player.addChatMessage(new ChatComponentText("You have teleported to " + wName + "."));
                } else {
                    player.playerNetServerHandler.setPlayerLocation(warp.getInteger("x"), warp.getInteger("y"), warp.getInteger("z"), player.rotationYaw, player.rotationPitch);
                    player.addChatMessage(new ChatComponentText("You have teleported to " + wName + "."));
                }
                NBTFileIO.setIntArray(CustomPlayerData, "back", new int[]{oldx, oldy, oldz, olddim});
            } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 2) {
            NBTTagCompound warp = NBTFileIO.getNbtTagCompound(HxCCore.CustomWorldData, "warp");
            return new LinkedList<>((Set<String>) warp.getKeySet());
        }
        return null;
    }
}

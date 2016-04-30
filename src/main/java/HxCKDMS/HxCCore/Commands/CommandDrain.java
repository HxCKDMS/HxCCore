package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.ArrayList;
import java.util.List;

@HxCCommand(defaultPermission = 4, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandDrain implements ISubCommand {
    public static CommandDrain instance = new CommandDrain();
    @Override
    public String getCommandName() {
        return "Drain";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, -1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get("Drain"), player);
            int range = 10; Block block = Blocks.water;
            if (args.length >= 2) { range = Integer.parseInt(args[1]);}
            if (args.length == 3 && FluidRegistry.getRegisteredFluids().keySet().contains(args[2]))
                block = FluidRegistry.getFluid(args[2]).getBlock();
            int x = (int)Math.round(player.posX), y = (int)Math.round(player.posY), z = (int)Math.round(player.posZ);
            if (CanSend) {
                for (int i = x - range; i < x + range; i++)
                    for (int j = y - range; j < y + range; j++)
                        for (int k = z - range; k < z + range; k++)
                            if (args.length == 3 ? player.worldObj.getBlock(i, j, k) == block : (player.worldObj.getBlock(i, j, k) instanceof IFluidBlock || player.worldObj.getBlock(i, j, k).getMaterial().isLiquid()))
                                player.worldObj.setBlockToAir(i, j, k);
            } else throw new WrongUsageException(StatCollector.translateToLocal("command.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("command.exception.playersonly"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 3)
            return new ArrayList<>(FluidRegistry.getRegisteredFluids().keySet());
        return null;
    }
}

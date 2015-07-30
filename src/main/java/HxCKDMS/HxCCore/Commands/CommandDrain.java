package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.ISubCommand;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.List;

public class CommandDrain implements ISubCommand {
    public static CommandDrain instance = new CommandDrain();

    @Override
    public String getCommandName() {
        return "drain";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(Configurations.commands.get("Drain"), player);
            int range = 10; String block = "minecraft:water";
            if (args.length >= 2) { range = Integer.parseInt(args[1]);}
            if (args.length == 3) { block = args[2];}
            int x = (int)Math.round(player.posX), y = (int)Math.round(player.posY), z = (int)Math.round(player.posZ);
            if (CanSend)
                for (int i = x-range; i < x+range; i++)
                    for (int j = y-range; j < y+range; j++)
                        for (int k = z-range; k < z+range; k++)
                            if (args.length == 3 ? player.worldObj.getBlock(i, j, k) == Block.getBlockFromName(block) : (player.worldObj.getBlock(i, j, k) instanceof IFluidBlock || player.worldObj.getBlock(i , j, k).getMaterial().isLiquid()))
                                player.worldObj.setBlockToAir(i, j, k);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 2){
            return net.minecraft.command.CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}

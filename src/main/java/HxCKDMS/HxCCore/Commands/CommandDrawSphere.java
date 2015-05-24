package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Utils.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

public class CommandDrawSphere implements ISubCommand{
    public static CommandDrawSphere instance = new CommandDrawSphere();

    private String color = EnumChatFormatting.GREEN.toString();

    @Override
    public String getCommandName() {
        return "drawSphere";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP){

            EntityPlayerMP player = (EntityPlayerMP) sender;

            int x = (int)CommandBase.func_110666_a(sender, player.posX, args[1]);
            int y = (int)CommandBase.func_110665_a(sender, player.posY, args[2], 0, 0);
            int z = (int)CommandBase.func_110666_a(sender, player.posZ, args[3]);
            int radius = Integer.parseInt(args[4]);
            String unlocalizedName = args[5];
            boolean hollow = Boolean.parseBoolean(args[6]);
            Block block = (Block) Block.blockRegistry.getObject(unlocalizedName);

            WorldHelper.drawSphere(player.worldObj, x, y, z, block, radius, hollow, 0.05);
            sender.addChatMessage(new ChatComponentText(String.format(color + "Successfully drew a %1$s sphere at x: %2$d, y: %3$d, z: %4$d " + color + "with a radius of %5$d with block: %6$s.", hollow ? "hollow" : "filled", x, y, z, radius, unlocalizedName)));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        ArrayList<String> booleans = new ArrayList<>();
        booleans.add(Boolean.FALSE.toString()); booleans.add(Boolean.TRUE.toString());

        if(args.length == 2 || args.length == 3 || args.length == 4) return Arrays.asList(new String[]{"~"});
        else if(args.length == 5) return Arrays.asList(new String[]{Integer.toString(8)});
        else if(args.length == 6) return CommandBase.getListOfStringsFromIterableMatchingLastWord(args, Block.blockRegistry.getKeys());
        else if(args.length == 7) return CommandBase.getListOfStringsFromIterableMatchingLastWord(args, booleans);
        return null;
    }
}

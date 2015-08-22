package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.api.Utils.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collections;
import java.util.List;

@HxCCommand(defaultPermission = 5, mainCommand = CommandMain.class)
public class CommandDrawSphere implements ISubCommand {
    //TODO: Draw 3dShape and Draw Shape commands
    //Pyramid, Cube, oval, rectangle :D MAYBE player
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
            boolean CanSend = PermissionsHandler.canUseCommand(Configurations.commands.get("DrawSphere"), player);

            if (CanSend) {
                int x = (int) CommandMain.clamp_coord(sender, player.posX, args[1]);
                int y = (int) CommandMain.clamp_double(sender, player.posY, args[2], 0, 0);
                int z = (int) CommandMain.clamp_coord(sender, player.posZ, args[3]);
                int radius = Integer.parseInt(args[4]);
                String unlocalizedName = args[5];
                boolean hollow = Boolean.parseBoolean(args[6]);
                Block block = (Block) Block.blockRegistry.getObject(unlocalizedName);
                double updateAmount = args.length == 8 ? Double.parseDouble(args[7]) : 0.05D;

                long cNano = System.nanoTime();
                WorldHelper.drawSphere(player.worldObj, x, y, z, block, radius, hollow, updateAmount);
                sender.addChatMessage(new ChatComponentText(String.format(color + "Successfully drew a %1$s sphere at x: %2$d, y: %3$d, z: %4$d " + color + "with a radius of %5$d with block: %6$s in %7$d seconds.", hollow ? "hollow" : "filled", x, y, z, radius, unlocalizedName, (System.nanoTime() - cNano) / 1000000000)));
            } else sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
        }
    }
    @Override
    @SuppressWarnings("unchecked")
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        String[] booleans = new String[]{Boolean.FALSE.toString(), Boolean.TRUE.toString()};

        if(args.length == 2 || args.length == 3 || args.length == 4) return Collections.singletonList("~");
        else if(args.length == 5) return Collections.singletonList(Integer.toString(8));
        else if(args.length == 6) return CommandMain.getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys().toString().replace('[', ' ').replace(']', ' ').trim().split(", "));
        else if(args.length == 7) return CommandMain.getListOfStringsMatchingLastWord(args, booleans);
        return null;
    }
}

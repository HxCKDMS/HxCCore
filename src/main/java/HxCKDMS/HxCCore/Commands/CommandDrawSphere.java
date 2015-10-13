package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.api.Utils.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.Collections;
import java.util.List;

@HxCCommand(defaultPermission = 5, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandDrawSphere implements ISubCommand {
    //TODO: Draw 3dShape and Draw Shape commands
    //Pyramid, Cube, oval, rectangle :D MAYBE player
    public static CommandDrawSphere instance = new CommandDrawSphere();

    private String color = EnumChatFormatting.GREEN.toString();

    @Override
    public String getCommandName() {
        return "DrawSphere";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{8, -1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get("DrawSphere"), player);

            if (CanSend) {
                int x = (int) (args[1].isEmpty() ? CommandsHandler.clamp_coord(sender, player.posX, "~") : CommandsHandler.clamp_coord(sender, player.posX, args[1]));
                int y = (int) (args[2].isEmpty() ? CommandsHandler.clamp_double(sender, player.posY, "~", 0, 0) : CommandsHandler.clamp_double(sender, player.posY, args[2], 0, 0));
                int z = (int) (args[3].isEmpty() ? CommandsHandler.clamp_coord(sender, player.posZ, "~") : CommandsHandler.clamp_coord(sender, player.posZ, args[3]));
                int radius = args[4].isEmpty() ? 8 : Integer.parseInt(args[4]);
                String unlocalizedName = args[5].isEmpty() ? "minecraft:stone" : args[5];
                boolean hollow = !args[6].isEmpty() && Boolean.parseBoolean(args[6]);

                Block block = (Block) Block.blockRegistry.getObject(unlocalizedName);
                double updateAmount = args.length == 8 ? Double.parseDouble(args[7]) : 0.05D;

                long cNano = System.nanoTime();
                WorldHelper.draw3DEllipsoid(player.worldObj, x, y, z, block, radius, hollow, updateAmount, 0, 2);
                ChatComponentText chatComponentText = new ChatComponentText(String.format("Successfully drew a %1$s sphere at x: %2$d, y: %3$d, z: %4$d with a radius of %5$d with block: %6$s in %7$d seconds.", hollow ? "hollow" : "filled", x, y, z, radius, unlocalizedName, (System.nanoTime() - cNano) / 1000000000));
                chatComponentText.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN));
                sender.addChatMessage(chatComponentText);

            } else throw new WrongUsageException(StatCollector.translateToLocal("command.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("commands." + getCommandName() + ".usage"));
    }
    @Override
    @SuppressWarnings("unchecked")
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        String[] booleans = new String[]{Boolean.FALSE.toString(), Boolean.TRUE.toString()};

        if(args.length == 2 || args.length == 3 || args.length == 4) return Collections.singletonList("~");
        else if(args.length == 5) return Collections.singletonList(Integer.toString(8));
        else if(args.length == 6) return CommandsHandler.getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys().toString().replace('[', ' ').replace(']', ' ').trim().split(", "));
        else if(args.length == 7) return CommandsHandler.getListOfStringsMatchingLastWord(args, booleans);
        return null;
    }
}

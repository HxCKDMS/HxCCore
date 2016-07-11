package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.api.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.api.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.Utils.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
//DISABLED UNTIL I FINISH TWEAKING!!!
@HxCCommand(defaultPermission = 4, mainCommand = CommandsHandler.class, isEnabled = false)
public class CommandDraw implements ISubCommand {
    @Override
    public String getCommandName() {
        return "Draw";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{2, -1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get(getCommandName()), player);
            String[] args2;
            switch (args.length) {
                case 3 : args2 = new String[]{args[2], "~", "~", "8", "minecraft:glass", "true", "0.005"};
                    break;
                case 4 : args2 = new String[]{args[2], args[3], "~", "8", "minecraft:glass", "true", "0.005"};
                    break;
                case 5 : args2 = new String[]{args[2], args[3], args[4], "8", "minecraft:glass", "true", "0.005"};
                    break;
                case 6 : args2 = new String[]{args[2], args[3], args[4], args[5], "minecraft:glass", "true", "0.005"};
                    break;
                case 7 : args2 = new String[]{args[2], args[3], args[4], args[5], args[6], "true", "0.005"};
                    break;
                case 8 : args2 = new String[]{args[2], args[3], args[4], args[5], args[6], args[7], "0.005"};
                    break;
                case 9 : args2 = new String[]{args[2], args[3], args[4], args[5], args[6], args[7], args[8]};
                    break;
                case 10 : args2 = new String[]{args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9]};
                    break;
                default : args2 = new String[]{"~", "~", "~", "8", "minecraft:glass", "true", "0.01"};
                    break;
            }

            if (CanSend) {
                switch (args[1].toLowerCase()) {
                    case ("2dellipsoid"):
                        draw2DEllipsoid(player, args2);
                        break;
//                    case ("3dellipsoid"):
//                    draw3DElllipsoid(player, args2);
//                        break;
                    case ("2dsquircle"):
                        draw2DSquircle(player, args2);
                        break;
                    case ("3dsquircle"):
                        draw3DSquircle(player, args2);
                        break;
                    case ("circle"):
                        drawCircle(player, args2);
                        break;
                    case ("sphere"):
                        drawSphere(player, args2);
                        break;
                    default:
                        break;
                }
            } else throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands.exception.permission"));
        } else throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands." + getCommandName() + ".usage"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        String[] booleans = new String[]{Boolean.FALSE.toString(), Boolean.TRUE.toString()};

        if (args.length == 2) return Arrays.asList("Sphere", "Circle", "2DEllipsoid", "2DSquircle", "3DSquircle");
        else if (args.length == 3 || args.length == 4 || args.length == 5) return Collections.singletonList("~");
        else if (args.length == 6) return Collections.singletonList(Integer.toString(8));
        else if (args.length == 7)
            return CommandsHandler.getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys().toString().replace('[', ' ').replace(']', ' ').trim().split(", "));
        else if (args.length == 8) return CommandsHandler.getListOfStringsMatchingLastWord(args, booleans);
        return null;
    }

    public void draw2DEllipsoid(EntityPlayerMP player, String[] args) {
        int x = (int) CommandsHandler.func_110666_a(player, player.posX, args[0]);
        int y = (int) CommandsHandler.func_110665_a(player, player.posY, args[1], 0, 0);
        int z = (int) CommandsHandler.func_110666_a(player, player.posZ, args[2]);
        int radius = Integer.parseInt(args[3]);
        String unlocalizedName = args[4];
        boolean hollow = Boolean.parseBoolean(args[5]);
        Block block = (Block) Block.blockRegistry.getObject(unlocalizedName);
        double n = Double.parseDouble(args[6]);
        double updateAmount = args.length >= 8 ? Double.parseDouble(args[7]) : 0.005D;

        System.out.println(updateAmount);
        long cNano = System.nanoTime();
        WorldHelper.draw2DEllipsoid(player.worldObj, x, y, z, block, radius, hollow, updateAmount, 0, n);
        ChatComponentText chatComponentText = new ChatComponentText(String.format("Successfully drew a %1$s 2D Ellipsoid at x: %2$d, y: %3$d, z: %4$d with a radius of %5$d with block: %6$s in %7$d seconds.", hollow ? "hollow" : "filled", x, y, z, radius, unlocalizedName, (System.nanoTime() - cNano) / 1000000000));
        chatComponentText.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN));
        player.addChatMessage(chatComponentText);
    }

    public void draw2DSquircle(EntityPlayerMP player, String[] args) {
        int x = (int) CommandsHandler.func_110666_a(player, player.posX, args[0]);
        int y = (int) CommandsHandler.func_110665_a(player, player.posY, args[1], 0, 0);
        int z = (int) CommandsHandler.func_110666_a(player, player.posZ, args[2]);
        int radius = Integer.parseInt(args[3]);
        String unlocalizedName = args[4];
        boolean hollow = Boolean.parseBoolean(args[5]);
        Block block = (Block) Block.blockRegistry.getObject(unlocalizedName);
        double updateAmount = args.length == 7 ? Double.parseDouble(args[6]) : 0.05D;

        long cNano = System.nanoTime();
        WorldHelper.draw2DEllipsoid(player.worldObj, x, y, z, block, radius, hollow, updateAmount, 0, 4);
        ChatComponentText chatComponentText = new ChatComponentText(String.format("Successfully drew a %1$s squircle at x: %2$d, y: %3$d, z: %4$d with a radius of %5$d with block: %6$s in %7$d seconds.", hollow ? "hollow" : "filled", x, y, z, radius, unlocalizedName, (System.nanoTime() - cNano) / 1000000000));
        chatComponentText.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN));
        player.addChatMessage(chatComponentText);
    }

    public void draw3DSquircle(EntityPlayerMP player, String[] args) {
        int x = (int) CommandsHandler.func_110666_a(player, player.posX, args[0]);
        int y = (int) CommandsHandler.func_110665_a(player, player.posY, args[1], 0, 0);
        int z = (int) CommandsHandler.func_110666_a(player, player.posZ, args[2]);
        int radius = Integer.parseInt(args[3]);
        String unlocalizedName = args[4];
        boolean hollow = Boolean.parseBoolean(args[5]);
        Block block = (Block) Block.blockRegistry.getObject(unlocalizedName);
        double updateAmount = args.length == 7 ? Double.parseDouble(args[6]) : 0.05D;

        long cNano = System.nanoTime();
        WorldHelper.draw3DEllipsoid(player.worldObj, x, y, z, block, radius, hollow, updateAmount, 0, 4);
        ChatComponentText chatComponentText = new ChatComponentText(String.format("Successfully drew a %1$s 3D squircle at x: %2$d, y: %3$d, z: %4$d with a radius of %5$d with block: %6$s in %7$d seconds.", hollow ? "hollow" : "filled", x, y, z, radius, unlocalizedName, (System.nanoTime() - cNano) / 1000000000));
        chatComponentText.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN));
        player.addChatMessage(chatComponentText);
    }


    public void drawCircle(EntityPlayerMP player, String[] args) {
        int x = (int) CommandsHandler.func_110666_a(player, player.posX, args[0]);
        int y = (int) CommandsHandler.func_110665_a(player, player.posY, args[1], 0, 0);
        int z = (int) CommandsHandler.func_110666_a(player, player.posZ, args[2]);
        int radius = Integer.parseInt(args[3]);
        String unlocalizedName = args[4];
        boolean hollow = Boolean.parseBoolean(args[5]);
        Block block = (Block) Block.blockRegistry.getObject(unlocalizedName);
        double updateAmount = args.length == 7 ? Double.parseDouble(args[6]) : 0.005D;

        long cNano = System.nanoTime();
        WorldHelper.draw2DEllipsoid(player.worldObj, x, y, z, block, radius, hollow, updateAmount, 0, 2);
        ChatComponentText chatComponentText = new ChatComponentText(String.format("Successfully drew a %1$s circle at x: %2$d, y: %3$d, z: %4$d with a radius of %5$d with block: %6$s in %7$d seconds.", hollow ? "hollow" : "filled", x, y, z, radius, unlocalizedName, (System.nanoTime() - cNano) / 1000000000));
        chatComponentText.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN));
        player.addChatMessage(chatComponentText);
    }
//clamp_coord & clamp_double = 66 & 65

    public void drawSphere(EntityPlayerMP player, String[] args) {
        int x = (int)CommandsHandler.func_110666_a(player, player.posX, args[0]);
        int y = (int)CommandsHandler.func_110665_a(player, player.posY, args[1], 0, 0);
        int z = (int)CommandsHandler.func_110666_a(player, player.posZ, args[2]);
        int radius = args[3].isEmpty() ? 8 : Integer.parseInt(args[3]);
        String unlocalizedName = args[4].isEmpty() ? "minecraft:stone" : args[4];
        boolean hollow = !args[5].isEmpty() && Boolean.parseBoolean(args[5]);

        Block block = (Block) Block.blockRegistry.getObject(unlocalizedName);
        double updateAmount = args.length == 7 ? Double.parseDouble(args[6]) : 0.005D;

        long cNano = System.nanoTime();
        WorldHelper.draw3DEllipsoid(player.worldObj, x, y, z, block, radius, hollow, updateAmount, 0, 2);
        ChatComponentText chatComponentText = new ChatComponentText(String.format("Successfully drew a %1$s sphere at x: %2$d, y: %3$d, z: %4$d with a radius of %5$d with block: %6$s in %7$d seconds.", hollow ? "hollow" : "filled", x, y, z, radius, unlocalizedName, (System.nanoTime() - cNano) / 1000000000));
        chatComponentText.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN));
        player.addChatMessage(chatComponentText);
    }
}

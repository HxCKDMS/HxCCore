package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.NickHandler;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.DimensionManager;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@HxCCommand(defaultPermission = 4, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandClientInfo implements ISubCommand {
    private EnumChatFormatting defaultColor = EnumChatFormatting.BLUE;

    public static CommandClientInfo instance = new CommandClientInfo();

    @Override
    public String getCommandName() {
        return "ClientInfo";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, 1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) {
        if (isPlayer) {
            if (PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get("ClientInfo"), (EntityPlayerMP) sender)) {
                EntityPlayerMP player = args.length > 1 ? CommandBase.getPlayer(sender, args[1]) : (EntityPlayerMP) sender;
                getClientInfo(sender, player);
            } else throw new WrongUsageException(StatCollector.translateToLocal("command.exception.permission"));
        } else {
            getClientInfo(sender, CommandBase.getPlayer(sender, args[1]));
        }
    }

    private void getClientInfo(ICommandSender sender, EntityPlayerMP player) {
        sender.addChatMessage(new ChatComponentText(defaultColor + String.format("Player: %1$s.", getPlayerNameStyled(player))));
        sender.addChatMessage(new ChatComponentText(defaultColor + String.format("NickName: %1$s.", NickHandler.getMessageHeader(player) + defaultColor)));
        sender.addChatMessage(new ChatComponentText(defaultColor + String.format("Location: X: %1$s, Y: %2$s, Z: %3$s.", getCoordStyled(player.posX), getCoordStyled(player.posY), getCoordStyled(player.posZ))));
        sender.addChatMessage(new ChatComponentText(defaultColor + String.format("Dimension: %1$s.", getDimensionStyled(player.dimension))));
        sender.addChatMessage(new ChatComponentText(defaultColor + String.format("GameMode: %1$s.", getGameModeStyled(player))));
        sender.addChatMessage(new ChatComponentText(defaultColor + String.format("IP address: %1$s.", getIPAddressStyled(player.getPlayerIP()))));
        sender.addChatMessage(new ChatComponentText(defaultColor + String.format("Ping: %1$s.", getPingStyled(player.ping))));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    }

    private String getPingStyled(int ping){
        EnumChatFormatting PingColor = ping <= 75 ? EnumChatFormatting.GREEN : ping > 200 ? EnumChatFormatting.RED : EnumChatFormatting.GOLD;

        return PingColor.toString() + ping + defaultColor;
    }

    private String getIPAddressStyled(String IP){
        return EnumChatFormatting.AQUA + IP + defaultColor;
    }

    private String getDimensionStyled(int dimension){
        String DimName = DimensionManager.getProvider(dimension).getDimensionName();
        EnumChatFormatting DimColor = DimName.equalsIgnoreCase("the end") ? EnumChatFormatting.YELLOW : DimName.equalsIgnoreCase("nether") ? EnumChatFormatting.RED : DimName.equalsIgnoreCase("overworld") ? EnumChatFormatting.GREEN : EnumChatFormatting.WHITE;
        return DimColor + StringUtils.capitalize(DimName) + defaultColor;
    }

    private String getCoordStyled(double coord){
        return EnumChatFormatting.AQUA.toString() +(int) coord + defaultColor;
    }

    private String getPlayerNameStyled(EntityPlayerMP player){
        return EnumChatFormatting.AQUA + player.getDisplayName() + defaultColor;
    }

    private String getGameModeStyled(EntityPlayerMP player) {
        String GameMode = player.theItemInWorldManager.getGameType().getName();
        EnumChatFormatting GMColor = GameMode.equals("creative") ? EnumChatFormatting.LIGHT_PURPLE : GameMode.equals("adventure") ? EnumChatFormatting.YELLOW : EnumChatFormatting.GREEN;
        return GMColor + StringUtils.capitalize(GameMode) + defaultColor;
    }
}

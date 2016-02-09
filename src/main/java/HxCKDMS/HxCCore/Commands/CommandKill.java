package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;

import java.util.List;

@HxCCommand(defaultPermission = 5, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandKill implements ISubCommand {
    public static CommandKill instance = new CommandKill();
    //Maybe make it so people who use this command can't use on people of higher ranks????

    @Override
    public String getCommandName() {
        return "Kill";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, 1, 1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws PlayerNotFoundException, WrongUsageException {
        switch(args.length){
            case 1:
                if (isPlayer) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get("Kill"), player);
                    if (CanSend) player.attackEntityFrom(new DamageSource("command_kill." + player.worldObj.rand.nextInt(35)).setDamageBypassesArmor().setDamageAllowedInCreativeMode(), Float.MAX_VALUE);
                    else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
                } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
            break;
            case 2:
                EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
                player2.attackEntityFrom(new DamageSource("command_kill." + player2.worldObj.rand.nextInt(35)).setDamageBypassesArmor().setDamageAllowedInCreativeMode(), Float.MAX_VALUE);
            break;
            default: throw new WrongUsageException(StatCollector.translateToLocal("commands." + getCommandName().toLowerCase() + ".usage"));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 2){
            return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}

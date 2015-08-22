package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;

import java.util.List;

@HxCCommand(defaultPermission = 5, mainCommand = CommandMain.class)
public class CommandKill implements ISubCommand {
    public static CommandKill instance = new CommandKill();

    @Override
    public String getCommandName() {
        return "Kill";
    }


    @Override
    public void handleCommand(ICommandSender sender, String[] args) throws PlayerNotFoundException, WrongUsageException {
        switch(args.length){
            case 1:
                if(sender instanceof EntityPlayer){
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    boolean CanSend = PermissionsHandler.canUseCommand(Configurations.commands.get("Kill"), player);
                    if (CanSend) player.attackEntityFrom(new DamageSource("command_kill").setDamageBypassesArmor().setDamageAllowedInCreativeMode(), Float.MAX_VALUE);
                    else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
                } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
            break;
            case 2:
                EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
                player2.attackEntityFrom(new DamageSource("command_kill").setDamageBypassesArmor().setDamageAllowedInCreativeMode(), Float.MAX_VALUE);
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

package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraftforge.fml.common.Loader;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentText;

import java.util.List;

@SuppressWarnings({"unchecked", "unused"})
@HxCCommand(defaultPermission = 0, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandUpdateCheck implements ISubCommand {
    public static CommandUpdateCheck instance = new CommandUpdateCheck();
    @Override
    public String getCommandName() {
        return "UpdateCheck";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, 0, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws WrongUsageException, PlayerNotFoundException{
        HxCCore.versionCheck();
        Loader.instance().getModList().forEach(m -> {
            if (HxCCore.knownMods.contains(m.getModId())) {
                String s = HxCCore.getNewVer(m.getModId(), m.getVersion());
                if (!s.isEmpty())
                    sender.addChatMessage(new ChatComponentText("A New version of " + m.getModId() + " has been found please update ASAP New Version Found = " + s));
            }
        });
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}

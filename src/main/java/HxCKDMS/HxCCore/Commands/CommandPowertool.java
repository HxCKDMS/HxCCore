package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.StatCollector;

import java.util.List;

@HxCCommand(defaultPermission = 5, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandPowertool implements ISubCommand {
    public static CommandPowertool instance = new CommandPowertool();

    @Override
    public String getCommandName() {
        return "Powertool";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, -1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws WrongUsageException {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get(getCommandName()), player);
            if (CanSend && player.getHeldItem() != null) {
                String c = "";
                for (int i = 1; i < args.length; i++)
                    c = c + " " + args[i];
                c = c.trim();
                if (player.getHeldItem().hasTagCompound()) {
                    NBTTagCompound tag = player.getHeldItem().getTagCompound();
                    tag.setString("powertool", c);
                    if (tag.hasKey("display")) {
                        NBTTagList l = tag.getCompoundTag("display").getTagList("Lore", 8);
                        l.appendTag(new NBTTagString(c));
                    } else {
                        NBTTagList l = new NBTTagList();
                        l.appendTag(new NBTTagString(c));
                    }
                    player.getHeldItem().setTagCompound(tag);
                } else {
                    NBTTagCompound tag = new NBTTagCompound();
                    tag.setString("powertool", c);
                    player.getHeldItem().setTagCompound(tag);
                }
            } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
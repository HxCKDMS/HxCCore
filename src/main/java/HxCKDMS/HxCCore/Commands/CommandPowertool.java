package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.api.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.api.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

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
                if (!c.startsWith("/")) c = "/" + c;
                if (player.getHeldItem().hasTagCompound()) {
                    NBTTagCompound tag = player.getHeldItem().getTagCompound();
                    tag.setString("powertool", c);
                    if (!c.isEmpty() && !c.equalsIgnoreCase("/")) {
                        if (tag.hasKey("display")) {
                            NBTTagList l = tag.getCompoundTag("display").getTagList("Lore", 8);
                            l.appendTag(new NBTTagString(c));
                        } else {
                            NBTTagCompound d = new NBTTagCompound();
                            NBTTagList l = new NBTTagList();
                            l.appendTag(new NBTTagString(c));
                            d.setTag("Lore", l);
                            tag.setTag("display", d);
                        }
                    } else {
                        if (tag.hasKey("display")) {
                            NBTTagList l = tag.getCompoundTag("display").getTagList("Lore", 8);
                            for (int li = 0; li < l.tagCount(); li++) {
                                if (l.getStringTagAt(li).startsWith("HxC") || l.getStringTagAt(li).startsWith("/"))
                                    l.removeTag(li);
                            }
                        }
                        tag.removeTag("powertool");
                    }
                    player.getHeldItem().setTagCompound(tag);
                } else {
                    NBTTagCompound tag = new NBTTagCompound();
                    tag.setString("powertool", c);
                    NBTTagCompound d = new NBTTagCompound();
                    NBTTagList l = new NBTTagList();
                    l.appendTag(new NBTTagString(c));
                    d.setTag("Lore", l);
                    tag.setTag("display", d);
                    player.getHeldItem().setTagCompound(tag);
                }
            } else throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands.exception.permission"));
        } else throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands.exception.playersonly"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
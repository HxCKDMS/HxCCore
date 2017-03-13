package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.configs.KitConfiguration;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.Kit;
import hxckdms.hxccore.utilities.PermissionHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandKit extends AbstractSubCommand<CommandHxC> {
    @Override
    public String getCommandName() {
        return "kit";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (args.size() < 1)
            throw new TranslatedCommandException(sender, "commands.hxc.kit.usage");
        if (!(sender instanceof EntityPlayerMP))
            throw new TranslatedCommandException(sender, "commands.exception.playersOnly");
        EntityPlayerMP player = (EntityPlayerMP) sender;
        String kitName = args.get(0).toLowerCase();
        int usedTimes = 0;
        int delay = 0;

        NBTTagCompound usedKits = HxCPlayerInfoHandler.getTagCompound(player, "kits", new NBTTagCompound());
        NBTTagCompound comp;

        if (usedKits.func_150296_c().contains(kitName)) {
            comp = usedKits.getCompoundTag(kitName);
            delay = (int)((System.currentTimeMillis() - comp.getLong("lastTimeUsed"))/1000);
            usedTimes = comp.getInteger("usedTimes");
        } else {
            comp = new NBTTagCompound();
        }

        Kit kit = KitConfiguration.kits.get(KitConfiguration.kits.keySet().stream().filter(key -> key.equalsIgnoreCase(kitName)).findFirst().orElseThrow(() -> new TranslatedCommandException(sender, "commands.kit.error.noSuchKit")));
        if (kit.permissionLevel > PermissionHandler.getPermissionLevel(sender) || PermissionHandler.getPermissionLevel(sender) == 0)
            throw new TranslatedCommandException(sender, "commands.generic.permission");
        if (usedTimes < kit.maxUses && delay >= kit.delayBetweenUses) {
            kit.getKitItems().forEach(player.inventory::addItemStackToInventory);
            comp.setLong("lastTimeUsed", System.currentTimeMillis());
            comp.setInteger("usedTimes", usedTimes + 1);
            usedKits.setTag(kitName, comp);
            HxCPlayerInfoHandler.setTagCompound(player, "kits", usedKits);
        }
        sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.kit.successful", kitName).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return args.size() == 1 ? new ArrayList<>(KitConfiguration.kits.keySet()) : Collections.emptyList();
    }
}

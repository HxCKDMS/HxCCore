package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.configs.KitConfiguration;
import hxckdms.hxccore.utilities.Kit;
import hxckdms.hxccore.utilities.PermissionHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandKit extends AbstractSubCommand<CommandHxC> {
    @Override
    public String getName() {
        return "kit";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (args.size() < 1) throw new TranslatedCommandException(sender, "commands.hxc.kit.usage");
        if (!(sender instanceof EntityPlayerMP)) throw new TranslatedCommandException(sender, "commands.exception.playersOnly");
        EntityPlayerMP player = (EntityPlayerMP) sender;
        String kitName = args.get(0);

        Kit kit = KitConfiguration.kits.get(KitConfiguration.kits.keySet().stream().filter(key -> key.equalsIgnoreCase(kitName)).findFirst().orElseThrow(() -> new TranslatedCommandException(sender, "commands.kit.error.noSuchKit")));
        if (kit.permissionLevel > PermissionHandler.getPermissionLevel(sender)) throw new TranslatedCommandException(sender, "commands.generic.permission");
        kit.getKitItems().forEach(player.inventory::addItemStackToInventory);
        sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.kit.successful", kitName).setStyle(new Style().setColor(TextFormatting.BLUE)));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return args.size() == 1 ? new ArrayList<>(KitConfiguration.kits.keySet()) : Collections.emptyList();
    }
}

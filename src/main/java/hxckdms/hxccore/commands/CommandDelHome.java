package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.configs.HomesConfigStorage;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandDelHome extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 1;
    }

    @Override
    public String getName() {
        return "delHome";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP))
            throw new TranslatedCommandException(sender, "commands.exception.playersOnly");
        EntityPlayerMP player = (EntityPlayerMP) sender;
        String name = args.size() == 0 ? "default" : args.get(0);
        if (!Configuration.useTextStorageofHomes) {
            NBTTagCompound homes = HxCPlayerInfoHandler.getTagCompound(player, "homes", new NBTTagCompound());
            if (!homes.hasKey(name))
                throw new TranslatedCommandException(sender, "commands.error.invalid.home", name);
            homes.removeTag(name);
            HxCPlayerInfoHandler.setTagCompound(player, "warps", homes);
        } else {
            String user = Configuration.storeTextHomesUsingName ? player.getDisplayNameString() : player.getUniqueID().toString();
            if (HomesConfigStorage.homes.containsKey(user)) {
                if (HomesConfigStorage.homes.get(user).homes.containsKey(name)) {
                    HomesConfigStorage.homes.get(user).homes.remove(name);
                    GlobalVariables.alternateHomesConfig.initConfiguration();
                } else {
                    throw new TranslatedCommandException(sender, "commands.error.invalid.home", name);
                }
            } else {
                throw new TranslatedCommandException(sender, "commands.error.invalid.home", name);
            }
        }
        sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.home.removed", name).setStyle(new Style().setColor(TextFormatting.DARK_RED)));
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, LinkedList<String> args, @Nullable BlockPos targetPos) {
        return (sender instanceof EntityPlayerMP && args.size() == 1) ? new ArrayList<>(HxCPlayerInfoHandler.getTagCompound((EntityPlayer) sender, "homes", new NBTTagCompound()).getKeySet()) : Collections.emptyList();
    }
}

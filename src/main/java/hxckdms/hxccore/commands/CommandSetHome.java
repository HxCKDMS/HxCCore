package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.configs.FakePlayerData;
import hxckdms.hxccore.configs.HomesConfigStorage;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.registry.CommandRegistry;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.PermissionHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandSetHome extends AbstractSubCommand<CommandHxC> {
    private static DecimalFormat posFormat = new DecimalFormat("#.###");

    {
        permissionLevel = 1;
    }


    @Override
    public String getName() {
        return "setHome";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP)) throw new TranslatedCommandException(sender, "commands.exception.playersOnly");
        EntityPlayerMP player = (EntityPlayerMP) sender;
        String name = args.size() == 0 ? "default" : args.get(0);
        if (!Configuration.useTextStorageofHomes) {
            NBTTagCompound homes = HxCPlayerInfoHandler.getTagCompound(player, "homes", new NBTTagCompound());

            if (!homes.hasKey(name) && !(CommandRegistry.CommandConfig.commandPermissions.get(PermissionHandler.getPermissionLevel(sender)).homeAmount == -1) && (homes.getKeySet().size() + 1 > CommandRegistry.CommandConfig.commandPermissions.get(PermissionHandler.getPermissionLevel(sender)).homeAmount))
                throw new TranslatedCommandException(sender, "commands.error.outOfHomes");

            NBTTagCompound home = new NBTTagCompound();

            home.setDouble("x", player.posX);
            home.setDouble("y", player.posY);
            home.setDouble("z", player.posZ);
            home.setInteger("dimension", player.dimension);

            homes.setTag(name, home);
            HxCPlayerInfoHandler.setTagCompound(player, "homes", homes);
        } else {
            String user = Configuration.storeTextHomesUsingName ? player.getDisplayNameString() : player.getUniqueID().toString();
            if (HomesConfigStorage.homes.containsKey(user)) {
                FakePlayerData pdata = HomesConfigStorage.homes.get(user);
                if (pdata.homes.containsKey(name)) {
                    pdata.homes.replace(name, new FakePlayerData.Warp(player.posX, player.posY, player.posZ, player.dimension));
                } else {
                    if ((CommandRegistry.CommandConfig.commandPermissions.get(PermissionHandler.getPermissionLevel(sender)).homeAmount == -1) || !(pdata.homes.size() + 1 > CommandRegistry.CommandConfig.commandPermissions.get(PermissionHandler.getPermissionLevel(sender)).homeAmount)) {
                        pdata.homes.put(name, new FakePlayerData.Warp(player.posX, player.posY, player.posZ, player.dimension));
                    } else {
                        throw new TranslatedCommandException(sender, "commands.error.outOfHomes");
                    }
                }
            } else {
                FakePlayerData data = new FakePlayerData();
                data.homes.put(name, new FakePlayerData.Warp(player.posX, player.posY, player.posZ, player.dimension));
                HomesConfigStorage.homes.put(user, data);
            }
            GlobalVariables.alternateHomesConfig.initConfiguration();
        }
        sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.home.set", name, posFormat.format(player.posX), posFormat.format(player.posY), posFormat.format(player.posZ), player.dimension).setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)));
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, LinkedList<String> args, @Nullable BlockPos targetPos) {
        return Collections.emptyList();
    }
}

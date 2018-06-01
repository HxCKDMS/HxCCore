package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.configs.FakePlayerData;
import hxckdms.hxccore.configs.HomesConfigStorage;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import hxckdms.hxccore.utilities.TeleportHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandHome extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 1;
    }

    @Override
    public String getName() {
        return "home";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, LinkedList<String> args) throws CommandException {
        EntityPlayerMP player = (EntityPlayerMP) sender;
        String name = args.size() == 0 ? "default" : args.get(0);
        if (!Configuration.useTextStorageofHomes) {
            NBTTagCompound homes = HxCPlayerInfoHandler.getTagCompound(player, "homes", new NBTTagCompound());

            if (!homes.getKeySet().contains(name))
                throw new TranslatedCommandException(sender, "commands.error.invalid.home", name);
            NBTTagCompound home = homes.getCompoundTag(name);

            System.out.println(DimensionManager.getWorld(home.getInteger("dimension")).getBlockState(new BlockPos(home.getDouble("x"), home.getDouble("y") + 1, home.getDouble("z"))).getBlock() == Blocks.AIR);

            if (DimensionManager.getWorld(home.getInteger("dimension")).getBlockState(new BlockPos(home.getDouble("x"), home.getDouble("y") + 1, home.getDouble("z"))).getBlock() != Blocks.AIR && !player.capabilities.isCreativeMode)
                throw new TranslatedCommandException(sender, "commands.error.teleport.nonAir");

            TeleportHelper.teleportEntityToDimension(player, home.getDouble("x"), home.getDouble("y"), home.getDouble("z"), home.getInteger("dimension"));
        } else {
            String user = Configuration.storeTextHomesUsingName ? player.getDisplayNameString() : player.getUniqueID().toString();
            if (HomesConfigStorage.homes.containsKey(user)) {
                if (HomesConfigStorage.homes.get(user).homes.containsKey(name)) {
                    FakePlayerData.Warp home = HomesConfigStorage.homes.get(user).homes.get(name);
                    TeleportHelper.teleportEntityToDimension(player, home.xpos, home.ypos, home.zpos, home.dimension);
                } else {
                    throw new TranslatedCommandException(sender, "commands.error.invalid.home", name);
                }
            } else {
                throw new TranslatedCommandException(sender, "commands.error.invalid.home", name);
            }
        }
        sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.home.teleport.self", name).setStyle(new Style().setColor(TextFormatting.BLUE)));
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, LinkedList<String> args, @Nullable BlockPos targetPos) {
        return (sender instanceof EntityPlayerMP && args.size() == 1) ? new ArrayList<>((Configuration.useTextStorageofHomes ?
                HomesConfigStorage.homes.get(Configuration.storeTextHomesUsingName ? ((EntityPlayerMP) sender).getDisplayName() : ((EntityPlayerMP) sender).getUniqueID().toString()).homes.keySet() :
                HxCPlayerInfoHandler.getTagCompound((EntityPlayer) sender, "homes", new NBTTagCompound()).getKeySet())) : Collections.emptyList();
    }
}

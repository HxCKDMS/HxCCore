package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractMultiCommand;
import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import hxckdms.hxccore.utilities.TeleportHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandHome extends AbstractSubCommand {
    {
        permissionLevel = 1;
    }

    @Override
    public String getCommandName() {
        return "home";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        EntityPlayerMP player = (EntityPlayerMP) sender;

        NBTTagCompound homes = HxCPlayerInfoHandler.getTagCompound(player, "homes", new NBTTagCompound());
        String name = args.size() == 0 ? "default" : args.get(0);

        if (!homes.getKeySet().contains(name)) throw new TranslatedCommandException(sender, "commands.error.invalid.home");
        NBTTagCompound home = homes.getCompoundTag(name);

        if (GlobalVariables.server.worldServerForDimension(home.getInteger("dimension")).getBlockState(new BlockPos(home.getDouble("x"), home.getDouble("y") + 1, home.getDouble("z"))).getBlock() != Blocks.AIR && !player.capabilities.isCreativeMode)
            throw new TranslatedCommandException(sender, "commands.error.teleport.nonAir");

        TeleportHelper.teleportEntityToDimension(player, home.getDouble("x"), home.getDouble("y"), home.getDouble("z"), home.getInteger("dimension"));
        sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.home.teleport.self", name).setStyle(new Style().setColor(TextFormatting.BLUE)));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return (sender instanceof EntityPlayerMP && args.size() == 1) ? new ArrayList<>(HxCPlayerInfoHandler.getTagCompound((EntityPlayer) sender, "homes", new NBTTagCompound()).getKeySet()) : Collections.emptyList();
    }

    @Override
    public Class<? extends AbstractMultiCommand> getParentCommand() {
        return CommandHxC.class;
    }
}

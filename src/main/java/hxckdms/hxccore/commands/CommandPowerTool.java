package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.libraries.GlobalVariables;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@HxCCommand
public class CommandPowerTool extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 5;
    }

    private static Predicate<String> isCommand = str -> str.startsWith("/") && GlobalVariables.server.getCommandManager().getCommands().containsKey(str.substring(1));

    @Override
    public String getName() {
        return "powerTool";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP)) throw new TranslatedCommandException(sender, "commands.exception.playersOnly");
        EntityPlayerMP player = (EntityPlayerMP) sender;
        ItemStack itemStack = player.inventory.getCurrentItem();
        if (itemStack == null) throw new TranslatedCommandException(sender, "commands.error.noItem");
        NBTTagCompound tagCompound = itemStack.getTagCompound() != null ? itemStack.getTagCompound() : new NBTTagCompound();

        if (!args.get(0).startsWith("/")) args.set(0, "/" + args.get(0));
        String commandText = args.stream().collect(Collectors.joining(" "));

        NBTTagList commands = new NBTTagList();
        boolean building = false;
        StringBuilder commandBuilder = new StringBuilder();

        for (String str : commandText.split("\\s")) {
            if (isCommand.test(str)) {
                building = true;
                if (!commandBuilder.toString().trim().isEmpty()) commands.appendTag(new NBTTagString(commandBuilder.toString().trim()));
                commandBuilder = new StringBuilder();
                commandBuilder.append(str).append(' ');
            } else if(building) commandBuilder.append(str).append(' ');
        }
        commands.appendTag(new NBTTagString(commandBuilder.toString().trim()));
        tagCompound.setTag("powerToolCommands", commands);
        itemStack.setTagCompound(tagCompound);
    }

    @Override
    public List<String> addTabCompletions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return Collections.emptyList();
    }
}

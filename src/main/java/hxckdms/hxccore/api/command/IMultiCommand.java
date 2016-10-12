package hxckdms.hxccore.api.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

interface IMultiCommand {
    void executeSubCommand(ICommandSender sender, String[] args) throws CommandException;
    void registerSubCommands(FMLPreInitializationEvent event);
}

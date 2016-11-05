package hxckdms.hxccore.api.command;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

interface IMultiCommand {
    void executeSubCommand(ICommandSender sender, String[] args) throws CommandException;
    void registerSubCommands(FMLPreInitializationEvent event);
}

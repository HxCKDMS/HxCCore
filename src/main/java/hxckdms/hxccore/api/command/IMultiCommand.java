package hxckdms.hxccore.api.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IMultiCommand {
    void executeSubCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;
    void registerSubCommands(FMLPreInitializationEvent event);
}

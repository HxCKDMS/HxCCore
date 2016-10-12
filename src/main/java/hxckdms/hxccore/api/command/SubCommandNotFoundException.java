package hxckdms.hxccore.api.command;

import net.minecraft.command.CommandException;

public class SubCommandNotFoundException extends CommandException {
    public SubCommandNotFoundException() {
        this("commands.sub.exception.notFound");
    }

    public SubCommandNotFoundException(String message, Object... args) {
        super(message, args);
    }
}

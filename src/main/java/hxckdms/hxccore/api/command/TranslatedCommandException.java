package hxckdms.hxccore.api.command;

import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class TranslatedCommandException extends CommandException {
    public TranslatedCommandException(ICommandSender sender, String message, Object... objects) {
        super(ServerTranslationHelper.getTranslation(sender, message, objects).getUnformattedText());
    }
}

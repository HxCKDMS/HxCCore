package hxckdms.hxccore.utilities;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;

import java.util.UUID;

import static hxckdms.hxccore.libraries.GlobalVariables.doesPlayerHaveMod;
import static hxckdms.hxccore.libraries.GlobalVariables.langFile;

public class ServerTranslationHelper {
    public static ChatComponentTranslation getTranslation(EntityPlayer player, String translationKey, Object... args) {
        return getTranslation(player.getUniqueID(), translationKey, args);
    }

    public static ChatComponentTranslation getTranslation(ICommandSender sender, String translationKey, Object... args) {
        if (sender instanceof EntityPlayer) return getTranslation(((EntityPlayer) sender).getUniqueID(), translationKey, args);
        else return new ChatComponentTranslation(translationKey, args);

    }

    public static ChatComponentTranslation getTranslation(UUID uuid, String translationKey, Object... args) {
        if (doesPlayerHaveMod.contains(uuid))
            return new ChatComponentTranslation(translationKey, args);

        String localized = langFile.get(translationKey);
        return new ChatComponentTranslation(localized != null && !localized.isEmpty() ? localized : translationKey, args);
    }
}

package hxckdms.hxccore.utilities;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.UUID;

import static hxckdms.hxccore.libraries.GlobalVariables.doesPlayerHaveMod;
import static hxckdms.hxccore.libraries.GlobalVariables.langFile;

public class ServerTranslationHelper {
    public static ITextComponent getTranslation(EntityPlayer player, String translationKey, Object... args) {
        return getTranslation(player.getUniqueID(), translationKey, args);
    }

    public static ITextComponent getTranslation(ICommandSender sender, String translationKey, Object... args) {
        if (sender instanceof EntityPlayer) return getTranslation(((EntityPlayer) sender).getUniqueID(), translationKey, args);
        else return new TextComponentTranslation(translationKey, args);

    }

    public static ITextComponent getTranslation(UUID uuid, String translationKey, Object... args) {
        if (doesPlayerHaveMod.contains(uuid))
            return new TextComponentTranslation(translationKey, args);
        else
            return new TextComponentString(String.format(langFile.get(translationKey) == null ? translationKey : langFile.get(translationKey), args));
    }
}

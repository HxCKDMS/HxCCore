package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandAFK extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 1;
    }

    @Override
    public String getCommandName() {
        return "AFK";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP)) throw new TranslatedCommandException(sender, "commands.exception.playersOnly");
        EntityPlayerMP player = (EntityPlayerMP) sender;

        HxCPlayerInfoHandler.setBoolean(player, "AFK", !HxCPlayerInfoHandler.getBoolean(player, "AFK"));

        if (HxCPlayerInfoHandler.getBoolean(player, "AFK")) {
            player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 5, 100, false));
            player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 5, 100, false));
            player.addPotionEffect(new PotionEffect(Potion.jump.id, 5, -100, false));
            player.capabilities.disableDamage = true;
        } else player.capabilities.disableDamage = false;

        ((List<EntityPlayerMP>)GlobalVariables.server.getConfigurationManager().playerEntityList)
                .forEach((iPlayer -> iPlayer.addChatMessage(ServerTranslationHelper.getTranslation(iPlayer, "commands.AFK." + (HxCPlayerInfoHandler.getBoolean(player, "AFK") ? "away" : "back"), player.getDisplayName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)))));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return Collections.emptyList();
    }
}

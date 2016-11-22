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
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandAFK extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 1;
    }

    @Override
    public String getName() {
        return "AFK";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP)) throw new TranslatedCommandException(sender, "commands.exception.playersOnly");
        EntityPlayerMP player = (EntityPlayerMP) sender;

        HxCPlayerInfoHandler.setBoolean(player, "AFK", !HxCPlayerInfoHandler.getBoolean(player, "AFK"));

        if (HxCPlayerInfoHandler.getBoolean(player, "AFK")) {
            player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 5, 100, false, false));
            player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 5, 100, false, false));
            player.capabilities.disableDamage = true;
        } else player.capabilities.disableDamage = false;

        GlobalVariables.server.getPlayerList().getPlayers().forEach(iPlayer -> iPlayer.sendMessage(ServerTranslationHelper.getTranslation(iPlayer, "commands.AFK." + (HxCPlayerInfoHandler.getBoolean(player, "AFK") ? "away" : "back"), player.getDisplayName()).setStyle(new Style().setColor(TextFormatting.YELLOW))));
    }

    @Override
    public List<String> addTabCompletions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return Collections.emptyList();
    }
}

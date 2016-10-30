package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


//TODO: make work for any entity.
@HxCCommand
public class CommandMakeItRain extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 5;
    }

    @Override
    public String getCommandName() {
        return "makeItRain";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                EntityPlayerMP player = (EntityPlayerMP) sender;
                spawnProjectiles(args, player);
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.makeItRain.self").setStyle(new Style().setColor(TextFormatting.RED)));
                break;
            case 1:
                if (Arrays.asList(GlobalVariables.server.getPlayerList().getAllUsernames()).contains(args.get(0))) {
                    EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));
                    spawnProjectiles(args, target);
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.makeItRain.other.sender", target.getDisplayName()).setStyle(new Style().setColor(TextFormatting.RED)));
                    target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.makeItRain.other.target", sender.getDisplayName()).setStyle(new Style().setColor(TextFormatting.RED)));

                } else if(sender instanceof EntityPlayerMP) {
                    player = (EntityPlayerMP) sender;
                    spawnProjectiles(args, player);
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.makeItRain.self").setStyle(new Style().setColor(TextFormatting.RED)));
                }
                break;
            case 2:
                EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));
                spawnProjectiles(args, target);
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.makeItRain.other.sender", target.getDisplayName()).setStyle(new Style().setColor(TextFormatting.RED)));
                target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.makeItRain.other.target", sender.getDisplayName()).setStyle(new Style().setColor(TextFormatting.RED)));
                break;
        }


    }

    private static void spawnProjectiles(LinkedList<String> args, EntityPlayerMP player) {
        boolean isKitty = args.size() >= 2 && args.get(1).equalsIgnoreCase("kitty");
        int minY = player.worldObj.getTopSolidOrLiquidBlock(player.getPosition()).getY() + 64;

        for (int x_offset = -10; x_offset <= 10; x_offset += 5) {
            for (int z_offset = -10; z_offset <= 10; z_offset += 5) {
                Entity projectile = isKitty ? new EntityOcelot(player.worldObj) : new EntityTNTPrimed(player.worldObj);
                projectile.setPosition(player.posX + x_offset, minY, player.posZ + z_offset);
                player.worldObj.spawnEntityInWorld(projectile);
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        if (args.size() == 1) return CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]));
        else if (args.size() == 2) return Arrays.asList("kitty", "tnt");
        else return Collections.emptyList();
    }
}

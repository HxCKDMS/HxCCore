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
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

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
        return "nuke";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                EntityPlayerMP player = (EntityPlayerMP) sender;
                spawnProjectiles(player, false);
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.makeItRain.self").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                break;
            case 1:
                if (Arrays.asList(GlobalVariables.server.getConfigurationManager().getAllUsernames()).contains(args.get(0))) {
                    EntityPlayerMP target = CommandBase.getPlayer(sender, args.get(0));
                    spawnProjectiles(target, false);
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.makeItRain.other.sender", target.getDisplayName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                    target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.makeItRain.other.target", sender.getCommandSenderName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));

                } else if (sender instanceof EntityPlayerMP) {
                    player = (EntityPlayerMP) sender;
                    spawnProjectiles(player, args.get(0).equalsIgnoreCase("kitty"));
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.makeItRain.self").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                }
                break;
            case 2:
                EntityPlayerMP target = CommandBase.getPlayer(sender, args.get(0));
                spawnProjectiles(target, args.get(1).equalsIgnoreCase("kitty"));
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.makeItRain.other.sender", target.getDisplayName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.makeItRain.other.target", sender.getCommandSenderName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                break;
        }


    }

    private static void spawnProjectiles(EntityPlayerMP player, boolean isKitty) {
        int minY = player.worldObj.getTopSolidOrLiquidBlock((int) Math.round(player.posX), (int) Math.round(player.posZ)) + 64;

        for (int x_offset = -10; x_offset <= 10; x_offset += 5) {
            for (int z_offset = -10; z_offset <= 10; z_offset += 5) {
                Entity projectile = isKitty ? new EntityOcelot(player.worldObj) : new EntityTNTPrimed(player.worldObj, player.posX + x_offset, minY, player.posZ + z_offset, player);
                projectile.setPosition(player.posX + x_offset, minY, player.posZ + z_offset);
                player.worldObj.spawnEntityInWorld(projectile);
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        if (args.size() == 1) return CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]));
        else if (args.size() == 2) return Arrays.asList("kitty", "tnt");
        else return Collections.emptyList();
    }
}

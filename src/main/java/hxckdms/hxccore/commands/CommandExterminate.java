package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandExterminate extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 4;
    }

    @Override
    public String getCommandName() {
        return "exterminate";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        int entitiesKilled = 0;

        for (WorldServer worldServer : DimensionManager.getWorlds()) {
            for (Entity entity : worldServer.loadedEntityList) {
                if (entity instanceof EntityPlayer) continue;
                switch (args.size()) {
                    case 0:
                        if (entity instanceof IMob) {
                            entity.setDead();
                            ++entitiesKilled;
                        }
                        break;
                    default:
                        if (args.contains("hostiles") && (entity instanceof IMob)) {
                            entity.setDead();
                            ++entitiesKilled;
                        } else if (args.contains("items") && (entity instanceof EntityItem)) {
                            entity.setDead();
                            ++entitiesKilled;
                        } else if (args.contains("xp") && (entity instanceof EntityXPOrb)) {
                            entity.setDead();
                            ++entitiesKilled;
                        } else if (args.contains("passive") && (entity instanceof EntityAnimal && !(entity instanceof EntityTameable))) {
                            entity.setDead();
                            ++entitiesKilled;
                        } else if (args.contains("tamable") && (entity instanceof EntityTameable)) {
                            entity.setDead();
                            ++entitiesKilled;
                        } else if (args.contains("hostiles") && (entity instanceof IMob)) {
                            entity.setDead();
                            ++entitiesKilled;
                        } else if (args.stream().anyMatch(str -> entity.getName().toLowerCase().contains(str.toLowerCase()))) {
                            entity.setDead();
                            ++entitiesKilled;
                        }
                        break;
                }
            }
        }

        sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.exterminate.killed", entitiesKilled).setStyle(new Style().setColor(TextFormatting.BLUE)));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return Arrays.asList("hostiles", "items", "passives", "xp", "tamable");
    }
}

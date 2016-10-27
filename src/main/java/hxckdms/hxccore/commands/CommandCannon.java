package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandCannon extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 3;
    }

    @Override
    public String getCommandName() {
        return "cannon";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP)) throw new TranslatedCommandException(sender, "commands.error.playersOnly");

        EntityPlayerMP player = (EntityPlayerMP) sender;
        int speed = args.size() >= 1 ? CommandBase.parseInt(args.get(0)) : 2;
        boolean isKitty = args.size() == 2 && args.get(1).equalsIgnoreCase("kitty");

        Vec3d vector = player.getLookVec();

        Entity projectile = isKitty ? new EntityOcelot(player.worldObj) : new EntityTNTPrimed(player.worldObj, player.posX, player.posY, player.posZ, player);
        projectile.setPosition(player.posX, player.posY + player.eyeHeight, player.posZ);

        projectile.motionX = vector.xCoord * speed;
        projectile.motionY = vector.yCoord * speed;
        projectile.motionZ = vector.zCoord * speed;
        projectile.isAirBorne = true;

        player.worldObj.spawnEntityInWorld(projectile);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        if (args.size() == 1) return Collections.singletonList(Integer.toString(2));
        else if (args.size() == 2) return Collections.singletonList("kitty");
        else return Collections.emptyList();
    }
}

package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandKill extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 5;
    }

    @Override
    public String getName() {
        return "kill";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;

                    player.attackEntityFrom(new DamageSource("command_hxc_kill." + player.world.rand.nextInt(35)) {
                        @Override
                        public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
                            return ServerTranslationHelper.getTranslation(sender, "death.attack." + damageType, entityLivingBaseIn.getDisplayName());
                        }
                    }.setDamageAllowedInCreativeMode().setDamageIsAbsolute().setDamageBypassesArmor(), Float.MAX_VALUE);
                }
                break;
            case 1:
                EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));

                boolean killed = target.attackEntityFrom(new DamageSource("command_hxc_kill." + target.world.rand.nextInt(35)) {
                    @Override
                    public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
                        return ServerTranslationHelper.getTranslation(target, "death.attack." + damageType, entityLivingBaseIn.getDisplayName());
                    }
                }.setDamageAllowedInCreativeMode().setDamageIsAbsolute().setDamageBypassesArmor(), Float.MAX_VALUE);
                sender.sendMessage(ServerTranslationHelper.getTranslation(sender, killed ? "commands.kill.successful" : "commands.kill.failed", target.getDisplayName()).setStyle(new Style().setColor(killed ? TextFormatting.GREEN : TextFormatting.YELLOW)));
                break;
        }
    }

    @Override
    public List<String> addTabCompletions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getOnlinePlayerNames()) : Collections.emptyList();
    }
}

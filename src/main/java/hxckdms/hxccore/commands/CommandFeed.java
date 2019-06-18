package hxckdms.hxccore.commands;

import cpw.mods.fml.common.Loader;
import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandFeed extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 2;
    }

    @Override
    public String getCommandName() {
        return "feed";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    player.getFoodStats().addStats(20, 1F);

                    if (Loader.isModLoaded("TerraFirmaCraft") || Loader.isModLoaded("terrafirmacraft")) {
                        try {
                            NBTTagCompound tfcf = player.getEntityData().getCompoundTag("ForgeData").getCompoundTag("foodCompound");
                            tfcf.setFloat("nutrDairy", 0.75f);
                            tfcf.setFloat("nutrProtein", 0.75f);
                            tfcf.setFloat("nutrVeg", 0.75f);
                            tfcf.setFloat("nutrGrain", 0.75f);
                            tfcf.setFloat("nutrFruit", 0.75f);
                            tfcf.setFloat("foodLevel", 24f);
                            tfcf.setFloat("waterLevel", 24000f);
                            tfcf.setFloat("foodSaturationLevel", 24f);
                            tfcf.setBoolean("satFruit", true);
                            tfcf.setBoolean("satDairy", true);
                            tfcf.setBoolean("satProtein", true);
                            tfcf.setBoolean("satVeg", true);
                            tfcf.setBoolean("satGrain", true);
                            tfcf.setBoolean("shouldSendUpdate", true);
                        } catch (Exception ignored) { }
                    }

                    sender.addChatMessage(ServerTranslationHelper.getTranslation(player, "commands.feed.self").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
                }
                break;
            case 1:
                EntityPlayerMP target = CommandBase.getPlayer(sender, args.removeFirst());
                target.getFoodStats().addStats(20, 1F);
                if (Loader.isModLoaded("TerraFirmaCraft") || Loader.isModLoaded("terrafirmacraft")) {
                    try {
                        NBTTagCompound tfcf = target.getEntityData().getCompoundTag("ForgeData").getCompoundTag("foodCompound");
                        tfcf.setFloat("nutrDairy", 0.75f);
                        tfcf.setFloat("nutrProtein", 0.75f);
                        tfcf.setFloat("nutrVeg", 0.75f);
                        tfcf.setFloat("nutrGrain", 0.75f);
                        tfcf.setFloat("nutrFruit", 0.75f);
                        tfcf.setFloat("foodLevel", 24f);
                        tfcf.setFloat("waterLevel", 24000f);
                        tfcf.setFloat("foodSaturationLevel", 24f);
                        tfcf.setBoolean("satFruit", true);
                        tfcf.setBoolean("satDairy", true);
                        tfcf.setBoolean("satProtein", true);
                        tfcf.setBoolean("satVeg", true);
                        tfcf.setBoolean("satGrain", true);
                        tfcf.setBoolean("shouldSendUpdate", true);
                    } catch (Exception ignored) {}
                }

                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.feed.other.sender", sender.getCommandSenderName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
                target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.feed.other.target", target.getDisplayName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames()) : Collections.emptyList();
    }
}

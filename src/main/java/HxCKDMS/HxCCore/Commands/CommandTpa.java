package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Config;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.Utils.Teleporter;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class CommandTpa implements ISubCommand {
    public static CommandTpa instance = new CommandTpa();

    @Override
    public String getCommandName() {
        return "tpa";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        System.out.println(args[1]);
        EntityPlayerMP player = (EntityPlayerMP)sender;

        EntityPlayerMP PlayerThatTPs = null;

        for(EntityPlayerMP key : HxCCore.tpaRequestList.keySet()){
            if(HxCCore.tpaRequestList.get(key) == player){
                PlayerThatTPs = key;
            }
        }

        if(args[1].equals("accept")){
            if(HxCCore.TpaTimeoutList.containsKey(PlayerThatTPs) && PlayerThatTPs != null){
                HxCCore.tpaRequestList.remove(PlayerThatTPs);
                HxCCore.TpaTimeoutList.remove(PlayerThatTPs);
                player.addChatComponentMessage(new ChatComponentText("You accepted: " + PlayerThatTPs.getDisplayName() + "'s tp request."));
                PlayerThatTPs.addChatComponentMessage(new ChatComponentText(player.getDisplayName() + " accepted your tp request."));

                if(PlayerThatTPs.dimension != player.dimension)
                    Teleporter.transferPlayerToDimension(PlayerThatTPs, player.dimension, PlayerThatTPs.mcServer.getConfigurationManager(), (int)player.posX, (int)player.posY, (int)player.posZ);
                else
                    PlayerThatTPs.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
            }else{
                player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "There is no tpa request to accept."));
            }
        }else if(args[1].equals("deny")){
            if(HxCCore.TpaTimeoutList.containsKey(PlayerThatTPs) && PlayerThatTPs != null){
                HxCCore.tpaRequestList.remove(PlayerThatTPs);
                HxCCore.TpaTimeoutList.remove(PlayerThatTPs);
                player.addChatComponentMessage(new ChatComponentText("You denied: " + PlayerThatTPs.getDisplayName() + "'s tp request."));
                PlayerThatTPs.addChatComponentMessage(new ChatComponentText(player.getDisplayName() + " denied your tp request."));
            }else{
                player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "There is no tpa request to deny."));
            }
        }else{
            EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
            if(PlayerThatTPs == null){
                HxCCore.tpaRequestList.put(player, player2);
                HxCCore.TpaTimeoutList.put(player, Config.TpaTimeout);
                player2.addChatComponentMessage(new ChatComponentText("Player: " + player.getDisplayName() + " wants to teleport to you."));
                player.addChatComponentMessage(new ChatComponentText("Teleport request successfully sent to player: " + player2.getDisplayName() + "."));
            }else{
                player.addChatComponentMessage(new ChatComponentText("Player is already being tpa'd to."));
            }

        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return net.minecraft.command.CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    }
}
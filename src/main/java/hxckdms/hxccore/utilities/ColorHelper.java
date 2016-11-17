package hxckdms.hxccore.utilities;

import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.registry.CommandRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static hxckdms.hxccore.libraries.GlobalVariables.devTags;
import static hxckdms.hxccore.libraries.GlobalVariables.permissionData;

public class ColorHelper {
    private static Map<Character, EnumChatFormatting> chatThingies = new HashMap<>();

    static {
        for (EnumChatFormatting textFormatting : EnumChatFormatting.values())
            chatThingies.put(textFormatting.getFormattingCode(), textFormatting);
    }

    private static ChatComponentTranslation color(String message, char defaultColor) {
        if (message == null || message.isEmpty()) return new ChatComponentTranslation("");

        char currentColor = defaultColor;
        HashSet<Character> currentEffects = new HashSet<>();
        LinkedList<String> words = new LinkedList<>(Arrays.asList(message.split(" ")));
        ChatComponentTranslation text = new ChatComponentTranslation("");

        StringBuilder cBuilder = new StringBuilder();

        for (String word : words) {
            LinkedList<Character> characters = new LinkedList<>(Arrays.asList(ArrayUtils.toObject(word.toCharArray())));

            Character prevChar = null;
            for (Character character : characters) {
                if (prevChar != null) {
                    if (prevChar == '&' && chatThingies.get(character) != null) {
                        EnumChatFormatting formatting = chatThingies.get(character);

                        if (cBuilder.length() != 0) {
                            ChatComponentTranslation subText = new ChatComponentTranslation(cBuilder.toString());
                            ChatStyle subStyle = subText.getChatStyle();


                            subStyle.setColor(chatThingies.get(currentColor))
                                    .setBold(currentEffects.contains(EnumChatFormatting.BOLD.getFormattingCode()))
                                    .setItalic(currentEffects.contains(EnumChatFormatting.ITALIC.getFormattingCode()))
                                    .setObfuscated(currentEffects.contains(EnumChatFormatting.OBFUSCATED.getFormattingCode()))
                                    .setStrikethrough(currentEffects.contains(EnumChatFormatting.STRIKETHROUGH.getFormattingCode()))
                                    .setUnderlined(currentEffects.contains(EnumChatFormatting.UNDERLINE.getFormattingCode()));

                            text.appendSibling(subText);

                            cBuilder = new StringBuilder();
                        }

                        if (formatting.isColor()) {
                            if (!Configuration.bannedColorCharacters.contains(character)) currentColor = character;
                            prevChar = null;
                            continue;
                        } else if (formatting.isFancyStyling()) {
                            if (!Configuration.bannedColorCharacters.contains(character)) currentEffects.add(character);
                            prevChar = null;
                            continue;
                        } else if (formatting == EnumChatFormatting.RESET) {
                            currentColor = 'f';
                            currentEffects = new HashSet<>();
                            prevChar = null;
                            continue;
                        }
                    } else cBuilder.append(prevChar == '%' ? "%%" : prevChar);
                }

                //last
                prevChar = character;
            }
            if (prevChar != null) cBuilder.append(prevChar == '%' ? "%%" : prevChar);
            cBuilder.append(' ');
        }

        ChatComponentTranslation subText = new ChatComponentTranslation(cBuilder.toString().trim());
        ChatStyle subStyle = subText.getChatStyle();

        subStyle.setColor(chatThingies.get(currentColor))
                .setBold(currentEffects.contains(EnumChatFormatting.BOLD.getFormattingCode()))
                .setItalic(currentEffects.contains(EnumChatFormatting.ITALIC.getFormattingCode()))
                .setObfuscated(currentEffects.contains(EnumChatFormatting.OBFUSCATED.getFormattingCode()))
                .setStrikethrough(currentEffects.contains(EnumChatFormatting.STRIKETHROUGH.getFormattingCode()))
                .setUnderlined(currentEffects.contains(EnumChatFormatting.UNDERLINE.getFormattingCode()));

        text.appendSibling(subText);
        return text;
    }

    public static ChatComponentTranslation handleMessage(String message, char defaultColor) {
        ChatComponentTranslation text = color(message, defaultColor);
        return text == null ? new ChatComponentTranslation("") : text;
    }

    public static final HashMap<UUID, String> playerNickNames = new HashMap<>();
    public static final HashMap<UUID, Boolean> isPlayerOp = new HashMap<>();

    public static ChatComponentTranslation handleNick(EntityPlayer player, boolean nameTagRender) {
        if (nameTagRender) return playerNickNames.get(player.getUniqueID()) == null || playerNickNames.get(player.getUniqueID()).isEmpty() ? color(player.getDisplayName(), isPlayerOp.get(player.getUniqueID()) != null && isPlayerOp.get(player.getUniqueID()) ? '4' : 'f') : color(playerNickNames.get(player.getUniqueID()), 'f');
        else {
            if (HxCPlayerInfoHandler.getString(player, "NickName") == null || "".equals(HxCPlayerInfoHandler.getString(player, "NickName")))
                return (Arrays.asList(((EntityPlayerMP) player).mcServer.getConfigurationManager().func_152603_m().func_152685_a()).contains(player.getDisplayName()) ? color(player.getDisplayName(), '4') : color(player.getDisplayName(), 'f'));
            else return color(HxCPlayerInfoHandler.getString(player, "NickName"), 'f');
        }
    }

    @SuppressWarnings("unused")
    public static String getTagName(String name, Entity entity) {
        ChatComponentTranslation text;
        if (entity instanceof EntityPlayer) return handleNick((EntityPlayer) entity, true).getFormattedText();
        else return (text = color(name, 'f')) == null ? name : text.getFormattedText();
    }

    public static ChatComponentTranslation handlePermission(EntityPlayerMP player) {
        return handlePermission(permissionData.getInteger(player.getUniqueID().toString()));
    }

    public static ChatComponentTranslation handlePermission(int permissionLevel) {
        return color(CommandRegistry.CommandConfig.commandPermissions.get(permissionLevel).name, 'f');
    }

    public static ChatComponentTranslation handleGameMode(EntityPlayerMP player) {
        String GMColor = "&";
        switch (player.theItemInWorldManager.getGameType()) {
            case CREATIVE:
                GMColor += "6";
                break;
            case SURVIVAL:
                GMColor += "2";
                break;
            case ADVENTURE:
                GMColor += "b";
                break;
        }

        return color(GMColor + StringUtils.capitalize(player.theItemInWorldManager.getGameType().getName()), 'f');
    }

    public static ChatComponentTranslation handleDimension(EntityPlayerMP player) {
        return handleDimension(player.dimension);
    }

    public static ChatComponentTranslation handleDimension(WorldServer worldServer) {
        return handleDimension(worldServer.provider.dimensionId);
    }

    public static ChatComponentTranslation handleDimension(int dimensionID) {
        ChatComponentTranslation textComponent = new ChatComponentTranslation(DimensionManager.getProvider(dimensionID).getDimensionName());
        switch (dimensionID) {
            case -1:
                textComponent.getChatStyle().setColor(EnumChatFormatting.RED);
                break;
            case 0:
                textComponent.getChatStyle().setColor(EnumChatFormatting.GREEN);
                break;
            case 1:
                textComponent.getChatStyle().setColor(EnumChatFormatting.YELLOW);
                break;
        }
        return textComponent;
    }

    public static ChatComponentTranslation handleChat(String message, EntityPlayerMP player) {
        System.out.println("message = " + message);
        String colorString = HxCPlayerInfoHandler.getString(player, "ChatColor");
        char color = (colorString == null || colorString.isEmpty()) ? 'f' : colorString.charAt(0);

        UUID UUID = player.getUniqueID();

        ChatComponentTranslation gameMode = handleGameMode(player);
        ChatComponentTranslation devTag = devTags.containsKey(UUID) ? color('\uFD3E' + devTags.get(UUID) + "&f\uFD3F", 'f') : new ChatComponentTranslation("");
        ChatComponentTranslation permissionTag = handlePermission(player);
        ChatComponentTranslation nick = handleNick(player, false);
        ChatComponentTranslation chatMessage = (PermissionHandler.getPermissionLevel(player) == -1 || Configuration.coloredChatMinimumPermissionLevel <= PermissionHandler.getPermissionLevel(player)) ? color(message, color) : new ChatComponentTranslation(message.replace("%", "%%"));

        System.out.println(PermissionHandler.getPermissionLevel(player));

        return new ChatComponentTranslation(Configuration.chatMessageLayout.replace("GAMEMODE", "%1$s").replace("DEV_TAG", "%2$s").replace("PERMISSION_TAG", "%3$s").replace("PLAYER_NICK", "%4$s").replace("CHAT_MESSAGE", "%5$s"), gameMode, devTag, permissionTag, nick, chatMessage);
    }

    @SuppressWarnings("unused")
    public static String handleSign(String text) {
        ChatComponentTranslation signText = color(text, '0');
        if (signText == null) return "";
        text = signText.getFormattedText();
        return text.startsWith("\u00a70") ? text.substring(2) : text;
    }
}

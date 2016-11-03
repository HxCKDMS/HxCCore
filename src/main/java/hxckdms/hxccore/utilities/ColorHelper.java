package hxckdms.hxccore.utilities;

import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.registry.CommandRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldServer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static hxckdms.hxccore.libraries.GlobalVariables.devTags;
import static hxckdms.hxccore.libraries.GlobalVariables.permissionData;

public class ColorHelper {
    private static Map<Character, TextFormatting> chatThingies = new HashMap<>();

    static {
        for (TextFormatting textFormatting : TextFormatting.values())
            chatThingies.put(textFormatting.formattingCode, textFormatting);
    }

    private static TextComponentTranslation color(String message, char defaultColor) {
        if (message == null || message.isEmpty()) return new TextComponentTranslation("");

        char currentColor = defaultColor;
        HashSet<Character> currentEffects = new HashSet<>();
        LinkedList<String> words = new LinkedList<>(Arrays.asList(message.split(" ")));
        TextComponentTranslation text = new TextComponentTranslation("");

        StringBuilder cBuilder = new StringBuilder();

        for (String word : words) {
            LinkedList<Character> characters = new LinkedList<>(Arrays.asList(ArrayUtils.toObject(word.toCharArray())));

            Character prevChar = null;
            for (Character character : characters) {
                if (prevChar != null) {
                    if (prevChar == '&' && chatThingies.get(character) != null) {
                        TextFormatting formatting = chatThingies.get(character);

                        if (cBuilder.length() != 0) {
                            TextComponentTranslation subText = new TextComponentTranslation(cBuilder.toString());
                            Style subStyle = subText.getStyle();


                            subStyle.setColor(chatThingies.get(currentColor))
                                    .setBold(currentEffects.contains(TextFormatting.BOLD.formattingCode))
                                    .setItalic(currentEffects.contains(TextFormatting.ITALIC.formattingCode))
                                    .setObfuscated(currentEffects.contains(TextFormatting.OBFUSCATED.formattingCode))
                                    .setStrikethrough(currentEffects.contains(TextFormatting.STRIKETHROUGH.formattingCode))
                                    .setUnderlined(currentEffects.contains(TextFormatting.UNDERLINE.formattingCode));

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
                        } else if (formatting == TextFormatting.RESET) {
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

        TextComponentTranslation subText = new TextComponentTranslation(cBuilder.toString().trim());
        Style subStyle = subText.getStyle();

        subStyle.setColor(chatThingies.get(currentColor))
                .setBold(currentEffects.contains(TextFormatting.BOLD.formattingCode))
                .setItalic(currentEffects.contains(TextFormatting.ITALIC.formattingCode))
                .setObfuscated(currentEffects.contains(TextFormatting.OBFUSCATED.formattingCode))
                .setStrikethrough(currentEffects.contains(TextFormatting.STRIKETHROUGH.formattingCode))
                .setUnderlined(currentEffects.contains(TextFormatting.UNDERLINE.formattingCode));

        text.appendSibling(subText);
        return text;
    }

    public static TextComponentTranslation handleMessage(String message, char defaultColor) {
        System.out.println(message);
        TextComponentTranslation text = color(message, defaultColor);
        return text == null ? new TextComponentTranslation("") : text;
    }

    public static final HashMap<UUID, String> playerNickNames = new HashMap<>();
    public static final HashMap<UUID, Boolean> isPlayerOp = new HashMap<>();

    public static TextComponentTranslation handleNick(EntityPlayer player, boolean nameTagRender) {
        if (nameTagRender) return "".equals(playerNickNames.get(player.getUniqueID())) ? color(player.getName(), isPlayerOp.get(player.getUniqueID()) ? '4' : 'f') : color(playerNickNames.get(player.getUniqueID()), 'f');
        else {
            if (HxCPlayerInfoHandler.getString(player, "NickName") == null || "".equals(HxCPlayerInfoHandler.getString(player, "NickName")))
                return (Arrays.asList(((EntityPlayerMP) player).mcServer.getPlayerList().getOppedPlayerNames()).contains(player.getName()) ? color(player.getName(), '4') : color(player.getName(), 'f'));
            else return color(HxCPlayerInfoHandler.getString(player, "NickName"), 'f');
        }
    }

    @SuppressWarnings("unused")
    public static String getTagName(String name, Entity entity) {
        TextComponentTranslation text;
        if (entity instanceof EntityPlayer) return handleNick((EntityPlayer) entity, true).getFormattedText();
        else return (text = color(name, 'f')) == null ? name : text.getFormattedText();
    }

    public static TextComponentTranslation handlePermission(EntityPlayerMP player) {
        return handlePermission(permissionData.getInteger(player.getUniqueID().toString()));
    }

    public static TextComponentTranslation handlePermission(int permissionLevel) {
        return color(CommandRegistry.CommandConfig.commandPermissions.get(permissionLevel).name, 'f');
    }

    public static TextComponentTranslation handleGameMode(EntityPlayerMP player) {
        String GMColor = "&";
        switch (player.interactionManager.getGameType()) {
            case CREATIVE:
                GMColor += "6";
                break;
            case SURVIVAL:
                GMColor += "2";
                break;
            case ADVENTURE:
                GMColor += "b";
                break;
            case SPECTATOR:
                GMColor += "7";
                break;
        }

        return color(GMColor + StringUtils.capitalize(player.interactionManager.getGameType().getName()), 'f');
    }

    public static TextComponentTranslation handleDimension(EntityPlayerMP player) {
        return handleDimension(player.dimension);
    }

    public static TextComponentTranslation handleDimension(WorldServer worldServer) {
        return handleDimension(worldServer.provider.getDimension());
    }

    public static TextComponentTranslation handleDimension(int dimensionID) {
        TextComponentTranslation textComponent = new TextComponentTranslation(DimensionType.getById(dimensionID).getName());
        switch (dimensionID) {
            case -1:
                textComponent.getStyle().setColor(TextFormatting.RED);
                break;
            case 0:
                textComponent.getStyle().setColor(TextFormatting.GREEN);
                break;
            case 1:
                textComponent.getStyle().setColor(TextFormatting.YELLOW);
                break;
        }
        return textComponent;
    }

    public static TextComponentTranslation handleChat(String message, EntityPlayerMP player) {
        String colorString = HxCPlayerInfoHandler.getString(player, "ChatColor");
        char color = (colorString == null || colorString.isEmpty()) ? 'f' : colorString.charAt(0);

        UUID UUID = player.getUniqueID();

        TextComponentTranslation gameMode = handleGameMode(player);
        TextComponentTranslation devTag = devTags.containsKey(UUID) ? color('\uFD3E' + devTags.get(UUID) + "&f\uFD3F", 'f') : new TextComponentTranslation("");
        TextComponentTranslation permissionTag = handlePermission(player);
        TextComponentTranslation nick = handleNick(player, false);
        TextComponentTranslation chatMessage = Configuration.coloredChatMinimumPermissionLevel <= PermissionHandler.getPermissionLevel(player) ? color(message, color) : new TextComponentTranslation(message);

        return new TextComponentTranslation(Configuration.chatMessageLayout.replace("GAMEMODE", "%1$s").replace("DEV_TAG", "%2$s").replace("PERMISSION_TAG", "%3$s").replace("PLAYER_NICK", "%4$s").replace("CHAT_MESSAGE", "%5$s"), gameMode, devTag, permissionTag, nick, chatMessage);
    }

    @SuppressWarnings("unused")
    public static String handleSign(String text) {
        TextComponentTranslation signText = color(text, '0');
        if (signText == null) return "";
        text = signText.getFormattedText();
        return text.startsWith("\u00a70") ? text.substring(2) : text;
    }
}
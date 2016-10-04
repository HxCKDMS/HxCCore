package hxckdms.hxccore.utilities;

import hxckdms.hxccore.configs.Configuration;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

import static hxckdms.hxccore.libraries.GlobalVariables.devTags;

public class ColorHelper {
    private static Map<Character, TextFormatting> chatThingies = new HashMap<>();
    private static Field formattingCodeField;

    static {
        try {
            formattingCodeField = TextFormatting.class.getDeclaredField("formattingCode");
            formattingCodeField.setAccessible(true);

            for (TextFormatting textFormatting : TextFormatting.values())
                chatThingies.put(formattingCodeField.getChar(textFormatting), textFormatting);


        } catch (NoSuchFieldException | IllegalAccessException ignored) {}
    }

    private static TextComponentTranslation color(String message, char defaultColor) {
        try {
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
                                        .setBold(currentEffects.contains(formattingCodeField.getChar(TextFormatting.BOLD)))
                                        .setItalic(currentEffects.contains(formattingCodeField.getChar(TextFormatting.ITALIC)))
                                        .setObfuscated(currentEffects.contains(formattingCodeField.getChar(TextFormatting.OBFUSCATED)))
                                        .setStrikethrough(currentEffects.contains(formattingCodeField.getChar(TextFormatting.STRIKETHROUGH)))
                                        .setUnderlined(currentEffects.contains(formattingCodeField.getChar(TextFormatting.UNDERLINE)));

                                subText.setStyle(subStyle);
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
                    .setBold(currentEffects.contains(formattingCodeField.getChar(TextFormatting.BOLD)))
                    .setItalic(currentEffects.contains(formattingCodeField.getChar(TextFormatting.ITALIC)))
                    .setObfuscated(currentEffects.contains(formattingCodeField.getChar(TextFormatting.OBFUSCATED)))
                    .setStrikethrough(currentEffects.contains(formattingCodeField.getChar(TextFormatting.STRIKETHROUGH)))
                    .setUnderlined(currentEffects.contains(formattingCodeField.getChar(TextFormatting.UNDERLINE)));


            subText.setStyle(subStyle);
            text.appendSibling(subText);
            return text;
        } catch (IllegalAccessException unhandled) {
            return null;
        }
    }

    public static HashMap<UUID, String> playerNickNames = new HashMap<>();
    public static HashMap<UUID, Boolean> isPlayerOp = new HashMap<>();

    public static TextComponentTranslation handleNick(EntityPlayer player, boolean nameTagRender) {
        if (nameTagRender) return "".equals(playerNickNames.get(player.getUniqueID())) ? color(player.getName(), isPlayerOp.get(player.getUniqueID()) ? '4' : 'f') : color(playerNickNames.get(player.getUniqueID()), 'f');
        else {
            if ("".equals(HxCPlayerInfoHandler.getString(player, "NickName")))
                return (Arrays.asList(((EntityPlayerMP) player).mcServer.getPlayerList().getOppedPlayerNames()).contains(player.getName()) ? color(player.getName(), '4') : color(player.getName(), 'f'));
            else return color(HxCPlayerInfoHandler.getString(player, "NickName"), 'f');
        }
    }

    public static String getTagName(String name, Entity entity) {
        TextComponentTranslation text;
        if (entity instanceof EntityPlayer) return handleNick((EntityPlayer) entity, true).getFormattedText();
        else return (text = color(name, 'f')) == null ? name : text.getFormattedText();
    }

    public static TextComponentTranslation handleChat(String message, EntityPlayerMP player) {
        UUID UUID = player.getUniqueID();
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

        TextComponentTranslation gameMode = color(GMColor + StringUtils.capitalize(player.interactionManager.getGameType().getName()), 'f');
        TextComponentTranslation devTag = devTags.containsKey(UUID) ? color('\uFD3E' + devTags.get(UUID) + "&f\uFD3F", 'f') : new TextComponentTranslation("");
        TextComponentTranslation permissionTag = new TextComponentTranslation("temp");
        TextComponentTranslation nick = handleNick(player, false);
        TextComponentTranslation chatMessage = color(message, 'f');


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

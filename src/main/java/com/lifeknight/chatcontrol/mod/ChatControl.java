package com.lifeknight.chatcontrol.mod;

import com.lifeknight.chatcontrol.utilities.Utils;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.ArrayList;

import static com.lifeknight.chatcontrol.mod.ChatControlMod.*;

public class ChatControl {

    public static String swearToRegex(String swear) {
        StringBuilder result = new StringBuilder();
        char[] asChars = swear.toCharArray();

        for (int i = 0; i < asChars.length; i++) {
            if (sensitivity.getValue() != 0) {
                result.append(appropriateLetterRegex(asChars[i])).append("{1,").append(2 * sensitivity.getValue()).append("}");
            } else {
                result.append(appropriateLetterRegex(asChars[i]));
            }

            if (i != asChars.length - 1) {
                if (sensitivity.getValue() != 0) {
                    result.append("[\\s]{0,").append(2 * sensitivity.getValue()).append("}");
                } else {
                    result.append("[\\s]");
                }
            }
        }
        return result.toString();
    }

    public static String appropriateLetterRegex(char character) {
        switch (Character.toLowerCase(character)) {
            case 'a': {
                return "[a|\\d]";
            }
            case 'b': {
                return "[b|d]";
            }
            case 'c':
            case 'k': {
                return "[c|k|q]";
            }
            case 'd': {
                return "[d|b]";
            }
            case 'e': {
                return "[e|\\d]";
            }
            case 'f': {
                return "[f]";
            }
            case 'g': {
                return "[g|9]";
            }
            case 'h': {
                return "[h|#]";
            }
            case 'i': {
                return "[i|l|\\d]";
            }
            case 'j': {
                return "[j]";
            }
            case 'l': {
                return "[l|1]";
            }
            case 'n':
            case 'm': {
                return "[n|m]";
            }
            case 'o': {
                return "[o|0]";
            }
            case 'q':
            case 'p': {
                return "[p|q|9]";
            }
            case 'r': {
                return "[r]";
            }
            case 's': {
                return "[s|$|5]";
            }
            case 't': {
                return "[t|7]";
            }
            case 'v':
            case 'w':
            case 'u': {
                return "[u|v|w|\\d]";
            }
            case 'x': {
                return "[x]";
            }
            case 'y': {
                return "[y|7]";
            }
            case 'z': {
                return "[z]";
            }
            default: {
                return "";
            }
        }
    }

    public static void processMessage(ClientChatReceivedEvent event) {
        String message = event.message.getFormattedText();
        String unformattedText = Utils.removeFormattingCodes(message);

        if (onHypixel) {
            if (message.contains(":")) {
                String preMessage = message.substring(0, message.indexOf(":") + 1);
                if (shouldHideChatType(Utils.removeFormattingCodes(preMessage)) || shouldHideMessage(Utils.removeFormattingCodes(message))) {
                    event.setCanceled(true);
                    System.out.println("Message hidden by ChatControl: " + event.message.getUnformattedText());
                    hiddenMessages.add(new Message(event.message));
                } else if (chatFilter.getValue()) {
                    String processedMessage = processMessageSwears(message);
                    if (!processedMessage.equals(message)) {
                        censoredMessages.add(new Message(event.message));
                        System.out.println("Message censored by ChatControl: " + event.message.getUnformattedText());
                    }

                    IChatComponent newMessage;
                    if (event.message.getSiblings().size() != 0) {
                        IChatComponent firstSibling = event.message.getSiblings().get(0);
                        String firstSiblingText = firstSibling.getFormattedText();
                        newMessage = new ChatComponentText(processMessageSwears(message.substring(0, message.indexOf(firstSiblingText)))).setChatStyle(event.message.getChatStyle());

                        for (IChatComponent chatComponent: event.message.getSiblings()) {
                            IChatComponent replacementSibling = new ChatComponentText(processMessageSwears(chatComponent.getFormattedText()));
                            replacementSibling.setChatStyle(chatComponent.getChatStyle());
                            newMessage.appendSibling(replacementSibling);
                        }
                    } else {
                        newMessage = new ChatComponentText(processedMessage);
                    }

                    event.message = newMessage;
                }
            } else if (shouldHideHypixelMessage(unformattedText)) {
                event.setCanceled(true);
                System.out.println("Message hidden by ChatControl: " + event.message.getUnformattedText());
                hiddenMessages.add(new Message(event.message));
            }
        } else if (shouldHideMessage(unformattedText)) {
            event.setCanceled(true);
            System.out.println("Message hidden by ChatControl: " + event.message.getUnformattedText());
            hiddenMessages.add(new Message(event.message));
        } else {
            String processedMessage = processMessageSwears(message);
            if (!message.equals(processedMessage)) {
                censoredMessages.add(new Message(event.message));
            }
            IChatComponent newMessage;

            if (event.message.getSiblings().size() != 0) {
                IChatComponent firstSibling = event.message.getSiblings().get(0);
                String firstSiblingText = firstSibling.getFormattedText();
                newMessage = new ChatComponentText(processMessageSwears(message.substring(0, message.indexOf(firstSiblingText)))).setChatStyle(event.message.getChatStyle());

                for (IChatComponent chatComponent: event.message.getSiblings()) {
                    IChatComponent replacementSibling = new ChatComponentText(processMessageSwears(chatComponent.getFormattedText()));
                    replacementSibling.setChatStyle(chatComponent.getChatStyle());
                    newMessage.appendSibling(replacementSibling);
                }
            } else {
                newMessage = new ChatComponentText(processedMessage);
            }
            event.message = newMessage;
        }
    }

    public static boolean shouldHideChatType(String input) {
        if (!hide.getValue()) {
            return false;
        }

        if (containsPlayer(input, Utils.getUsername())) {
            return false;
        }

        if (messageIsNotFromWhitelistedPlayer(input)) {
            return true;
        }

        if (messageIsFromBlacklistedPlayer(input)) {
            return true;
        }

        if (messageIsPartyChat(input) && hidePartyChat.getValue()) {
            return true;
        }

        if (messageIsGuildChat(input) && hideGuildChat.getValue()) {
            return true;
        }

        if (messageIsShoutChat(input) && hideShoutChat.getValue()) {
            return true;
        }

        if (messageIsMessageChat(input) && hideMessageChat.getValue()) {
            return true;
        }

        return messageIsAllChat(input) && hideAllChat.getValue();
    }

    public static boolean messageIsPartyChat(String input) {
        return input.startsWith("Party ");
    }

    public static boolean messageIsGuildChat(String input) {
        return input.startsWith("Guild ");
    }

    public static boolean messageIsShoutChat(String input) {
        return input.startsWith("[SHOUT]");
    }

    public static boolean messageIsMessageChat(String input) {
        return input.startsWith("From ");
    }

    public static boolean messageIsAllChat(String input) {
        return !messageIsPartyChat(input) && !messageIsGuildChat(input) && !messageIsShoutChat(input) && !messageIsMessageChat(input) && !input.startsWith("To:");
    }

    public static boolean messageIsNotFromWhitelistedPlayer(String input) {
        if (!chatWhitelist.getValue()) {
            return false;
        } else return !containsAnyPlayer(input, whitelistedChatPlayers.getValue());
    }

    public static boolean messageIsFromBlacklistedPlayer(String input) {
        if (!chatBlacklist.getValue()) {
            return false;
        } else return containsAnyPlayer(input, blacklistedChatPlayers.getValue());
    }

    public static String processSwear(String input) {
        if (input == null) {
            return "";
        }
        if (censorType.getCurrentValue() == 1) {
            return censorVowels(input);
        }
        return Utils.multiplyString("*", input.length());
    }

    public static boolean containsAdvertisement(String input) {
        String result = input;
        if (hideAdvertisementMessages.getValue() && hide.getValue()) {
            String[] advertisements = {"/party join", "/p join", "/guild join", "/g join", "/visit", "/housing", "/play"};
            for (String advertisement: advertisements) {
                result = result.replaceAll("(?i)" + advertisement, ".");
                if (censorAdvancedSwears.getValue()) {
                    result = result.replaceAll("(?i)" + swearToRegex(advertisement), ".");
                }
            }
            return !result.equals(input);
        }
        return false;
    }
    
    public static String processMessageSwears(String input) {
        String result = input;
        if (censorBasicSwears.getValue()) {
            for (String swear: censoredWords.getValue()) {
                result = result.replaceAll("(?i)" + swear, processSwear(swear));
                if (censorAdvancedSwears.getValue()) {
                    result = result.replaceAll("(?i)" + swearToRegex(swear), processSwear(swear));
                }
            }
        }
        return result;
    }

    public static String censorVowels(String input) {
        char[] asChars = input.toCharArray();
        for (int i = 0; i < input.length(); i++) {
            if (Utils.isVowel(asChars[i])) {
                asChars[i] = '*';
            }
        }
        return new String(asChars);
    }

    public static boolean shouldHideMessage(String input) {
        return (hide.getValue() && hideSwearMessages.getValue() && !input.equals(processMessageSwears(input))) || containsAdvertisement(input);
    }

    public static boolean shouldHideHypixelMessage(String input) {
        if (hide.getValue()) {
            if (hideSwearMessages.getValue() && !input.equals(processMessageSwears(input))) {
                return true;
            }

            if (!(joinLeaveWhitelist.getValue() && containsAnyPlayer(input, joinLeaveWhitelistedPlayers.getValue())) && (input.endsWith(" joined.") || input.endsWith(" left."))) {
                if ((input.startsWith("Friend ") && hideFriendJoinLeaveMessages.getValue()) || (input.startsWith("Guild ") && hideGuildJoinLeaveMessages.getValue())) {
                    return true;
                }
            }

            return (input.contains("joined the lobby!") && hideLobbyJoinMessages.getValue()) || ((input.contains("found a") || input.contains("has found")) && hideLobbyCosmeticMessages.getValue()) || (hideBedWarsShopMessages.getValue() && (input.contains("purchased") || input.startsWith("You don't have enough")) || shouldHideMessageForUselessness(input));
        }
        return false;
    }

    public static boolean shouldHideMessageForUselessness(String input) {
        if (!hideUselessMessages.getValue()) {
            return false;
        }

        if (input.startsWith("+")) {
            return true;
        }

        if (input.startsWith("The game")) {
            return true;
        }

        if (input.startsWith("You will"))  {
            return true;
        }

        if (input.startsWith("If you get")) {
            return true;
        }

        if (input.equals("Cages opened! FIGHT!")) {
            return true;
        }

        if (input.toLowerCase().contains("teaming")) {
            return true;
        }

        if (input.contains("Gain XP")) {
            return true;
        }

        if (input.equals("You have respawned!")) {
            return true;
        }

        if (input.startsWith("Sending you to")) {
            return true;
        }

        if (input.startsWith("You gained")) {
            return true;
        }

        if (input.contains("gathered") && input.contains("head")) {
            return true;
        }

        return input.contains("store.hypixel.net");
    }

    public static boolean containsAnyPlayer(String input, ArrayList<String> players) {
        for (String player: players) {
            if (containsPlayer(input, player)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsPlayer(String input, String player) {
        input = input.toLowerCase();
        player = player.toLowerCase();
        return input.contains(" " + player + " ") || (input.startsWith(player + " ") || input.startsWith(player + ":")) || input.contains(" " + player + ":");
    }
}

package com.lifeknight.chatcontrol.utilities;

import net.minecraft.client.Minecraft;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Utils {
    public static int height = 0;
    public static int width = 0;

    public static String getUsername() {
        return Minecraft.getMinecraft().thePlayer.getName();
    }

    public static ArrayList<String> returnStartingEntries(ArrayList<String> arrayList, String input) {
        ArrayList<String> result = new ArrayList<>();
        if (!input.equals("") && arrayList != null) {
            for (String element: arrayList) {
                try {
                    if (element.toLowerCase().startsWith(input.toLowerCase())) {
                        result.add(element);
                    }
                } catch (Exception ignored) {

                }
            }
        } else {
            result.addAll(arrayList);
        }
        return result;
    }

    public static String removeAll(String msg, String rmv) {
        msg = msg.replaceAll(rmv, "");
        return msg;
    }

    public static String removeFormattingCodes(String input) {
        String formattingSymbol = "";
        formattingSymbol += '\u00A7';

        input = removeAll(input, formattingSymbol + "0");
        input = removeAll(input, formattingSymbol + "1");
        input = removeAll(input, formattingSymbol + "2");
        input = removeAll(input, formattingSymbol + "3");
        input = removeAll(input, formattingSymbol + "4");
        input = removeAll(input, formattingSymbol + "5");
        input = removeAll(input, formattingSymbol + "6");
        input = removeAll(input, formattingSymbol + "7");
        input = removeAll(input, formattingSymbol + "8");
        input = removeAll(input, formattingSymbol + "9");
        input = removeAll(input, formattingSymbol + "a");
        input = removeAll(input, formattingSymbol + "b");
        input = removeAll(input, formattingSymbol + "c");
        input = removeAll(input, formattingSymbol + "d");
        input = removeAll(input, formattingSymbol + "e");
        input = removeAll(input, formattingSymbol + "f");
        input = removeAll(input, formattingSymbol + "k");
        input = removeAll(input, formattingSymbol + "l");
        input = removeAll(input, formattingSymbol + "m");
        input = removeAll(input, formattingSymbol + "n");
        input = removeAll(input, formattingSymbol + "o");
        input = removeAll(input, formattingSymbol + "r");

        return input;
    }

    public static boolean isVowel(char character) {
        return character == 'a' || character == 'e' || character == 'i' || character == 'o' || character == 'u';
    }

    public static String multiplyString(String string, int times) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < times; i++) {
            result.append(string);
        }
        return result.toString();
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("hh:mm:ss a").format(new Date());
    }

    public static int scale(int toScale) {
        switch (Minecraft.getMinecraft().gameSettings.guiScale) {
            case 1: {
                return toScale * 2;
            }
            case 2: {
                return toScale;
            }
            default: {
                return (int) (toScale / 1.5);
            }
        }
    }

    public static int get2ndPanelCenter() {
        return getScaledHeight(300) + (width - getScaledWidth(300)) / 2;
    }

    public static int getSupposedWidth() {
        switch (Minecraft.getMinecraft().gameSettings.guiScale) {
            case 1: {
                return 1920;
            }
            case 2: {
                return 960;
            }
            default: {
                return 640;
            }
        }
    }

    public static int getSupposedHeight() {
        switch (Minecraft.getMinecraft().gameSettings.guiScale) {
            case 1: {
                return 960;
            }
            case 2: {
                return 540;
            }
            default: {
                return 360;
            }
        }
    }

    public static int getScaledWidth(int widthIn) {
        return scale((int) (widthIn * ((double) width / getSupposedWidth())));
    }

    public static int getScaledHeight(int heightIn) {
        return scale((int) (heightIn * ((double) height / getSupposedHeight())));
    }
}

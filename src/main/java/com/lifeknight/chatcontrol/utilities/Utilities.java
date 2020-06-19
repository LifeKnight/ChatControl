package com.lifeknight.chatcontrol.utilities;

import net.minecraft.client.Minecraft;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Utilities {
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
                } catch (Exception ignored) {}
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
            case 0:
                return (int) ((toScale * 2) / (double)getScaleFactor());
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
        return getScaledHeight(300) + (getGameWidth()- getScaledWidth(300)) / 2;
    }

    public static int getSupposedWidth() {
        return 1920 / getScaleFactor();
    }

    public static int getSupposedHeight() {
        return 1080 / getScaleFactor();
    }

    public static int getScaledWidth(int widthIn) {
        return scale((int) (widthIn * ((double) getGameWidth() / (double) getSupposedWidth())));
    }

    public static int getScaledHeight(int heightIn) {
        return scale((int) (heightIn * ((double) getGameHeight() / (double) getSupposedHeight())));
    }

    private static int getScaleFactor() {
        int scaledWidth = Minecraft.getMinecraft().displayWidth;
        int scaledHeight = Minecraft.getMinecraft().displayHeight;
        int scaleFactor = 1;
        boolean flag = Minecraft.getMinecraft().isUnicode();
        int i = Minecraft.getMinecraft().gameSettings.guiScale;

        if (i == 0) {
            i = 1000;
        }

        while (scaleFactor < i && scaledWidth / (scaleFactor + 1) >= 320 && scaledHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }

        if (flag && scaleFactor % 2 != 0 && scaleFactor != 1) {
            --scaleFactor;
        }

        return scaleFactor;
    }

    public static int getGameWidth() {
        if (Minecraft.getMinecraft().gameSettings.guiScale != 0) {
            return Minecraft.getMinecraft().displayWidth / Minecraft.getMinecraft().gameSettings.guiScale;
        }
        return (int) (Math.ceil(Minecraft.getMinecraft().displayWidth / (double)getScaleFactor()));
    }

    public static int getGameHeight() {
        if (Minecraft.getMinecraft().gameSettings.guiScale != 0) {
            return Minecraft.getMinecraft().displayHeight / Minecraft.getMinecraft().gameSettings.guiScale;
        }
        return (int) (Math.ceil(Minecraft.getMinecraft().displayHeight / (double)getScaleFactor()));
    }

    public static int scaleFrom1080pWidth(int widthIn) {
        int i = widthIn / getScaleFactor();
        return (int) (i * (getGameWidth() / (double) getSupposedWidth()));
    }

    public static int scaleFrom1080pHeight(int heightIn) {
        int i = heightIn / getScaleFactor();
        return (int) (i * (getGameHeight() / (double) getSupposedHeight()));
    }

    public static int scaleTo1080pWidth(int widthIn) {
        int i = widthIn * getScaleFactor();
        return (int) (i * (getSupposedWidth() / (double) getGameWidth()));
    }

    public static int scaleTo1080pHeight(int heightIn) {
        int i = heightIn * getScaleFactor();
        return (int) (i * (getSupposedHeight() / (double) getGameHeight()));
    }
}

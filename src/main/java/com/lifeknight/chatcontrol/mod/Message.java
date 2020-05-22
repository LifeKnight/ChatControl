package com.lifeknight.chatcontrol.mod;

import com.lifeknight.chatcontrol.utilities.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import static com.lifeknight.chatcontrol.mod.ChatControlMod.modColor;


public class Message {
    private final String time;
    private final IChatComponent message;

    public Message(IChatComponent message) {
        this.message = message;
        time = Utils.getCurrentTime();
    }

    public void printMessage() {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("").appendText(modColor + "" + EnumChatFormatting.BOLD + "[" + time + "] " + EnumChatFormatting.RESET).appendSibling(message));
    }
}

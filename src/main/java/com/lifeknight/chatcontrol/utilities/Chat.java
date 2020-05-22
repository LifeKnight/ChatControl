package com.lifeknight.chatcontrol.utilities;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.lifeknight.chatcontrol.mod.ChatControlMod.*;
import static net.minecraft.util.EnumChatFormatting.*;

public class Chat {

	public static final ArrayList<String> queuedMessages = new ArrayList<>();

	public static void addChatMessage(String msg) {
		if (Minecraft.getMinecraft().theWorld != null) {
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(modColor + "" + EnumChatFormatting.BOLD + modName + " > " + EnumChatFormatting.RESET + msg));
		} else {
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					addChatMessage(msg);
				}
			}, 100L);
		}
	}
	public static void addCommandUsageMessage(String msg) {
		addChatMessage(DARK_GREEN + msg);
	}

	public static void addErrorMessage(String msg) {
		addChatMessage(RED + msg);
	}

	public static void addChatMessageWithoutName(String msg) {
		if (Minecraft.getMinecraft().theWorld != null) {
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(msg));
		} else {
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					addChatMessageWithoutName(msg);
				}
			}, 100L);
		}
	}

	public static void queueChatMessageForConnection(String msg) {
		queuedMessages.add(msg);
	}
}

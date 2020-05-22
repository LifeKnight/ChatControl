package com.lifeknight.chatcontrol.mod;

import com.lifeknight.chatcontrol.gui.LifeKnightGui;
import com.lifeknight.chatcontrol.utilities.Chat;
import com.lifeknight.chatcontrol.utilities.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static com.lifeknight.chatcontrol.mod.ChatControlMod.*;
import static net.minecraft.util.EnumChatFormatting.*;

public class ChatControlCommand extends CommandBase {
	private final List<String> aliases = new ArrayList<>();
	private final String[] mainCommands = {"gui", "view"};
	private final String[] viewCommands = {"censoredmessages", "hiddenmessages"};

	public ChatControlCommand() {
		aliases.add("cc");
	}

	public String getCommandName() {
		return modID;
	}

	public String getCommandUsage(ICommandSender arg0) {
		return modID;
	}

	public List<String> addTabCompletionOptions(ICommandSender arg0, String[] arg1, BlockPos arg2) {

		if (arg1.length == 1) {
			return Utils.returnStartingEntries(new ArrayList<>(Arrays.asList(mainCommands)), arg1[0]);
		} else if (arg1.length == 2) {
			 if (arg1[0].equalsIgnoreCase("view")) {
				return Utils.returnStartingEntries(new ArrayList<>(Arrays.asList(viewCommands)), arg1[1]);
			}
		}
		return new ArrayList<>(Arrays.asList(mainCommands));
	}

	public boolean canCommandSenderUseCommand(ICommandSender arg0) {
		return true;
	}

	public List<String> getCommandAliases() {
		return aliases;
	}

	public boolean isUsernameIndex(String[] arg0, int arg1) {
		return false;
	}

	public int compareTo(ICommand o) {
		return 0;
	}

	public void processCommand(ICommandSender arg0, String[] arg1) throws CommandException {

		if (arg1.length >= 1) {
			switch (arg1[0].toLowerCase()) {
				case "gui": {
					ChatControlMod.openGui(new LifeKnightGui(modName, variables));
					break;
				}
				case "view": {
					processViewCommand(arg1);
					break;
				}
				default: {
					addMainCommandMessage();
					break;
				}
			}
		} else {
			ChatControlMod.openGui(new LifeKnightGui(modName, variables));
		}
		config.updateConfigFromVariables();
	}

	public void addMainCommandMessage() {
		StringBuilder result = new StringBuilder(DARK_GREEN + "/" + modID);

		for (String command: mainCommands) {
			result.append(" ").append(command).append(",");
		}

		Chat.addChatMessage(result.substring(0, result.length() - 1));
	}

	public void processViewCommand(String[] args) {
		if (args.length > 1) {
			switch (args[1].toLowerCase()) {
				case "cm":
				case "censoredmessages": {
					if (args.length > 2) {
						try {
							int page = Integer.parseInt(args[2]);
							if (page < 1) {
								Chat.addErrorMessage("Invalid Input! [min: 1]");
							} else {
								addCensoredMessages(page);
							}
						} catch (Exception e) {
							Chat.addErrorMessage("Invalid input! [min: 1]");
						}
					} else {
						addCensoredMessages(1);
					}
					break;
				}
				case "hm":
				case "hiddenmessages": {
					if (args.length > 2) {
						try {
							int page = Integer.parseInt(args[2]);
							if (page < 1) {
								Chat.addErrorMessage("Invalid Input! [min: 1]");
							} else {
								addHiddenMessages(page);
							}
						} catch (Exception e) {
							Chat.addErrorMessage("Invalid input! [min: 1]");
						}
					} else {
						addHiddenMessages(1);
					}
					break;
				}
				default: {
					Chat.addCommandUsageMessage("/chatcontrol view censoredmessages, hiddenmessages");
					break;
				}
			}
		} else {
			Chat.addCommandUsageMessage("/chatcontrol view censoredmessages, hiddenmessages");
		}
	}
	public void addCensoredMessages(int page) {
		if (censoredMessages.size() > 0) {
			if (censoredMessages.size() - ((page - 1) * 8) > 0) {
				IChatComponent next;
				IChatComponent last;
				if (censoredMessages.size() - (page * 8) > 0) {
					ChatStyle chatStyle = new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(AQUA + "Click here to view the next page.")));
					next = new ChatComponentText(GOLD + ">>").setChatStyle(chatStyle.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chatcontrol view censoredmessages " + (page + 1))));
				} else {
					next = new ChatComponentText(GOLD + "||").setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(YELLOW + "This is the end of the list."))));
				}
				if (page > 1) {
					ChatStyle chatStyle = new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(AQUA + "Click here to view the previous page.")));
					last = new ChatComponentText(GOLD + "<<").setChatStyle(chatStyle.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chatcontrol view censoredmessages " + (page - 1))));
				} else {
					last = new ChatComponentText(GOLD + "||").setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(YELLOW + "This is the beginning of the list."))));
				}
				IChatComponent header = new ChatComponentText("").appendSibling(last).appendText(modColor + " -----------" + GOLD + "[" + page + "]" + modColor + "----------- ").appendSibling(next);
				Minecraft.getMinecraft().thePlayer.addChatMessage(header);
				for (int i = 8 * (page - 1) + 1; i < 8 * page; i++) {
					int x = censoredMessages.size() - i;
					if (x == -1) {
						break;
					}
					censoredMessages.get(x).printMessage();
				}
				Chat.addChatMessageWithoutName(modColor + "-------------------------");
			} else {
				Chat.addErrorMessage("The page number " + YELLOW + page + RED + " is out of bounds.");
			}
		} else {
			Chat.addErrorMessage("There are no censored messages to display.");
		}
	}

	public void addHiddenMessages(int page) {
		if (hiddenMessages.size() > 0) {
			if (hiddenMessages.size() - ((page - 1) * 8) > 0) {
				IChatComponent next;
				IChatComponent last;
				if (hiddenMessages.size() - (page * 8) > 0) {
					ChatStyle chatStyle = new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(AQUA + "Click here to view the next page.")));
					next = new ChatComponentText(GOLD + ">>").setChatStyle(chatStyle.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chatcontrol view hiddenmessages " + (page + 1))));
				} else {
					next = new ChatComponentText(GOLD + "||").setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(YELLOW + "This is the end of the list."))));
				}
				if (page > 1) {
					ChatStyle chatStyle = new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(AQUA + "Click here to view the previous page.")));
					last = new ChatComponentText(GOLD + "<<").setChatStyle(chatStyle.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chatcontrol view hiddenmessages " + (page - 1))));
				} else {
					last = new ChatComponentText(GOLD + "||").setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(YELLOW + "This is the beginning of the list."))));
				}
				IChatComponent header = new ChatComponentText("").appendSibling(last).appendText(modColor + " -----------" + GOLD + "[" + page + "]" + modColor + "----------- ").appendSibling(next);
				Minecraft.getMinecraft().thePlayer.addChatMessage(header);
				for (int i = 8 * (page - 1) + 1; i < 8 * page; i++) {
					int x = hiddenMessages.size() - i;
					if (x == 0) {
						break;
					}
					hiddenMessages.get(x).printMessage();
				}
				Chat.addChatMessageWithoutName(modColor + "---------------------------");
			} else {
				Chat.addErrorMessage("The page number " + YELLOW + page + RED + " is out of bounds.");
			}
		} else {
			Chat.addErrorMessage("There are no hidden messages to display.");
		}
	}
}

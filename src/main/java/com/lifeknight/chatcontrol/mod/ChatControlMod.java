package com.lifeknight.chatcontrol.mod;

import com.lifeknight.chatcontrol.utilities.*;
import com.lifeknight.chatcontrol.variables.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import static com.lifeknight.chatcontrol.mod.ChatControlMod.*;

@net.minecraftforge.fml.common.Mod(modid = modID, name = modName, version = modVersion, clientSideOnly = true)
public class ChatControlMod {
	public static final String modName = "ChatControl",
			modVersion = "1.1",
			modID = "chatcontrol";
	public static final EnumChatFormatting modColor = EnumChatFormatting.GREEN;
	public static boolean onHypixel = false, openGui = false;
	public static GuiScreen guiToOpen;
	public static final ArrayList<LifeKnightVariable> variables = new ArrayList<>();
	public static final LifeKnightBoolean runMod = new LifeKnightBoolean("Mod", "Main", true);
	public static final LifeKnightBoolean chatFilter = new LifeKnightBoolean("ChatFilter", "Main", true);
	public static final LifeKnightBoolean hide = new LifeKnightBoolean("Hide", "Main", true);
	public static final LifeKnightStringList censoredWords = new LifeKnightStringList("CensoredWords", "ChatFilter", new ArrayList<>(Arrays.asList(
			"arse",
			"bitch",
			"blowjob",
			"boob",
			"clitoris",
			"cock",
			"coon",
			"cunt",
			"dick",
			"dik",
			"bich",
			"dildo",
			"fag",
			"fuck",
			"fuk",
			"goddamn",
			"goddam",
			"homo",
			"nigger",
			"nigga",
			"niger",
			"penis",
			"pussy",
			"shit",
			"slut",
			"vagina",
			"whore")));
	public static final LifeKnightBoolean censorBasicSwears = new LifeKnightBoolean("CensorBasicSwears", "ChatFilter", true, censoredWords);
	public static final LifeKnightBoolean censorAdvancedSwears = new LifeKnightBoolean("CensorAdvancedSwears", "ChatFilter", false);
	public static final LifeKnightBoolean hideSwearMessages = new LifeKnightBoolean("HideSwearMessages", "ChatFilter", false);
	public static final LifeKnightInteger sensitivity = new LifeKnightInteger("Sensitivity", "ChatFilter", 3, 0, 10);
	public static final LifeKnightCycle censorType = new LifeKnightCycle("CensorType", "ChatFilter", new ArrayList<>(Arrays.asList("Star", "Vowel")), 0);
	public static final LifeKnightBoolean hideAllChat = new LifeKnightBoolean("HideAllChat", "Hide", false);
	public static final LifeKnightBoolean hidePartyChat = new LifeKnightBoolean("HidePartyChat", "Hide", false);
	public static final LifeKnightBoolean hideGuildChat = new LifeKnightBoolean("HideGuildChat", "Hide", false);
	public static final LifeKnightBoolean hideShoutChat = new LifeKnightBoolean("HideShoutChat", "Hide", false);
	public static final LifeKnightBoolean hideMessageChat = new LifeKnightBoolean("HideMessageChat", "Hide", false);
	public static final LifeKnightStringList whitelistedChatPlayers = new LifeKnightStringList("WhitelistedChatPlayers", "Hide");
	public static final LifeKnightStringList blacklistedChatPlayers = new LifeKnightStringList("BlacklistedChatPlayers", "Hide");
	public static final LifeKnightBoolean chatWhitelist = new LifeKnightBoolean("ChatWhitelist", "Hide", false, whitelistedChatPlayers);
	public static final LifeKnightBoolean chatBlacklist = new LifeKnightBoolean("ChatBlacklist", "Hide", false, blacklistedChatPlayers);
	public static final LifeKnightBoolean hideFriendJoinLeaveMessages = new LifeKnightBoolean("HideFriendJoin-LeaveMessages", "Hide", false);
	public static final LifeKnightBoolean hideGuildJoinLeaveMessages = new LifeKnightBoolean("HideGuildJoin-LeaveMessages", "Hide", false);
	public static final LifeKnightStringList joinLeaveWhitelistedPlayers = new LifeKnightStringList("Join-LeaveWhitelistedPlayers", "Hide");
	public static final LifeKnightBoolean joinLeaveWhitelist = new LifeKnightBoolean("Join-LeaveWhitelist", "Hide", false, joinLeaveWhitelistedPlayers);
	public static final LifeKnightBoolean hideLobbyJoinMessages = new LifeKnightBoolean("HideLobbyJoinMessages", "Extra", false);
	public static final LifeKnightBoolean hideLobbyCosmeticMessages = new LifeKnightBoolean("HideLobbyCosmeticMessages", "Extra", false);
	public static final LifeKnightBoolean hideAdvertisementMessages = new LifeKnightBoolean("HideAdvertisementMessages", "Extra", false);
	public static final LifeKnightBoolean hideBedWarsShopMessages = new LifeKnightBoolean("HideBedWarsShopMessages", "Extra", false);
	public static final LifeKnightBoolean hideUselessMessages = new LifeKnightBoolean("HideUselessMessages", "Extra", false);
	public static final ArrayList<Message> hiddenMessages = new ArrayList<>();
	public static final ArrayList<Message> censoredMessages = new ArrayList<>();
	public static final Configuration configuration = new Configuration();

	@EventHandler
	public void init(FMLInitializationEvent initEvent) {
		MinecraftForge.EVENT_BUS.register(this);
		ClientCommandHandler.instance.registerCommand(new ChatControlCommand());
	}
	
	@SubscribeEvent
    public void onConnect(final FMLNetworkEvent.ClientConnectedToServerEvent event) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (Minecraft.getMinecraft().theWorld != null) {
                	for (String msg: Chat.queuedMessages) {
                		Chat.addChatMessage(msg);
                	}
                }
                try {
					onHypixel = Minecraft.getMinecraft().getCurrentServerData().serverIP.toLowerCase().contains("hypixel.net");
				} catch (Exception ignored) {

				}
            }
        }, 2000L);
    }

    @SubscribeEvent
    public void onChatMessageReceived(ClientChatReceivedEvent event) {
		if (runMod.getValue() && event.type == 0) {
			ChatControl.processMessage(event);
		}
	}

	@SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
		if (openGui) {
			Minecraft.getMinecraft().displayGuiScreen(guiToOpen);
			openGui = false;
		}
	}

	public static void openGui(GuiScreen guiScreen) {
		guiToOpen = guiScreen;
		openGui = true;
	}
}
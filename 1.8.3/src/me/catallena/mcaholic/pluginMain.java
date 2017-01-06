package me.catallena.mcaholic;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.catallena.mcaholic.NoteBlockAPI.SongPlayer;
import me.catallena.mcaholic.api.MusicThread;
import me.catallena.mcaholic.cmd.adminCommands;
import me.catallena.mcaholic.cmd.playerCommands;
import me.catallena.mcaholic.listeners.playerJoinLeft;
import me.catallena.mcaholic.listeners.playerMove;

public class pluginMain extends JavaPlugin {
		
	public static MusicThread mt;
	public static pluginMain instance;	
	public static HashMap<String, ArrayList<SongPlayer>> playingSongs = new HashMap<String, ArrayList<SongPlayer>>();
	public static HashMap<String, Byte> playerVolume = new HashMap<String, Byte>();

	public void onEnable() {
		instance = this;
		
		mt = new MusicThread(getSongFolder());
		
		if(mt.getSongs().length == 0){
			getLogger().warning("Alert! No songs found.");
		} else {
			Bukkit.getScheduler().runTaskTimer(this, mt, 0, 20);
			getMusicThread().randomSong();
			pluginMain.getMusicThread().getSongPlayer().setPlaying(true);
		}
		Bukkit.broadcastMessage(ChatColor.DARK_GREEN+""+ChatColor.STRIKETHROUGH+"---------------------------------------");
		Bukkit.broadcastMessage(ChatColor.GREEN+"Hi, "+ChatColor.YELLOW+"I'm PondJa."+ChatColor.AQUA+" The Main Creator of This plugin");
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(ChatColor.WHITE+""+ChatColor.BOLD+"Plugin Topic: "+ChatColor.GREEN+ChatColor.BOLD+"Music");
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(ChatColor.AQUA+"Adviser Teacher: AJ. Jurarat Seeya");
		Bukkit.broadcastMessage(ChatColor.GREEN+"Co-Adviser Teacher: AJ. Nutthapong Seenaj");
		Bukkit.broadcastMessage(ChatColor.YELLOW+"Member: "+ChatColor.GOLD+"Palapon Soontornpas , Thanyalak Kaewkah , Netiporn Thaitawatkul , Phichayanan Thisongmuang , Suphisara Yaworn");
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"Have a nice day :)");
		Bukkit.broadcastMessage(ChatColor.DARK_GREEN+""+ChatColor.STRIKETHROUGH+"---------------------------------------");
		regCmds();
		regEvents();
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(ChatColor.BLUE + "Music System is" + ChatColor.GREEN + ChatColor.BOLD + " enabled.");
			getMusicThread().getSongPlayer().addPlayer(p);
			saveConfig();
			p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
			}
	}


	public void onDisable() {
		pluginMain.getMusicThread().getSongPlayer().setPlaying(false);
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(ChatColor.BLUE + "Music System [Action Bar EDITION] is" + ChatColor.RED + ChatColor.BOLD + " disabled");
			p.playSound(p.getLocation(), Sound.NOTE_PLING, 10, 0);
			getMusicThread().getSongPlayer().removePlayer(p);
		}
	}

	public void regCmds() {
		getCommand("musicadmin").setExecutor(new adminCommands());
		getCommand("music").setExecutor(new playerCommands(this));
	}

	public void regEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new playerJoinLeft(this), this);
		pm.registerEvents(new playerMove(this), this);
	}

	public static boolean isReceivingSong(Player p) {
		return ((pluginMain.playingSongs.get(p.getName()) != null) && (!pluginMain.playingSongs.get(p.getName()).isEmpty()));
	}

	public static void stopPlaying(Player p) {
		if (pluginMain.playingSongs.get(p.getName()) == null) {
			return;
		}
		for (SongPlayer s : pluginMain.playingSongs.get(p.getName())) {
			s.removePlayer(p);
		}
	}

	public static void setPlayerVolume(Player p, byte volume) {
		playerVolume.put(p.getName(), volume);
	}

	public static byte getPlayerVolume(Player p) {
		Byte b = playerVolume.get(p.getName());
		if (b == null) {
			b = 100;
			playerVolume.put(p.getName(), b);
		}
		return b;
	}
	
	public static pluginMain getInstance() {
		return instance;
	}

	public static MusicThread getMusicThread() {
		return pluginMain.mt;
	}
	
	public static File getSongFolder(){
		return new File(getInstance().getDataFolder(), "songs/");
	}
	
}

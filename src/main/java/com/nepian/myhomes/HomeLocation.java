package com.nepian.myhomes;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class HomeLocation implements Serializable {
	private UUID worldUid;
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;
	
	public HomeLocation(Location location) {
		worldUid = location.getWorld().getUID();
		x = location.getX();
		y = location.getY();
		z = location.getZ();
		yaw = location.getYaw();
		pitch = location.getPitch();
	}
	
	public Location getLocation(JavaPlugin plugin) {
		World world = plugin.getServer().getWorld(worldUid);
		return new Location(world, x, y, z, yaw, pitch);
	}
}

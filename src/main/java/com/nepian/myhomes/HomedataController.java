package com.nepian.myhomes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.nepian.npcore.util.Util;
import com.nepian.npcore.util.sqlite.SQLite;

public class HomedataController {
	private String TABLE = "homedata";
	private String UUID = "uuid";
	private String NAME = "name";
	private String LOCATION = "location";
	
	private String WHERE_UUID_NAME = " where " + UUID + "=? and " + NAME + "=?";
	private String HAS = "select * from " + TABLE + WHERE_UUID_NAME;
	private String ADD_HOME = "insert into " + TABLE + " values (?,?,?)";
	private String UPDATE_HOME = "update " + TABLE + " set " + LOCATION + "=?" + WHERE_UUID_NAME;
 	private String GET_HOME = "select " + LOCATION + " from " + TABLE + WHERE_UUID_NAME;
 	private String GET_HOMES = "select " + NAME + ", " + LOCATION + " from " + TABLE + " where " + UUID + "=?";
 	private String GET_HOME_NAMES = "select " + NAME + " from " + TABLE + " where " + UUID + "=?";
 	private String REMOVE_HOME = "delete from " + TABLE + WHERE_UUID_NAME;
	
	private SQLite sqlite;
	private Myhomes plugin;

	public HomedataController(Myhomes plugin, SQLite sqlite) {
		this.sqlite = sqlite;
		this.plugin = plugin;
		createTable();
	}
	
	/**
	 * ホームデータを追加する
	 * @param uuid プレイヤーのUUID
	 * @param name ホームの名前
	 * @param location ホームの場所
	 * @throws SQLException 
	 */
	public void addHome(UUID uuid, String name, Location location) throws SQLException {
		PreparedStatement ps = sqlite.getPreparedStatement(ADD_HOME);
		
		ps.setString(1, uuid.toString());
		ps.setString(2, name);
		ps.setBytes(3, this.getByteObject(location));
		ps.executeUpdate();
	}
	
	public void addHome(Player player, String name) throws SQLException {
		addHome(player.getUniqueId(), name, player.getLocation());
	}
	
	/**
	 * ホームデータを更新する
	 * @param uuid プレイヤーのUUID
	 * @param name ホームの名前
	 * @param location ホームの場所
	 * @throws SQLException 
	 */
	public void updateHome(UUID uuid, String name, Location location) throws SQLException {
		PreparedStatement ps = sqlite.getPreparedStatement(UPDATE_HOME);
		
		ps.setBytes(1, this.getByteObject(location));
		ps.setString(2, uuid.toString());
		ps.setString(3, name);
		ps.executeUpdate();
	}
	
	public void updateHome(Player player, String name) throws SQLException {
		updateHome(player.getUniqueId(), name, player.getLocation());
	}
	
	/**
	 * データが登録されているか確認する
	 * @param uuid プレイヤーのUUID
	 * @param name ホームの名前
	 * @return (登録済)-> true (非登録)-> false
	 */
	public boolean has(UUID uuid, String name) {
		try {
			PreparedStatement ps = sqlite.getPreparedStatement(HAS);
			ps.setString(1, uuid.toString());
			ps.setString(2, name);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean has(OfflinePlayer player, String name) {
		return has(player.getUniqueId(), name);
	}
	
	/**
	 * ホームの場所を取得する
	 * @param uuid プレイヤーのUUID
	 * @param name ホームの名前
	 * @return (Location)-> ホームの場所
	 */
	public Location getHome(UUID uuid, String name) {
		try {
			PreparedStatement ps = sqlite.getPreparedStatement(GET_HOME);
			ps.setString(1, uuid.toString());
			ps.setString(2, name);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) return this.createLocation(rs.getBytes(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Location getHome(OfflinePlayer player, String name) {
		return getHome(player.getUniqueId(), name);
	}
	
	/**
	 * ホームの一覧を取得する
	 * @param uuid プレイヤーのUUID
	 * @return (Map<String, Location>)-> ホームの一覧を保持したMap
	 */
	public Map<String, Location> getHomes(UUID uuid) {
		Map<String, Location> homes = Util.newMap();
		
		try {
			PreparedStatement ps = sqlite.getPreparedStatement(GET_HOMES);
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				homes.put(rs.getString(1), this.createLocation(rs.getBytes(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return homes;
	}
	
	public Map<String, Location> getHomes(OfflinePlayer player) {
		return getHomes(player.getUniqueId());
	}
	
	/**
	 * ホーム名の一覧を取得する
	 * @param uuid プレイヤーのUUID
	 * @return (List<String>)-> ホーム名の一覧を保持したリスト
	 */
	public List<String> getHomeNames(UUID uuid) {
		List<String> homeNames = Util.newList();
		
		try {
			PreparedStatement ps = sqlite.getPreparedStatement(GET_HOME_NAMES);
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				homeNames.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return homeNames;
	}
	
	public List<String> getHomeNames(OfflinePlayer player) {
		return getHomeNames(player.getUniqueId());
	}
	
	/**
	 * ホームデータを削除する
	 * @param uuid プレイヤーのUUID
	 * @param name ホーム名
	 * @throws SQLException
	 */
	public void removeHome(UUID uuid, String name) throws SQLException {
		PreparedStatement ps = sqlite.getPreparedStatement(REMOVE_HOME);
		ps.setString(1, uuid.toString());
		ps.setString(2, name);
		ps.executeUpdate();
	}
	
	public void removeHome(OfflinePlayer player, String name) throws SQLException {
		removeHome(player.getUniqueId(), name);
	}
	
	/* Private Method ------------------------------------------------------ */
	
	/**
	 * テーブルを作成する
	 */
	private void createTable() {
		String token = "create table if not exists " + TABLE + " ( "
				+ UUID + ", "
				+ NAME + ", "
				+ LOCATION + " )";
		try {
			sqlite.getPreparedStatement(token).executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ロケーションデータをホームロケーションデータのバイト配列で取得する
	 * @param location 対象のロケーション
	 * @return (byte[])-> ホームロケーションデータのバイト配列
	 */
	private byte[] getByteObject(Location location) {
		HomeLocation homelocation = new HomeLocation(location);
		byte[] retObject = null;
		
		try {
			ByteArrayOutputStream byteos = new ByteArrayOutputStream();
			ObjectOutputStream objos = new ObjectOutputStream(byteos);
			objos.writeObject(homelocation);
			objos.close();
			byteos.close();
			retObject = byteos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retObject;
	}
	
	/**
	 * ホームロケーションデータのバイト配列からロケーションデータを作成する
	 * @param objByte ホームロケーションデータのバイト配列
	 * @return (Location)-> ロケーションデータ
	 */
	private Location createLocation(byte[] objByte) {
		HomeLocation homelocation = null;
		
		try {
			ByteArrayInputStream byteis = new ByteArrayInputStream(objByte);
			ObjectInputStream objis = new ObjectInputStream(byteis);
			homelocation = (HomeLocation) objis.readObject();
			byteis.close();
			objis.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return homelocation.getLocation(plugin);
	}
}

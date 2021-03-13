package com.revature.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * Singleton utility for creating and retrieving database connection
 */
public class ConnectionUtil {
	//private static Logger logger = Logger.getLogger(ConnectionUtil.class);
	private static ConnectionUtil cu = null;
	private static String url;
	private static String usr;
	private static String pswd;

	/**
	 * This method reads in "database.properties" file and load values for connection.
	 */
	private ConnectionUtil() {
		ClassLoader cl = getClass().getClassLoader();
		InputStream is =  cl.getResourceAsStream("database.properties");
		Properties p =  new Properties();
		try {
			p.load(is);
			url  = (String) p.getProperty("url");
			usr  = (String) p.getProperty("usr");
			pswd  = (String) p.getProperty("pswd");
		}catch(IOException e) {
		//	logger.error("Could not read properties",e);
		}
	}
	
	public static synchronized ConnectionUtil getConnectionUtil() {
		if(cu==null) {
			cu = new ConnectionUtil();
		}
		
		return cu;
	}
	
	/**
	 * This method creates and returns a Connection Object. 
	 * @return a connection to database
	 */
	public Connection getConnection() {
		Connection conn =  null;
		try {
			Class.forName("org.postgresql.Driver");

			conn = DriverManager.getConnection(url, usr, pswd);
		} catch (SQLException | ClassNotFoundException e) {
		//	logger.error("Connection to SQL database could not be established", e);
		}
		return conn;
		
	}
}

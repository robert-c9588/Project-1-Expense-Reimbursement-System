package com.revature.logger;

import org.apache.log4j.Logger;

public class ReimbSysLogger {
	private static ReimbSysLogger rsl;
	
	private ReimbSysLogger() {
		super();
	}
	
	public static synchronized ReimbSysLogger getReimbSysLogger() {
		if(rsl==null) {
			rsl = new ReimbSysLogger();
		}
		return rsl;
	}
	
	public Logger getLogger() {
		Logger logger =  null;
		
		try {
			Class.forName("org.apache.log4j.Logger");
			
			logger = Logger.getLogger(ReimbSysLogger.class);
		} catch (ClassNotFoundException e) {
			System.out.println("Unable to initialize logger.");
		}
		return logger;
	}
	
}

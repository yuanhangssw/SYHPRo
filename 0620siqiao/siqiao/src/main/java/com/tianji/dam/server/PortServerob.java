package com.tianji.dam.server;

import java.util.Observable;

public class PortServerob extends Observable {
	
	 public static String carid;
	private static PortServerob psb =new PortServerob(null);
	
	 
	
	private PortServerob(String carid) {
		  notifyObservers(carid);
	};
	
	public static PortServerob  getobport(String carid) {
		  PortServerob.carid =carid;
		return psb;
	}

	public void callob(String carid) {
		
		 notifyObservers(carid);
	}

	
	
	
}

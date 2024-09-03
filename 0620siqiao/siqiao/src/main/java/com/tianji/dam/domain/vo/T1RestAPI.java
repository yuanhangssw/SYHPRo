package com.tianji.dam.domain.vo;

import lombok.Data;

@Data
public class T1RestAPI {
	
	private double CoordX;
	private double CoordY;
	
	private double Latitude;
	private double Longitude;
	private float Elevation;
	private long  Timestamp;
	private String VehicleID;
	private float Speed;
	private int IsForward;
	private int  IsVibrate;
	
	private double VibrateValue;
	private double Frequency;
	
	private double Acceleration;
	private double Amplitude;
	private float Angle;

	private double BeforeCoordLX;//前一个点轮左X
	private double BeforeCoordLY;//前一个点轮左Y
	private double BeforeCoordRX;//前一个点轮右X
	private double BeforeCoordRY;//前一个点轮右Y
	
}

package com.tianji.dam.bean;

import lombok.Data;

@Data
public class SMSBean {
	//接收人姓名
	private String username;
	//接收人电话
	private String usertel;
	//站点名称
	private String sitename;
	//设备名称/编号
	private String	devicename;
	//预警时间
	private String time;
	//具体预警信息
	private String warning;
	private String datagid;

}

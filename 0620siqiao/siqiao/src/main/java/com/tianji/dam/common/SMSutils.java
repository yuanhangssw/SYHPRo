package com.tianji.dam.common;



import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.SMSBean;
import com.tj.system.domain.SysConfig;
import com.tj.system.mapper.SysConfigMapper;


public class SMSutils {
	  
	private static String key_value ="";
	private	static String secret_value ="";
	private	static String signName_value="";
	private	static String templateCode_value ="";
		  
 /**
  * 使用AK&SK初始化账号Client
  * @param accessKeyId
  * @param accessKeySecret
  * @return Client
  * @throws Exception
  */
 private static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
	 
	 
     Config config = new Config()
             // 您的AccessKey ID
             .setAccessKeyId(accessKeyId)
             // 您的AccessKey Secret
             .setAccessKeySecret(accessKeySecret);
     // 访问的域名
     config.endpoint = "dysmsapi.aliyuncs.com";
     return new Client(config);
     
     
     
     
 }


    /**
     *
	 * @param param
     * @return
     */
 public static  String send(SMSBean param) {
	 Client client;
	 
		if("".equals(key_value)) {
			try {
				initparam();
			} catch (Exception e) {
				e.printStackTrace();
				return "init_error";
			}
		}
		try {
		client = SMSutils.createClient(key_value, secret_value);
     SendSmsRequest sendSmsRequest = new SendSmsRequest();
	     sendSmsRequest.setPhoneNumbers(param.getUsertel());
	     sendSmsRequest.setSignName(signName_value);
	     sendSmsRequest.setTemplateCode(templateCode_value);
	     sendSmsRequest.setTemplateParam(JSON.toJSONString(param));
    
     	SendSmsResponse ssr =  client.sendSms(sendSmsRequest);
   		String code =		ssr.body.code;
   		 return code;
   		 
   		 
	} catch (Exception e) {
		e.printStackTrace();
		return "error";
	}
	 
 }
 
 
 private static void initparam() {
		SysConfigMapper config = BeanContext.getApplicationContext().getBean(SysConfigMapper.class);
		SysConfig  scf_key =new SysConfig();
		SysConfig  scf_secret =new SysConfig();
		SysConfig  scf_Sign =new SysConfig();
		SysConfig  scf_TemplateCode =new SysConfig();
		scf_key.setConfigKey("sms_accessKeyId");
		scf_secret.setConfigKey("sms_accessKeySecret");
		scf_Sign.setConfigKey("sms_signName");
		scf_TemplateCode.setConfigKey("sms_templateCode");
		scf_key =	config.selectConfig(scf_key);
		scf_secret =config.selectConfig(scf_secret);
		scf_Sign =	config.selectConfig(scf_Sign);
		scf_TemplateCode =config.selectConfig(scf_TemplateCode);
		 key_value =scf_key.getConfigValue();
		 secret_value =scf_secret.getConfigValue();
		 signName_value=scf_Sign.getConfigValue();
		 templateCode_value =scf_TemplateCode.getConfigValue();
	 
 }
 
}
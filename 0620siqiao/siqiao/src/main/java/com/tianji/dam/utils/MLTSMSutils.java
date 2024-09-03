package com.tianji.dam.utils;

import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.SMSBean;
import com.tj.common.utils.sign.Md5Utils;
import com.tj.system.domain.SysConfig;
import com.tj.system.mapper.SysConfigMapper;

import java.net.URLEncoder;

public class MLTSMSutils {


    private static String username;
    private static String password;
    private static String apikey;
    private static String baseurl;

    static {
        SysConfigMapper config = BeanContext.getApplicationContext().getBean(SysConfigMapper.class);
        SysConfig scf_key = new SysConfig();
        SysConfig scf_secret = new SysConfig();
        SysConfig scf_Sign = new SysConfig();
        SysConfig scf_TemplateCode = new SysConfig();
        scf_key.setConfigKey("mltsms_username");
        scf_secret.setConfigKey("mltsms_password");
        scf_Sign.setConfigKey("mltsms_apikey");
        scf_TemplateCode.setConfigKey("mltsms_baseurl");
        scf_key = config.selectConfig(scf_key);
        scf_secret = config.selectConfig(scf_secret);
        scf_Sign = config.selectConfig(scf_Sign);
        scf_TemplateCode = config.selectConfig(scf_TemplateCode);
        username = scf_key.getConfigValue();
        password = scf_secret.getConfigValue();
        apikey = scf_Sign.getConfigValue();
        baseurl = scf_TemplateCode.getConfigValue();
    }

    public static String send_caronline(SMSBean bean) {
        try {

            String content = "";
            String password_md5 = Md5Utils.hash(password);
            content = "【设备上线提醒】" + bean.getUsername() + "您好，" + bean.getSitename() + "的" + bean.getDevicename() + "设备已经" + bean.getWarning() + "天，没有施工上传数据，请注意 " + bean.getTime();


            String encontent = URLEncoder.encode(content, "utf-8");

            String finalcontent = "?username=" + username + "&password_md5=" + password_md5 + "&apikey=" + apikey + "&mobile=" + bean.getUsertel() + ""
                    + "&encode=UTF-8&content=" + encontent;

            String finnalurl = baseurl + finalcontent;

            String reult = HttpClientUtils.doGet(finnalurl);

            return reult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String send_speed(SMSBean bean) {
        try {
            String content = "";
            String password_md5 = Md5Utils.hash(password);
            content = "【碾压速度提醒】" + bean.getUsername() + "您好，" + bean.getSitename() + bean.getDevicename() + "连续10s速度超过2.5km/h，有碾压超速情况，请注意。";


            String encontent = URLEncoder.encode(content, "utf-8");

            String finalcontent = "?username=" + username + "&password_md5=" + password_md5 + "&apikey=" + apikey + "&mobile=" + bean.getUsertel() + ""
                    + "&encode=UTF-8&content=" + encontent;

            String finnalurl = baseurl + finalcontent;

            String reult = HttpClientUtils.doGet(finnalurl);
            System.out.println("预警发送：" + reult);
            return reult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String send_houdu(SMSBean bean) {
        try {
            String content = "";
            String password_md5 = Md5Utils.hash(password);
            //content = "【碾压厚度提醒】" + bean.getDamname() + "：" + bean.getTitle() + "，" + "当前仓位平均底高程：" + bean.getBegin_evolution() + "m，平均顶高程：" + bean.getCurrent_avg_gaocheng() + "m，碾压平均厚度：" + bean.getCurrenthoudu() + "cm，正常厚度：" + bean.getNormalhoudu() + "cm，有超正常碾压厚度情况，请注意。";
            //最终版
            content = "【碾压厚度提醒】" + bean.getDamname() + "：" + bean.getTitle() + "，当前有超正常碾压厚度情况，请注意。";
            System.out.println(content);
            String encontent = URLEncoder.encode(content, "utf-8");

            String finalcontent = "?username=" + username + "&password_md5=" + password_md5 + "&apikey=" + apikey + "&mobile=" + bean.getUsertel() + ""
                    + "&encode=UTF-8&content=" + encontent;

            String finnalurl = baseurl + finalcontent;

            String reult = HttpClientUtils.doGet(finnalurl);
            System.out.println("预警发送：" + reult);
            return reult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


}

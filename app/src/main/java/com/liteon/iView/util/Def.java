package com.liteon.iView.util;

public class Def {

    public final static String DVR_PREVIEW_URL = "http://192.168.10.1:8081/?action=stream";
    public final static String DVR_RECORDINGS_URL = "http://192.168.10.1:8080/sda1/MBA-CAM/";
    public final static String DVR_Url = "http://192.168.10.1/%s";
    //camera setting
    public final static String camera_setting = "camera.shtml";
    public final static String camera_cgi = "cgi-bin/camera.cgi";
    public final static String FRONT_CAM_MODE = "cha";
    public final static String REAR_CAM_MODE = "chb";
    public final static String FRONT_REAR_CAM_MODE = "chab";
    public final static String LENGTH_2M = "2m";
    public final static String LENGTH_3M = "3m";
    public final static String LENGTH_5M = "5m";
    //preview or storage mode
    public final static String system_setting = "system.shtml";
    public final static String system_cgi = "cgi-bin/system.cgi";
    public final static String RECORDING_MODE = "uvc";
    public final static String STORAGE_MODE = "msdc";
    //time zone setting
    public final static String adm_setting = "adm/management.shtml";
    public final static String adm_cgi = "cgi-bin/adm.cgi";
    //DVR network setting
    public final static String net_setting = "internet/wan.shtml";
    public final static String net_cgi = "cgi-bin/internet.cgi";
    //DVR security setting
    public final static String security_setting = "wireless/security.shtml";
    public final static String security_cgi = "cgi-bin/wireless.cgi";
    //DVR wifi setting
    public final static String wifi_setting = "wireless/basic.shtml";
    public final static String wifi_cgi = "cgi-bin/wireless.cgi";
    //current DVR version
    public final static String status_page = "adm/status.shtml";

    public final static String username = "admin";
    public final static String password = "admin";

    //html form key list
    public final static String PAGE = "page";
    //system
    public final static String USB_MODE = "listbox_usbmode";
    //camera
    public final static String KEY_PAGE_RECORDINGS = "camera_configuration";
    public final static String VIDEO_LENGTH = "listbox_video_length";
    public final static String RECORDING_CHANNEL = "listbox_resolution";
    public final static String LIVE_CHANNEL = "listbox_capture";
    //wireless
    public final static String KEY_PAGE_BASIC = "basic";
    public final static String WLAN_CONF = "wlan_conf";
    public final static String WIRELESSMODE = "wirelessmode";
    public final static String MSSID_0 = "mssid_0";
    public final static String MSSID_1 = "mssid_1";
    public final static String MSSID_2 = "mssid_2";
    public final static String MSSID_3 = "mssid_3";
    public final static String MSSID_4 = "mssid_4";
    public final static String MSSID_5 = "mssid_5";
    public final static String MSSID_6 = "mssid_6";
    public final static String MSSID_8 = "mssid_8";
    public final static String MSSID_9 = "mssid_9";
    public final static String MSSID_10 = "mssid_10";
    public final static String MSSID_11 = "mssid_11";
    public final static String MSSID_12 = "mssid_12";
    public final static String MSSID_13 = "mssid_13";
    public final static String MSSID_14 = "mssid_14";
    public final static String MSSID_15 = "mssid_15";
    public final static String BROADCASTSSID= "broadcastssid";
    public final static String APISOLATED = "apisolated";
    public final static String MBSSIDAPISOLATED = "mbssidapisolated";
    public final static String SZ11GCHANNEL = "sz11gChannel";
    public final static String N_MODE = "n_mode";
    public final static String N_BANDWIDTH = "n_bandwidth";
    public final static String N_GI = "n_gi";
    public final static String N_MCS = "n_mcs";
    public final static String N_RDG = "n_rdg";
    public final static String N_EXTCHA = "n_extcha";
    public final static String N_STBC = "n_stbc";
    public final static String N_AMSDU = "n_amsdu";
    public final static String N_AUTOBA = "n_autoba";
    public final static String N_BADECLINE = "n_badecline";
    public final static String N_DISALLOW_TKIP = "n_disallow_tkip";
    public final static String N_2040_COEXIT = "n_2040_coexit";
    public final static String N_LDPC = "n_ldpc";
    public final static String VHT_BANDWIDTH = "vht_bandwidth";
    public final static String VHT_STBC = "vht_stbc";
    public final static String VHT_SGI = "vht_sgi";
    public final static String VHT_BW_SIGNAL = "vht_bw_signal";
    public final static String VHT_LDPC = "vht_ldpc";
    public final static String TX_STREAM = "tx_stream";
    public final static String RX_STREAM = "rx_stream";
    //internet
    public final static String KEY_PAGE_WAN   = "wan";
    public final static String CONNECTIONTYPE = "connectionType";
    public final static String STATICIP       = "staticip";
    public final static String STATICNETMASK  = "staticnetmask";
    public final static String STATICGATEWAY  = "staticgateway";
    public final static String STATICPRIDNS   = "staticpridns";
    public final static String STATICSECDNS   = "staticsecdns";
    public final static String HOSTNAME       = "hostname";
    public final static String APN3G          = "APN3G";
    public final static String PIN3G          = "PIN3G";
    public final static String DIAL3G         = "Dial3G";
    public final static String USER3G         = "User3G";
    public final static String PASSWORD3G     = "Password3G";
    public final static String DEV3G          = "Dev3G";
    public final static String PPTPSERVER     = "pptpServer";
    public final static String PPTPUSER       = "pptpUser";
    public final static String PPTPPASS       = "pptpPass";
    public final static String MACCLONEENBL   = "maccloneenbl";
    public final static String MACCLONEMAC    = "macclonemac";
    //security
    public final static String KEY_PAGE_SECURITY          = "security";
    public final static String SSIDINDEX                  = "ssidIndex";
    public final static String SECURITY_MODE              = "security_mode";
    public final static String WEP_DEFAULT_KEY            = "wep_default_key";
    public final static String WEP_KEY_1                  = "wep_key_1";
    public final static String WEP1SELECT                 = "WEP1Select";
    public final static String WEP_KEY_2                  = "wep_key_2";
    public final static String WEP2SELECT                 = "WEP2Select";
    public final static String WEP_KEY_3                  = "wep_key_3";
    public final static String WEP3SELECT                 = "WEP3Select";
    public final static String WEP_KEY_4                  = "wep_key_4";
    public final static String WEP4SELECT                 = "WEP4Select";
    public final static String CIPHER                     = "cipher";
    public final static String PASSPHRASE                 = "passphrase";
    public final static String KEYRENEWALINTERVAL         = "keyRenewalInterval";
    public final static String PREAUTHENTICATION          = "PreAuthentication";
    public final static String RADIUSSERVERIP             = "RadiusServerIP";
    public final static String RADIUSSERVERPORT           = "RadiusServerPort";
    public final static String RADIUSSERVERSECRET         = "RadiusServerSecret";
    public final static String RADIUSSERVERSESSIONTIMEOUT = "RadiusServerSessionTimeout";
    public final static String RADIUSSERVERIDLETIMEOUT    = "RadiusServerIdleTimeout";
    public final static String APSELECT_0                 = "apselect_0";
    public final static String NEWAP_TEXT_0               = "newap_text_0";
    public final static String APSELECT_1                 = "apselect_1";
    public final static String NEWAP_TEXT_1               = "newap_text_1";
    public final static String APSELECT_2                 = "apselect_2";
    public final static String NEWAP_TEXT_2               = "newap_text_2";
    public final static String APSELECT_3                 = "apselect_3";
    public final static String NEWAP_TEXT_3               = "newap_text_3";
    public final static String APSELECT_4                 = "apselect_4";
    public final static String NEWAP_TEXT_4               = "newap_text_4";
    public final static String APSELECT_5                 = "apselect_5";
    public final static String NEWAP_TEXT_5               = "newap_text_5";
    public final static String APSELECT_6                 = "apselect_6";
    public final static String NEWAP_TEXT_6               = "newap_text_6";
    public final static String APSELECT_7                 = "apselect_7";
    public final static String NEWAP_TEXT_7               = "newap_text_7";
    //admin
    public final static String KEY_PAGE_ADM = "sysAdm";
    public final static String ADM_USER = "admuser";
    public final static String ADM_PASS = "admpass";
    //timezone
    public final static String KEY_PAGE_TIMEZONE = "ntp";
    public final static String HOSTTIME = "HOSTTIME";
    public final static String TIME_ZONE = "TIME_ZONE";
    public final static String NTPSERVERIP = "NTPSERVERIP";
    public final static String NTPSYNC = "NTPSYNC";

    //Action for HTTP request service
    public final static String ACTION_GET_ALL_INFO = "com.liteon.iView.service.action.GET_ALL";
    public final static String ACTION_GET_SYS_MODE = "com.liteon.iView.service.action.GET_SYS";
    public final static String ACTION_SET_SYS_MODE = "com.liteon.iView.service.action.SET_SYS";
    public final static String ACTION_GET_CAM_MODE = "com.liteon.iView.service.action.GET_CAM";
    public final static String ACTION_GET_INTERNET = "com.liteon.iView.service.action.GET_INET";
    public final static String ACTION_GET_WIRELESS = "com.liteon.iView.service.action.GET_WIRELESS";
    public final static String ACTION_GET_SECURITY = "com.liteon.iView.service.action.GET_SECURITY";
    public final static String ACTION_GET_ADMIN    = "com.liteon.iView.service.action.GET_ADMIN";
    public final static String ACTION_SET_TIMEZONE = "com.liteon.iView.service.action.SET_TIMEZONE";
    public final static String ACTION_SET_RECORDINGS = "com.liteon.iView.service.action.SET_RECORDINGS";
    public final static String ACTION_SET_INTERNET = "com.liteon.iView.service.action.SET_INTERNET";
    public final static String ACTION_SET_WIFI = "com.liteon.iView.service.action.SET_WIFI";
    public final static String ACTION_SET_VPN = "com.liteon.iView.service.action.SET_VPN";
    //Key for Intent extra
    public final static String EXTRA_GET_SYS_MODE = "com.liteon.iView.service.extra.GET_SYS";
    public final static String EXTRA_SET_SYS_MODE = "com.liteon.iView.service.extra.SET_SYS";
    public final static String EXTRA_GET_CAM_MODE = "com.liteon.iView.service.extra.GET_CAM";
    public final static String EXTRA_TIMEZONE     = "com.liteon.iView.service.extra.TIMEZONE";
    public final static String EXTRA_NTP_SERVER   = "com.liteon.iView.service.extra.NTP_SERVER";
    public final static String EXTRA_RECORDING_LENGTH = "com.liteon.iView.service.extra.RECORDING_LENGTH";;
    public final static String EXTRA_RECORDING_CHANNEL = "com.liteon.iView.service.extra.RECORDING_CHANNEL";
    public final static String EXTRA_APN = "com.liteon.iView.service.extra.APN";
    public final static String EXTRA_PIN = "com.liteon.iView.service.extra.PIN";
    public final static String EXTRA_DIAL_NUM = "com.liteon.iView.service.extra.DIAL_NUM";
    public final static String EXTRA_USERNAME_3G = "com.liteon.iView.service.extra.USERNAME_3G";
    public final static String EXTRA_PASSWORD_3G = "com.liteon.iView.service.extra.PASSWORD_3G";
    public final static String EXTRA_MODEM = "com.liteon.iView.service.extra.MODEM";
    public final static String EXTRA_PPTP_SERVER = "com.liteon.iView.service.extra.pptpserver";
    public final static String EXTRA_PPTP_USERNAME = "com.liteon.iView.service.extra.pptpusername";
    public final static String EXTRA_PPTP_PASSWORD = "com.liteon.iView.service.extra.pptppassword";
    public final static String EXTRA_SSID = "com.liteon.iView.service.extra.ssid";
    public final static String EXTRA_SECURITYMODE = "com.liteon.iView.service.extra.securitymode";
    public final static String EXTRA_ENCRYPTTYPE = "com.liteon.iView.service.extra.encrypttype";
    public final static String EXTRA_PASSPHASE = "com.liteon.iView.service.extra.passphase";
    //Key for sharePreference
    public final static String SHARE_PREFERENCE = "com.liteon.iView.PREFERENCE_FILE_KEY";
    public final static String SP_SSID = "SP_SSID";
    public final static String SP_BSSID = "SP_BSSID";
    public final static String SP_SECURITY = "SP_SECURITY";
    public final static String SP_ENCRYPTTYPE = "SP_ENCRYPTTYPE";
    public final static String SP_PASSPHASE = "SP_PASSPHASE";
    public final static String SP_KEYRENEW = "SP_KEYRENEW";
    public final static String SP_APN3G = "SP_APN3G";
    public final static String SP_PIN3G = "SP_PIN3G";
    public final static String SP_DIAL3G = "SP_DIAL3G";
    public final static String SP_USER3G = "SP_USER3G";
    public final static String SP_MODEM_NAME = "SP_MODEM_NAME";
    public final static String SP_MODEM_LIST_JSON = "SP_MODEM_LIST_JSON";
    public final static String SP_PASSWORD3G = "SP_PASSWORD3G";
    public final static String SP_PPTPSERVER = "SP_PPTPSERVER";
    public final static String SP_PPTPUSER = "SP_PPTPUSER";
    public final static String SP_PPTPPASS = "SP_PPTPPASS";
    public final static String SP_PPTPCLIENT = "SP_PPTPCLIENT";
    public final static String SP_TIMEZONE_LIST = "SP_TIMEZONE_LIST";
    public final static String SP_TIMEZONE = "SP_TIMEZONE";
    public final static String SP_NTPSERVER = "SP_NTPSERVER";
    public final static String SP_NTP_SYNC_VALUE = "SP_NTP_SYNC_VALUE";
    public final static String SP_RECORDING_LENGTH = "SP_RECORDING_LENGTH";
    public final static String SP_RECORDING_CAMERA = "SP_RECORDING_CAMERA";
    public final static String SP_PREVIEW_CAMERA = "SP_PREVIEW_CAMERA";
    public final static String SP_RECORDING_LIST = "SP_RECORDING_LIST";

}

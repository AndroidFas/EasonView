package eason.rxsomthing.http;

public class Config {
    public static boolean isDebug = true;
    public static boolean IS_TEST = true;//测试
//    private static boolean IS_TEST = false;//生产

    //云
//    public static final String SERVER_ROOT_URI = IS_TEST ? "http://62.234.136.68:8080" : "http://180.76.114.176";
//    public static final String SERVER_SOCKET_URI = IS_TEST ? "ws://62.234.136.68:8080" : "ws://180.76.114.176";


    //本地0
//    public static final String SERVER_ROOT_URI = "http://192.168.31.193:8080";//216 193
//    public static final String SERVER_SOCKET_URI = "ws://192.168.31.193:8080";

    //热点
//    public static final String SERVER_ROOT_URI = "http://172.20.10.4:8080";//216 193
//    public static final String SERVER_SOCKET_URI = "ws://172.20.10.4:8080";
//    本地0
//    public static final String SERVER_ROOT_URI = "http://172.20.10.4:8080";//216 193
//    public static final String SERVER_SOCKET_URI = "ws://172.20.10.4:8080";
    //家
    public static final String SERVER_ROOT_URI = "http://192.168.199.106:8080";//106
    public static final String SERVER_SOCKET_URI = "ws://192.168.199.106:8080";


    public static final String SESSIONID_KEY = "ZSESSIONID";
    public static String SESSIONID = "";

    public static String OKHTTP = "BAO_OKHTTP";


    public interface Code {
        String RELOGIN = "ERROR_CODE_00001";
        String GUZHU_CANCEL = "-1000";
        String BIND_PHONE = "ERROR_CODE_20006";
        String NEED_MONEY = "ERROR_CODE_50002";

    }
}

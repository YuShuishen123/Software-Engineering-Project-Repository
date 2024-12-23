package cn.kmbeast.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class RootUtils {

    /**
     * 获取本机的IP地址
     *
     * @return String IP地址
     */
    public static String getIpAddr() throws UnknownHostException {
        InetAddress localhost = InetAddress.getLocalHost();
        return localhost.getHostAddress();
    }

}

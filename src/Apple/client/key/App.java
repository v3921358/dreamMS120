package Apple.client.key;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class App {

    public static String MAC = "";
    public static String IP = "";
    public static String Name = "";

    public static void init() {
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            System.out.println("Current Name address : " + ip.getHostName());
            System.out.println("Current IP address : " + ip.getHostAddress());
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();
            System.out.print("Current MAC address : ");

            StringBuilder sb = new StringBuilder();
            //for (int i = 0; i < mac.length; i++) {
                //sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            //}
            Name = ip.getHostName();
            IP = ip.getHostAddress();
            MAC = sb.toString();
            System.out.println(sb.toString());
        } catch (UnknownHostException | SocketException e) {
            System.err.println(e);
        }
    }
}

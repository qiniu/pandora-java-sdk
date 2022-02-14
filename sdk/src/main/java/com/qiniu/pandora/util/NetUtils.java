package com.qiniu.pandora.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class NetUtils {

  public static int findFreePort() throws IOException {
    ServerSocket socket = new ServerSocket(0);
    return socket.getLocalPort();
  }

  public static String getHostname() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException ex) {
      return "localhost";
    }
  }
}

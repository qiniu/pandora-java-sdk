package com.qiniu.pandora.util;

import java.io.IOException;
import java.net.ServerSocket;

public class NetUtils {

  public static int findFreePort() throws IOException {
    ServerSocket socket = new ServerSocket(0);
    return socket.getLocalPort();
  }
}

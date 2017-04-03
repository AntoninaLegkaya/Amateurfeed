package com.dbbest.amateurfeed.app.net;

import com.dbbest.amateurfeed.app.net.response.ResponseWrapper;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


public class NetworkUtil {

  public static final int CODE_SOCKET_TIMEOUT = -604;
  public static final int CODE_UNABLE_TO_RESOLVE_HOST = -605;
  public static final int CODE_UNKNOWN_IO_EXCEPTION = -605;

  public static <T> ResponseWrapper<T> handleError(Exception e) {
    if (e != null) {
      if (e instanceof SocketTimeoutException) {
        return new ResponseWrapper<>(CODE_SOCKET_TIMEOUT, false, "Socket Timeout", null);
      } else if (e instanceof UnknownHostException) {
        return new ResponseWrapper<>(CODE_UNABLE_TO_RESOLVE_HOST, false, "Unable to resolve host",
            null);
      }
    }
    return new ResponseWrapper<>(CODE_UNKNOWN_IO_EXCEPTION, false,
        "An unknown exception happened ¯\\_(ツ)_/¯", null);
  }

  private NetworkUtil() {

  }
}

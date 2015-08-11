/**
 * CustomExceptionMapper
 * @author Ilja.Winokurow
 */
package org.geohunt.service.rest;

import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.message.Message;

import javax.ws.rs.core.Response;

/**
 * PreInvokeHandler.
 */
public class PreInvokeHandler {

  /**
   * handleRequest.
   * 
   * @param message
   *          -message
   * @param arg1
   *          - info
   * @return Response
   */
  public final Response handleRequest(final Message message, final ClassResourceInfo arg1) {
    // Properties prop = new Properties();
    // InputStream inputStream =
    // getClass().getClassLoader().getResourceAsStream("main.properties");
    //
    // try {
    // prop.load(inputStream);
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    //
    // Map<String, List<String>> headers = CastUtils.cast((Map<?, ?>)
    // message.get(Message.PROTOCOL_HEADERS));
    // if (headers.get("version") != prop.get("version")) {
    // return ResponseCreator.error(401, CustomError.OLD_VERSION_ERROR);
    // }
    // if (headers.get("ss_id") != null &&
    // validate(headers.get("ss_id").get(0))) {
    // // let request to continue
    // return null;
    // } else {
    // // authentication failed, request the authentication, add the realm
    // return ResponseCreator.error(401, Error.NOT_AUTHORIZED.getCode(),
    // headers.get("version").get(0));
    // }
    return null;
  }
}

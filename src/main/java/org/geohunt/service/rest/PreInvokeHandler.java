package org.geohunt.service.rest;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.message.Message;

public class PreInvokeHandler {

	// just for test
	int count = 0;

	private boolean validate(String ss_id) {
		// just for test
		// needs to implement
		count++;
		System.out.println("SessionID: " + ss_id);
		if (count == 1) {
			return false;
		} else {
			return true;
		}
	}

	public Response handleRequest(Message message, ClassResourceInfo arg1) {
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

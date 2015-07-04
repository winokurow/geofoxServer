package org.geohunt.service.rest.responce;

import javax.ws.rs.core.Response;

import org.geohunt.service.game.rest.exceptions.CustomError;

public class ResponseCreator {
	public static Response error(int status, CustomError error) {
		Response.ResponseBuilder response = Response.status(500);
		response.header("errorcode", error.getCode());
		response.header("error", error.getDescription());
		response.entity("none");
		return response.build();
	}

	public static Response success(String version, Object object) {
		Response.ResponseBuilder response = Response.ok();
		response.header("version", version);
		if (object != null) {
			response.entity(object);
		} else {
			response.entity("none");
		}
		return response.build();
	}
}

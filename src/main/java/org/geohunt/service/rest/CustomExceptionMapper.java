package org.geohunt.service.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.geohunt.service.game.rest.exceptions.Error;
import org.geohunt.service.rest.responce.ResponseCreator;

public class CustomExceptionMapper {
	@Context
	private HttpHeaders requestHeaders;

	private String getHeaderVersion() {
		return requestHeaders.getRequestHeader("version").get(0);
	}

	public Response toResponse(Exception ex) {
		System.out.println(ex.getMessage() + ex.getCause());
		return ResponseCreator.error(500, Error.SERVER_ERROR.getCode(),
				getHeaderVersion());
	}
}

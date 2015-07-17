package org.geohunt.service.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.geohunt.service.game.rest.exceptions.CustomError;
import org.geohunt.service.rest.responce.ResponseCreator;

public class CustomExceptionMapper implements ExceptionMapper<Exception> {
	@Context
	private HttpHeaders requestHeaders;

	public Response toResponse(Exception ex) {
		System.out.println(ex.getMessage() + ex.getCause());
		return ResponseCreator.error(500, CustomError.SERVER_ERROR);
	}
}

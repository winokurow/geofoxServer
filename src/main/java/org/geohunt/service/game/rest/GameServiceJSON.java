package org.geohunt.service.game.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.geohunt.service.game.entities.Game;
import org.geohunt.service.rest.responce.ResponseCreator;

public class GameServiceJSON {

	// for retrieving request headers from context
	// an injectable interface that provides access to HTTP header information.
	@Context
	private HttpHeaders requestHeaders;

	private String getHeaderVersion() {
		return requestHeaders.getRequestHeader("version").get(0);
	}

	// Create new game
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response createGame(Game game) {
		return ResponseCreator.success(
				getHeaderVersion(),
				String.format(" 1 Success: %s %s %s %s", game.getTyp(),
						game.getName(), game.getPassword(), game.getUser()));
		// Customer updCustomer = customersDAO.updateCustomer(customer);
		// if (updCustomer != null) {
		// return ResponseCreator.success(getHeaderVersion(), updCustomer);
		// } else {
		// return ResponseCreator.error(500, Error.SERVER_ERROR.getCode(),
		// getHeaderVersion());
		// }
	}
}

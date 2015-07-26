package org.geohunt.service.game.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.geohunt.service.game.dao.IGameDAO;
import org.geohunt.service.game.entities.Game;
import org.geohunt.service.game.rest.exceptions.CustomError;
import org.geohunt.service.rest.responce.ResponseCreator;

public class GameServiceJSON implements IGameService {

	// for retrieving request headers from context
	// an injectable interface that provides access to HTTP header information.
	@Context
	private HttpHeaders requestHeaders;

	// link to our dao object
	private IGameDAO gameDAO;

	// for customersDAO bean property injection
	public IGameDAO getGameDAO() {
		return gameDAO;
	}

	public void setGameDAO(IGameDAO gameDAO) {
		this.gameDAO = gameDAO;
	}

	private String getHeaderVersion() {
		return requestHeaders.getRequestHeader("version").get(0);
	}

	// Create new game
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createGame(Game game) {
		Properties prop = new Properties();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("main.properties");

		try {
			prop.load(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!(getHeaderVersion().equals(prop.get("service.version")))) {
			return ResponseCreator.error(5002, CustomError.OLD_VERSION_ERROR);
		}
		UUID uuid = gameDAO.createGame(game);
		if (uuid != null) {
			return ResponseCreator.success(getHeaderVersion(), String.format("{gameid:'%s'}", uuid));
		} else {
			return ResponseCreator.error(5003, CustomError.GAME_EXISTS);
		}
		// Customer updCustomer = customersDAO.updateCustomer(customer);
		// if (updCustomer != null) {
		// return ResponseCreator.success(getHeaderVersion(), updCustomer);
		// } else {
		// return ResponseCreator.error(500, Error.SERVER_ERROR.getCode(),
		// getHeaderVersion());
		// }
	}
}

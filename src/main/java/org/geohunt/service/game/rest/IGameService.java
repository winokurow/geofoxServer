package org.geohunt.service.game.rest;

import javax.ws.rs.core.Response;

public interface IGameService {
	Response createCustomers(String typ, String gameName, String gamePassword,
			String gameUser);
}

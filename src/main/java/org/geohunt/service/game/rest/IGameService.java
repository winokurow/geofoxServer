package org.geohunt.service.game.rest;

import javax.ws.rs.core.Response;

import org.geohunt.service.game.entities.Game;

public interface IGameService {
	Response createGame(Game game);
}

package org.geohunt.service.game.dao;

import java.util.UUID;

import org.geohunt.service.game.entities.Game;

public interface IGameDAO {
	UUID createGame(Game game);
}

/**
 * GameServiceJSON
 * @author Ilja.Winokurow
 */
package org.geohunt.service.game.rest;

import org.geohunt.service.game.entities.GameData;

import javax.ws.rs.core.Response;

/**
 * IGameService.
 */
public interface IGameService {

  /**
   * Create new game.
   *
   * @param game
   *          data
   * @return responce
   */
  Response createGame(GameData game);
}

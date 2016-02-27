/**
 * GameServiceJSON
 * @author Ilja.Winokurow
 */
package org.geohunt.service.game.rest;

import org.geohunt.service.game.entities.GameData;
import org.geohunt.service.game.entities.MemberData;

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
   * @return response
   */
  Response createGame(GameData game);

  /**
   * Write a position.
   *
   * @param game
   *          - data
   * @return response
   */
  Response writePosition(MemberData game);
}

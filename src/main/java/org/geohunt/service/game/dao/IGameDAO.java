/**
 * Game DAO
 * @author Ilja.Winokurow
 */
package org.geohunt.service.game.dao;

import org.geohunt.service.game.entities.GameData;

/**
 * IGameDAO.
 */
public interface IGameDAO {

  /**
   * createGame. insert data in DB
   *
   * @param game
   *          - data
   *
   * @return session id
   */
  String createGame(GameData game);

  /**
   * joinGame. insert data in DB
   *
   * @param game
   *          - data
   *
   * @return session id
   */
  String joinGame(GameData game);
}

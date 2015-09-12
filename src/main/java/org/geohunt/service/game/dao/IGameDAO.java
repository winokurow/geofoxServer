/**
 * Game DAO
 * @author Ilja.Winokurow
 */
package org.geohunt.service.game.dao;

import org.geohunt.service.game.entities.GameData;
import org.geohunt.service.game.entities.PositionData;

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

  /**
   * writePosition. insert position in DB
   *
   * @param position
   *          - data
   *
   * @return session id
   */
  String writePosition(PositionData position);

  /**
   * isSessionIdExist
   *
   * Verify if Session Id exists.
   *
   * @param sessionId
   *          - session id
   *
   * @return is exists
   */
  boolean isSessionIdExist(String sessionId);
}

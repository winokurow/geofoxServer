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
  String writePosition(final String sessionId, PositionData position);

  /**
   * getFoxPosition.
   *
   * Read fox position.
   *
   * @param sessionId
   *          - session id
   *
   *
   * @return position - fox position
   */
  PositionData getFoxPosition(final String sessionId);

  /**
   * getMemberId
   *
   * Verify if Session Id exists.
   *
   * @param sessionId
   *          - session id
   *
   * @return member id
   */
  int getMemberId(String sessionId);

  /**
   * setDistance
   *
   * Set distance.
   *
   * @param sessionId
   *          - session id
   * @param distance
   *          - distance
   */
  void setDistance(String sessionId, double distance);
}

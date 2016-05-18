/**
 * Game DAO
 * @author Ilja.Winokurow
 */
package org.geohunt.service.game.dao;

import org.geohunt.service.game.data.MemberTyp;
import org.geohunt.service.game.data.Status;
import org.geohunt.service.game.entities.Game;
import org.geohunt.service.game.entities.GameData;
import org.geohunt.service.game.entities.MemberData;

import java.util.ArrayList;

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
   * @param memberid
   *          - member id
   * @param data
   *          to set
   * @return session id
   */
  String writePosition(final int memberid, MemberData data);

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
  MemberData getFoxPosition(final String sessionId);

  /**
   * getUsername.
   *
   * Read username.
   *
   * @param memberId
   *          - member id
   *
   * @return username
   */
  String getUsername(final int memberId);

  /**
   * getHuntersPosition.
   *
   * Read hunters position.
   *
   * @param sessionId
   *          - session id
   *
   *
   * @return position - hunters position
   */
  ArrayList<MemberData> getHuntersPosition(final String sessionId);

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
   * releaseMember
   *
   * Set release member.
   *
   * @param sessionId
   *          - session id
   */
  void releaseMember(String sessionId);

  /**
   * setGameStatus
   *
   * Set status.
   *
   * @param sessionId
   *          - session id
   * @param status
   *          - status
   */
  void setGameStatus(String sessionId, Status status);

  /**
   * getGameType
   *
   * Get game type.
   *
   * @param sessionId
   *          - session id
   *
   * @return type - type
   */
  int getGameType(String sessionId);

  /**
   * Get game data.
   *
   * @param sessionId
   *          - session id
   *
   * @return game data
   */
  Game getGame(String sessionId);

  /**
   * getGameStatus
   *
   * Get status.
   *
   * @param sessionId
   *          - session id
   * @return status - status
   */
  Status getGameStatus(String sessionId);

  /**
   * getMemberType
   *
   * Get member type.
   *
   * @param memberId
   *          - member id
   *
   * @return member type
   */
  MemberTyp getMemberType(int memberId);

}

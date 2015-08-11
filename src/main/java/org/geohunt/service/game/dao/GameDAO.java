/**
 * Game DAO
 * @author Ilja.Winokurow
 */
package org.geohunt.service.game.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geohunt.service.game.common.CommonUtils;
import org.geohunt.service.game.entities.GameData;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

/**
 * Game DAO.
 *
 * @author Ilja.Winokurow
 */
public class GameDAO implements IGameDAO {

  /**
   * JDBC template.
   */
  private JdbcTemplate templGame;

  /**
   * Insert game request.
   */
  private SimpleJdbcInsert insertGame;

  /**
   * Insert member request.
   */
  private SimpleJdbcInsert insertMember;

  /**
   * Insert person request.
   */
  private SimpleJdbcInsert insertPerson;

  /**
   * Search for game.
   */
  private final String sql1 = "SELECT id FROM games WHERE name = ?";

  /**
   * Search for user.
   */
  private final String sql2 = "SELECT id FROM users WHERE name = ?";

  /**
   * Search for active members.
   */
  private final String sql3 = "SELECT count(*) FROM test.members INNER JOIN test.games"
      + " where members.gameid = games.id and members.userid=? and games.typ=1";

  /**
   * Search for password.
   */
  private final String sql4 = "SELECT password FROM games WHERE name = ?";

  /**
   * logger.
   */
  private static final Logger LOGGER = LogManager.getLogger("GameDAO");

  /**
   * setDataSource.
   *
   * @param newdataSource
   *          - new data source
   */
  public final void setDataSource(final DataSource newdataSource) {
    this.templGame = new JdbcTemplate(newdataSource);
    this.insertGame = new SimpleJdbcInsert(newdataSource).withTableName("games");
    this.insertMember = new SimpleJdbcInsert(newdataSource).withTableName("members");
    this.insertPerson = new SimpleJdbcInsert(newdataSource).withTableName("users");
  }

  /**
   * searchForGameId.<br>
   * Search for game ID by game name in DB<br>
   *
   * @param name
   *          - game name
   * @return game id
   */
  private int searchForGameId(final String name) {

    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Name has no content.");
    }

    int gameid = 0;
    try {
      gameid = templGame.queryForObject(sql1, new Object[] { name }, Integer.class);
    } catch (final EmptyResultDataAccessException e) {
      LOGGER.error(e.getMessage());
    }
    return gameid;
  }

  /**
   * searchForGamePassword.<br>
   * Search for game Password by game name in DB<br>
   *
   * @param name
   *          - game name
   * @return game password
   */
  private String searchForGamePassword(final String name) {

    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Name has no content.");
    }

    String password = "";
    try {
      password = templGame.queryForObject(sql4, new Object[] { name }, String.class);
    } catch (final EmptyResultDataAccessException e) {
      LOGGER.error(e.getMessage());
    }
    return password;
  }

  /**
   * getUserId.
   *
   * Search in DB for user ID by user name
   *
   * @param name
   *          - user name
   *
   * @return user id
   */
  private int searchForUserId(final String name) {

    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Name has no content.");
    }

    int userid = 0;
    try {
      userid = templGame.queryForObject(sql2, new Object[] { name }, Integer.class);
    } catch (final EmptyResultDataAccessException e) {
      LOGGER.error(e.getMessage());
    }
    return userid;
  }

  /**
   * searchForActiveMember.
   *
   * Search in DB for active members
   *
   * @param userid
   *          - user id
   *
   * @return member id
   */
  private int searchForActiveMember(final int userid) {
    if (userid == 0) {
      throw new IllegalArgumentException("Name has no content.");
    }

    int members = 0;
    try {
      members = templGame.queryForObject(sql3, new Object[] { userid }, Integer.class);
    } catch (final EmptyResultDataAccessException e) {
      LOGGER.error(e.getMessage());
    }
    return members;
  }

  /**
   * fillUserTable.
   *
   * Insert a record in the table 'users'.
   *
   * @param name
   *          - user name
   * @return user id
   * @throws Exception
   */
  private int insertUser(final String name) {

    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Name has no content.");
    }

    int id = -1;
    final Map<String, Object> parameters = new ConcurrentHashMap<String, Object>(1);
    parameters.put("name", name);
    insertPerson.execute(parameters);
    id = searchForUserId(name);

    return id;
  }

  /**
   * fillGameTable.<br>
   * Insert a record in the table 'games'.
   *
   * @param gameName
   *          - game name
   * @param type
   *          - game type
   * @param password
   *          - game password
   * @param status
   *          - game status
   * @param gameend-
   *          game End
   * @return game id
   */
  private int insertGame(final String gameName, final int type, final String password, final int status,
      final Date gameend) {

    if (gameName == null || gameName.isEmpty()) {
      throw new IllegalArgumentException("Game name has no content.");
    }
    if (password == null || password.isEmpty()) {
      throw new IllegalArgumentException("Password has no content.");
    }
    if (gameend == null) {
      throw new IllegalArgumentException("Game end has no content.");
    }

    int id = -1;
    final Map<String, Object> parameters = new ConcurrentHashMap<String, Object>();
    parameters.put("name", gameName);
    parameters.put("typ", type);
    parameters.put("password", password);
    parameters.put("status", status);
    parameters.put("gameend", gameend);
    insertGame.execute(parameters);
    id = searchForGameId(gameName);
    return id;
  }

  /**
   * insertIntoMemberTable.
   *
   * Insert a record in the table 'members'.
   *
   * @param sessionid
   *          - session id
   * @param type
   *          - member type
   * @param coordx
   *          - x coordinate
   * @param coordy
   *          - y coordinate
   * @param speed
   *          - speed
   * @param userid
   *          - user id
   * @param gameid
   *          - game id
   * @param lastupdate
   *          - last update
   */
  private void insertMember(final UUID sessionid, final int type, final int coordx, final int coordy, final int speed,
      final int userid, final int gameid, final Date lastupdate) {
    if (sessionid == null) {
      throw new IllegalArgumentException("Session id has no content.");
    }
    if (lastupdate == null) {
      throw new IllegalArgumentException("Lastupdate has no content.");
    }
    if (userid < 1) {
      throw new IllegalArgumentException("User id is incorrect.");
    }
    if (gameid < 1) {
      throw new IllegalArgumentException("Game id is incorrect.");
    }

    final Map<String, Object> parameters = new ConcurrentHashMap<String, Object>();
    parameters.put("sessionid", sessionid);
    parameters.put("typ", type);
    parameters.put("coordx", coordx);
    parameters.put("coordy", coordy);
    parameters.put("speed", speed);
    parameters.put("userid", userid);
    parameters.put("gameid", gameid);
    parameters.put("lastupdate", lastupdate);
    insertMember.execute(parameters);
  }

  /**
   * createGame. insert data in DB
   *
   * @param game
   *          - data
   *
   * @return response
   */
  @Override
  public final String createGame(final GameData game) {
    String returnValue = "ERROR_ERR0";
    if (game != null) {

      Integer gameid = searchForGameId(game.getName());

      if (gameid == 0) {

        // Create a new user if user doesn't exist
        int userid = searchForUserId(game.getUser());
        int activeMembers = 0;
        if (userid == 0) {
          userid = insertUser(game.getUser());
        } else {
          activeMembers = searchForActiveMember(userid);
        }
        if (activeMembers == 0) {
          final String typ = game.getTyp().trim();
          final String password = CommonUtils.getHash(game.getPassword());
          gameid = insertGame(game.getName(), Integer.parseInt(typ), password, 1,
              new java.sql.Date(game.getEndGameDate().getTime()));
          final UUID idOne = UUID.randomUUID();
          insertMember(idOne, 1, 0, 0, 0, userid, gameid, new java.sql.Date(new Date().getTime()));
          returnValue = idOne.toString();
        } else {
          returnValue = "ERROR_ERR1";
        }
      }
    }

    return returnValue;
  }

  /**
   * joinGame. insert data in DB
   *
   * @param game
   *          - data
   *
   * @return session id
   */
  @Override
  public final String joinGame(final GameData game) {
    String returnValue = "ERROR_ERR0";
    if (game != null) {

      final int gameid = searchForGameId(game.getName());
      final String passwordMust = searchForGamePassword(game.getName());
      final String password = CommonUtils.getHash(game.getPassword());
      if ((gameid != 0) && password.equals(passwordMust)) {
        Integer userid = searchForUserId(game.getUser());

        // Create a new user if user doesn't exist
        int activeMembers = 0;
        if (userid == 0) {
          userid = insertUser(game.getUser());
        } else {
          activeMembers = searchForActiveMember(userid);
        }
        if (activeMembers == 0) {

          final UUID idOne = UUID.randomUUID();
          insertMember(idOne, 2, 0, 0, 0, userid, gameid, new java.sql.Date(new Date().getTime()));
          returnValue = idOne.toString();
        } else {
          returnValue = "ERROR_ERR1";
        }
      }
    }
    return returnValue;
  }

}

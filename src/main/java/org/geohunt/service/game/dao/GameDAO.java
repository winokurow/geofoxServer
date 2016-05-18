/**
 * Game DAO
 * @author Ilja.Winokurow
 */
package org.geohunt.service.game.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geohunt.service.game.common.CommonUtils;
import org.geohunt.service.game.data.MemberTyp;
import org.geohunt.service.game.data.Status;
import org.geohunt.service.game.entities.Game;
import org.geohunt.service.game.entities.GameConfig;
import org.geohunt.service.game.entities.GameData;
import org.geohunt.service.game.entities.MemberData;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
   * Insert position request.
   */
  private SimpleJdbcInsert insertPosition;

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
   * Search for session id.
   */
  private final String sql5 = "SELECT id FROM members WHERE sessionid = ?";

  /**
   * Search for member type.
   */
  private final String sql51 = "SELECT typ FROM members WHERE id = ?";

  /**
   * Search for gameid.
   */
  private final String sql6 = "SELECT gameid FROM members WHERE id = ?";

  /**
   * Search for fox memberid.
   */
  private final String sql7 = "SELECT id FROM members WHERE gameid = ? AND typ = 1";

  /**
   * Search for all hunters.
   */
  private final String sqlSearchForAllHunters = "SELECT id FROM members WHERE gameid = ? AND typ = 2";

  /**
   * Search for hunters memberid.
   */
  private final String sql71 = "SELECT id FROM members WHERE gameid = ? AND typ = 2 AND id != ?";

  /**
   * GET coordinates.
   */
  private final String sql8 = "SELECT latitude, longitude, accuracy, speed, altitude, timestamp FROM position at1 WHERE memberid = ? AND "
      + "timestamp = (SELECT MAX(timestamp) from position at2 where at1.memberid = at2.memberid)";

  /**
   * Add status.
   */
  private final String sql9 = "update games set status = ? WHERE id = ?";

  /**
   * Get status.
   */
  private final String sql10 = "select status from games WHERE id = ?";

  /**
   * Get game type.
   */
  private final String sql10a = "select typ from games WHERE id = ?";

  /**
   * Get member status.
   */
  private final String sql11 = "select status from members WHERE id = ?";

  /**
   * Add member status.
   */
  private final String sql12 = "update members set status = ? WHERE id = ?";

  /**
   * Get username.
   */
  private final String sql14 = "select name from users, members WHERE users.id = members.userid AND members.id=?";

  /**
   * Get all game data.
   */
  private final String sqlSelectGameData = "SELECT id, typ, name, password, gamebegin, status FROM games WHERE id = ?";

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
    this.insertPosition = new SimpleJdbcInsert(newdataSource).withTableName("position");
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
   * searchForGameIdByMemberId.<br>
   *
   * Search for game ID by member id in DB<br>
   *
   * @param memberid
   *          - member id
   *
   * @return game id
   */
  private int searchForGameId(final int memberid) {

    if (memberid < 1) {
      throw new IllegalArgumentException("Member id is incorrect.");
    }

    int gameid = 0;
    try {
      gameid = templGame.queryForObject(sql6, new Object[] { memberid }, Integer.class);
    } catch (final EmptyResultDataAccessException e) {
      LOGGER.error(e.getMessage());
    }
    return gameid;
  }

  /**
   * searchForUserName.<br>
   *
   * Search for username by member id in DB<br>
   *
   * @param memberid
   *          - member id
   *
   * @return username
   */
  private String searchForUserName(final int memberid) {

    if (memberid < 1) {
      throw new IllegalArgumentException("Member id is incorrect.");
    }

    String username = "";
    try {
      username = templGame.queryForObject(sql14, new Object[] { memberid }, String.class);
    } catch (final EmptyResultDataAccessException e) {
      LOGGER.error(e.getMessage());
    }
    return username;
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
   * getFoxMemberId.
   *
   * Get fox memberid
   *
   * @param gameid
   *          - game id
   *
   * @return member id
   */
  private int getFoxMemberId(final int gameid) {
    if (gameid < 1) {
      throw new IllegalArgumentException("Game id is incorrect");
    }

    int members = 0;
    try {
      members = templGame.queryForObject(sql7, new Object[] { gameid }, Integer.class);
    } catch (final EmptyResultDataAccessException e) {
      LOGGER.error(e.getMessage());
    }
    return members;
  }

  /**
   * getFoxMemberId.
   *
   * Get hunters memberid
   *
   * @param gameid
   *          - game id
   *
   * @param memberid
   *          - member to exclude
   *
   * @return members id
   */
  private List<Integer> getHuntersMemberId(final int gameid, final int memberid) {
    if (gameid < 1) {
      throw new IllegalArgumentException("Game id is incorrect");
    }

    List<Integer> members = null;
    try {
      members = templGame.queryForList(sql71, new Object[] { gameid, memberid }, Integer.class);
    } catch (final Exception e) {
      LOGGER.error(e.getMessage());
    }
    return members;
  }

  /**
   * getFoxMemberId.
   *
   * Get hunters memberid
   *
   * @param gameid
   *          - game id
   *
   * @return members id
   */
  private List<Integer> getHunters(final int gameid) {
    if (gameid < 1) {
      throw new IllegalArgumentException("Game id is incorrect");
    }

    List<Integer> members = null;
    try {
      members = templGame.queryForList(sqlSearchForAllHunters, new Object[] { gameid }, Integer.class);
    } catch (final Exception e) {
      LOGGER.error(e.getMessage());
    }
    return members;
  }

  /**
   * getLastCoordinates.
   *
   * Get last coordinates.
   *
   * @param memberid
   *          - member id
   *
   * @return position
   */
  private MemberData getLastCoordinates(final int memberid) {
    if (memberid < 1) {
      throw new IllegalArgumentException("Member id is incorrect");
    }

    MemberData position = new MemberData();
    position.setAccuracy(0.00);
    position.setAltitude(0.00);
    position.setLongitude(0.00);
    position.setLatitude(0.00);
    position.setSpeed(0.00);
    try {
      position = templGame.queryForObject(sql8, new Object[] { memberid }, new RowMapper<MemberData>() {
        @Override
        public MemberData mapRow(final ResultSet rs, final int rowNumber) throws SQLException {
          final MemberData dept = new MemberData();
          dept.setLongitude(rs.getDouble("latitude"));
          dept.setLatitude(rs.getDouble("longitude"));
          dept.setAccuracy(rs.getDouble("accuracy"));
          dept.setAltitude(rs.getDouble("altitude"));
          dept.setSpeed(rs.getDouble("speed"));

          final Date date = rs.getTimestamp("timestamp");
          final Date dateNow = new Date(System.currentTimeMillis() - 5 * 60 * 1000);
          if (dateNow.after(date)) {
            setMemberStatus(memberid, 1);
          }
          return dept;
        }
      });
    } catch (final Exception e) {
      LOGGER.error(e.getMessage());
    }
    return position;
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
   * @param gamebegin
   *          - game begin
   * @return game id
   */
  private int insertGame(final String gameName, final int type, final String password, final Status status,
      final Date gamebegin) {

    if (gameName == null || gameName.isEmpty()) {
      throw new IllegalArgumentException("Game name has no content.");
    }
    if (password == null || password.isEmpty()) {
      throw new IllegalArgumentException("Password has no content.");
    }
    if (gamebegin == null) {
      throw new IllegalArgumentException("Game end has no content.");
    }

    int id = -1;
    final Map<String, Object> parameters = new ConcurrentHashMap<String, Object>();
    parameters.put("name", gameName);
    parameters.put("typ", type);
    parameters.put("password", password);
    parameters.put("status", status.toString());
    parameters.put("gamebegin", gamebegin);
    insertGame.execute(parameters);
    id = searchForGameId(gameName);
    return id;
  }

  /**
   * insertPosition.<br>
   * Insert a record in the table 'coordinates'.
   *
   * @param latitude
   *          - latitude
   * @param longitude
   *          - longitude
   * @param accuracy
   *          - accuracy
   * @param speed
   *          - speed
   * @param altitude
   *          - altitude
   * @param memberid
   *          - member id
   * @param timestamp
   *          - timestamp
   *
   * @return code
   */
  private void insertPosition(final double latitude, final double longitude, final double accuracy, final double speed,
      final double altitude, final int memberid, final Timestamp timestamp) {

    if ((latitude == 0.00) || (longitude == 0.00)) {
      throw new IllegalArgumentException("Position is incorrect.");
    }
    if (memberid == 0) {
      throw new IllegalArgumentException("Member has no content.");
    }
    if (timestamp == null) {
      throw new IllegalArgumentException("Timestamp has no content.");
    }

    final Map<String, Object> parameters = new ConcurrentHashMap<String, Object>();
    parameters.put("latitude", latitude);
    parameters.put("longitude", longitude);
    parameters.put("accuracy", accuracy);
    parameters.put("speed", speed);
    parameters.put("altitude", altitude);
    parameters.put("memberid", memberid);
    parameters.put("timestamp", timestamp);
    final int i = insertPosition.execute(parameters);
    LOGGER.info(i);
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
   * @param userid
   *          - user id
   * @param gameid
   *          - game id
   */
  private void insertMember(final UUID sessionid, final int type, final int userid, final int gameid) {
    if (sessionid == null) {
      throw new IllegalArgumentException("Session id has no content.");
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
    parameters.put("userid", userid);
    parameters.put("gameid", gameid);
    parameters.put("status", 0);
    final Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    parameters.put("timestamp", new java.sql.Date(cal.getTime().getTime()));

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
          final String typ = game.getGametyp().trim();
          final String password = CommonUtils.getHash(game.getPassword());
          gameid = insertGame(game.getName(), Integer.parseInt(typ), password, Status.AWAITINGFORHUNTER,
              new java.sql.Date(game.getBeginGameTime().getTime()));
          final UUID idOne = UUID.randomUUID();
          insertMember(idOne, 1, userid, gameid);
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
        final List<Integer> hunters = getHunters(gameid);
        final GameConfig gameConfig = new GameConfig(game.getGametyp());
        if (hunters.size() < gameConfig.getHunterscount()) {
          Integer userid = searchForUserId(game.getUser());

          // Create a new user if user doesn't exist
          int activeMembers = 0;
          if (userid == 0) {
            userid = insertUser(game.getUser());
          } else {
            activeMembers = searchForActiveMember(userid);
          }
          if (activeMembers == 1) {

            final UUID idOne = UUID.randomUUID();
            insertMember(idOne, 2, userid, gameid);
            returnValue = idOne.toString();
          } else {
            returnValue = "ERROR_ERR1";
          }
        } else {
          returnValue = "ERROR_ERR3";
        }
      } else {
        returnValue = "ERROR_GAME_IS_FULL";
      }
    }
    return returnValue;
  }

  /**
   * writePosition. insert position in DB
   *
   * @param memberid
   *          - member id
   * @param data
   *          to set
   * @return session id
   */
  @Override
  public final String writePosition(final int memberid, final MemberData position) {
    String returnValue = "ERROR_ERR0";
    if (position != null) {
      if (memberid != 0) {
        LOGGER.info(position.getLongitude());
        insertPosition(position.getLongitude(), position.getLatitude(), position.getAccuracy(), position.getSpeed(),
            position.getAltitude(), memberid, new Timestamp(new Date().getTime()));
        returnValue = "";
      } else {

        returnValue = "ERROR_ERR1";
      }
    } else {
      LOGGER.error("position data is empty");
    }
    return returnValue;
  }

  @Override
  public int getMemberId(final String sessionId) {
    if (sessionId == null || sessionId.isEmpty()) {
      throw new IllegalArgumentException("Session Id is empty.");
    }

    int memberid = 0;
    try {
      memberid = templGame.queryForObject(sql5, new Object[] { sessionId }, Integer.class);
      LOGGER.info(memberid);
    } catch (final EmptyResultDataAccessException e) {
      LOGGER.error(e.getMessage());
    }
    return memberid;
  }

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
  @Override
  public final String getUsername(final int memberId) {
    String username = "";
    if (memberId != 0) {
      username = searchForUserName(memberId);
    }
    return username;
  }

  @Override
  public MemberData getFoxPosition(final String sessionId) {
    MemberData returnValue = null;
    int memberid = getMemberId(sessionId);
    if (memberid != 0) {
      final int gameid = searchForGameId(memberid);
      if (gameid != 0) {
        memberid = getFoxMemberId(gameid);

        if (memberid != 0) {
          returnValue = getLastCoordinates(memberid);
          final String username = searchForUserName(memberid);
          returnValue.setName(username);
          final int status = this.getMemberStatus(memberid);
          if (status == 1) {
            setGameStatus(sessionId, Status.FOX_TIMEOUT);
          }
        }
      }
    }
    return returnValue;
  }

  @Override
  public ArrayList<MemberData> getHuntersPosition(final String sessionId) {
    final ArrayList<MemberData> returnValue = new ArrayList<>();
    final int memberid = getMemberId(sessionId);
    if (memberid != 0) {
      final int gameid = searchForGameId(memberid);
      if (gameid != 0) {
        final List<Integer> membersid = getHuntersMemberId(gameid, memberid);
        for (final Integer member : membersid) {
          if (member != 0) {
            final MemberData positionData = getLastCoordinates(member);
            if ((positionData.getLatitude() != 0.00) && (positionData.getLongitude() != 0.00)) {
              final int status = this.getMemberStatus(memberid);
              if (status == 0) {
                final String username = searchForUserName(memberid);
                positionData.setName(username);
                returnValue.add(positionData);

              }

            }
          }

          if ((returnValue.size() == 0) && (membersid.size() > 0)) {
            setGameStatus(sessionId, Status.HUNTERS_TIMEOUT);
          }
        }
      }
    }

    return returnValue;
  }

  @Override
  public void releaseMember(final String sessionId) {
    if (sessionId == null || sessionId.isEmpty()) {
      throw new IllegalArgumentException("Session Id is empty.");
    }
    final int memberid = getMemberId(sessionId);
    if (memberid != 0) {
      setMemberStatus(memberid, 1);
    }
  }

  @Override
  public void setGameStatus(final String sessionId, final Status status) {
    if (sessionId == null || sessionId.isEmpty()) {
      throw new IllegalArgumentException("Session Id is empty.");
    }
    final int memberid = getMemberId(sessionId);
    if (memberid != 0) {
      final int gameid = searchForGameId(memberid);
      if (gameid != 0) {
        try {
          templGame.update(sql9, status.toString(), gameid);
        } catch (final Exception e) {
          LOGGER.error(e.getMessage());
        }
      }
    }
  }

  @Override
  public Status getGameStatus(final String sessionId) {
    int status = 0;
    if (sessionId == null || sessionId.isEmpty()) {
      throw new IllegalArgumentException("Session Id is empty.");
    }
    final int memberid = getMemberId(sessionId);
    if (memberid != 0) {
      final int gameid = searchForGameId(memberid);
      if (gameid != 0) {
        status = templGame.queryForObject(sql10, new Object[] { gameid }, Integer.class);
      }
    }

    return Status.valueOf(status);
  }

  @Override
  public int getGameType(final String sessionId) {
    int status = 0;
    if (sessionId == null || sessionId.isEmpty()) {
      throw new IllegalArgumentException("Session Id is empty.");
    }
    final int memberid = getMemberId(sessionId);
    if (memberid != 0) {
      final int gameid = searchForGameId(memberid);
      if (gameid != 0) {
        status = templGame.queryForObject(sql10a, new Object[] { gameid }, Integer.class);
      }
    }

    return status;
  }

  public int getMemberStatus(final int memberId) {
    int status = -1;
    if (memberId != 0) {
      status = templGame.queryForObject(sql11, new Object[] { memberId }, Integer.class);
    }

    return status;
  }

  public void setMemberStatus(final int memberId, final int status) {
    if (memberId != 0) {
      try {
        templGame.update(sql12, status, memberId);
      } catch (final Exception e) {
        LOGGER.error(e.getMessage());
      }
    }
  }

  /**
   * Get member type.
   *
   * @param memberId
   *          - member id
   *
   * @return member type
   */
  @Override
  public final MemberTyp getMemberType(final int memberId) {
    int status = 0;
    try {
      status = templGame.queryForObject(sql51, new Object[] { memberId }, Integer.class);
    } catch (final EmptyResultDataAccessException e) {
      LOGGER.error(e.getMessage());
    }
    return MemberTyp.valueOf(status);
  }

  @Override
  public Game getGame(final String sessionId) {
    Game game = new Game();
    if (sessionId == null || sessionId.isEmpty()) {
      throw new IllegalArgumentException("Session Id is empty.");
    }
    final int memberid = getMemberId(sessionId);
    if (memberid != 0) {
      final int gameid = searchForGameId(memberid);
      if (gameid != 0) {
        game = (Game) templGame.queryForObject(sqlSelectGameData, new Object[] { gameid }, new GameRowMapper());
      }

    }
    return game;
  }
}

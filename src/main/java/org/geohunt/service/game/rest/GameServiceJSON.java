/**
 * GameServiceJSON
 * @author Ilja.Winokurow
 */
package org.geohunt.service.game.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geohunt.service.game.dao.IGameDAO;
import org.geohunt.service.game.entities.GameData;
import org.geohunt.service.game.entities.PositionData;
import org.geohunt.service.game.rest.exceptions.CustomError;
import org.geohunt.service.rest.responce.ResponseCreator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * GameServiceJSON.
 */
public class GameServiceJSON implements IGameService {

  /**
   * logger.
   */
  private final static Logger LOGGER = LogManager.getLogger("GameServiceJSON");

  /**
   * for retrieving request headers from context an injectable interface that
   * provides access to HTTP header information.
   */
  @Context
  private HttpHeaders requestHeaders;

  /**
   * link to our dao object.
   */
  private IGameDAO gameDAO;

  /**
   * For customersDAO bean property injection.
   *
   * @return customersDAO
   */
  public final IGameDAO getGameDAO() {
    return gameDAO;
  }

  /**
   * setGameDAO.
   *
   * @param newgameDAO
   *          - customersDAO
   */
  public final void setGameDAO(final IGameDAO newgameDAO) {
    this.gameDAO = newgameDAO;
  }

  /**
   * getHeaderVersion.
   *
   * @return version
   */
  private String getHeaderVersion() {
    final List<String> version = requestHeaders.getRequestHeader("version");
    String returnValue = "xxx";
    if (version != null) {
      returnValue = version.get(0);
    }
    return returnValue;
  }

  /**
   * getHeaderSessionId.
   *
   * @return Session Id
   */
  private String getHeaderSessionId() {
    final List<String> version = requestHeaders.getRequestHeader("sessionid");
    String returnValue = "xxx";
    if (version != null) {
      returnValue = version.get(0);
    }
    return returnValue;
  }

  /**
   * Create new game.
   *
   * @param game
   *          data
   * @return response
   */
  @Override
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  public final Response createGame(final GameData game) {
    Response returnResponse;

    final String version = getHeaderVersion();
    final Properties prop = new Properties();
    final InputStream inputStream = Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("main.properties");
    try {
      prop.load(inputStream);
    } catch (final IOException e) {
      LOGGER.error(e.getMessage());
    }
    if (!(version.equals(prop.get("service.version")))) {
      return ResponseCreator.error(CustomError.OLD_VERSION_ERROR);

    }

    if (game.getName() == null || game.getName().isEmpty()) {
      return ResponseCreator.error("Game name has no content.");
    }
    if (game.getPassword() == null || game.getPassword().isEmpty()) {
      return ResponseCreator.error("Password has no content.");
    }
    if ((game.getUser() == null) || (game.getUser().isEmpty())) {
      return ResponseCreator.error("User name has no content.");
    }

    switch (game.getTyp()) {
    default:
      final Calendar cal = Calendar.getInstance();
      cal.setTime(new Date());
      cal.add(Calendar.HOUR, 2);
      game.setEndGameDate(cal.getTime());

    }

    String uuid;
    if (game.getTyp().equals("0")) {
      uuid = gameDAO.joinGame(game);
    } else {
      uuid = gameDAO.createGame(game);
    }
    if (uuid.contains("ERROR")) {
      returnResponse = ResponseCreator.error(uuid);
    } else {
      returnResponse = ResponseCreator.success(getHeaderVersion(), String.format("{gameid:'%s'}", uuid));
    }

    // Customer updCustomer = customersDAO.updateCustomer(customer);
    // if (updCustomer != null) {
    // return ResponseCreator.success(getHeaderVersion(), updCustomer);
    // } else {
    // return ResponseCreator.error(500, Error.SERVER_ERROR.getCode(),
    // getHeaderVersion());
    // }
    return returnResponse;
  }

  /**
   * Create new game.
   *
   * @param game
   *          data
   * @return response
   */
  @Override
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public final Response writePosition(final PositionData game) {
    Response returnResponse;

    final String sessionId = getHeaderSessionId();
    if (!(gameDAO.isSessionIdExist(sessionId))) {
      return ResponseCreator.error(CustomError.NOT_AUTHORIZED);
    }

    final String version = getHeaderVersion();
    final Properties prop = new Properties();
    final InputStream inputStream = Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("main.properties");
    try {
      prop.load(inputStream);
    } catch (final IOException e) {
      LOGGER.error(e.getMessage());
    }
    if (!(version.equals(prop.get("service.version")))) {
      return ResponseCreator.error(CustomError.OLD_VERSION_ERROR);
    }

    if (game.getCoordx() == 0.00 || game.getCoordy() == 0.00) {
      return ResponseCreator.error("Coordinates ist corrupted.");
    }

    final String uuid = gameDAO.writePosition(game);

    if (uuid.contains("ERROR")) {
      returnResponse = ResponseCreator.error(uuid);
    } else {
      returnResponse = ResponseCreator.success(getHeaderVersion(), "Position is transfered successfully.");
    }

    return returnResponse;
  }
}

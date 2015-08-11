/**
 * CustomExceptionMapper
 * @author Ilja.Winokurow
 */
package org.geohunt.service.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geohunt.service.game.rest.exceptions.CustomError;
import org.geohunt.service.rest.responce.ResponseCreator;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * CustomExceptionMapper.
 */
public class CustomExceptionMapper implements ExceptionMapper<Exception> {

  /**
   * logger.
   */
  private final static Logger LOGGER = LogManager.getLogger("CustomExceptionMapper");

  // /**
  // * Headers.
  // */
  // @Context
  // private HttpHeaders requestHeaders;

  /**
   * toResponse.
   *
   * @param exception
   *          - exception
   * @return Response
   */
  @Override
  public final Response toResponse(final Exception exception) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(exception.getMessage() + exception.getCause());
    }
    return ResponseCreator.error(CustomError.SERVER_ERROR);
  }
}

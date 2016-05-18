/**
 * ResponseCreator
 * @author Ilja.Winokurow
 */
package org.geohunt.service.rest.responce;

import org.geohunt.service.game.rest.exceptions.CustomError;

import javax.ws.rs.core.Response;

/**
 * ResponseCreator.
 */
public final class ResponseCreator {

  /**
   * Constructor.
   */
  private ResponseCreator() {
    super();
  }

  /**
   * error message.
   *
   * @param error
   *          - error
   *
   * @return response
   */
  public static Response error(final CustomError error) {
    final Response.ResponseBuilder response = Response.status(500);
    response.header("errorcode", error.getCode());
    response.header("error", error.getDescription());
    response.entity("none");
    return response.build();
  }

  /**
   * error message.
   *
   * @param errorText
   *          - error text
   *
   * @return response
   */
  public static Response error(final String errorText) {
    final Response.ResponseBuilder response = Response.status(500);
    response.header("errorcode", "6003");
    response.header("error", errorText);
    response.entity("none");
    return response.build();
  }

  /**
   * success message.
   *
   * @param version
   *          - version
   * @param object
   *          - body
   * @return response
   */
  public static Response success(final String version, final Object object) {
    final Response.ResponseBuilder response = Response.ok();
    response.header("version", version);
    if (object == null) {
      response.entity("none");
    } else {

      response.entity(object);
    }
    return response.build();
  }
}

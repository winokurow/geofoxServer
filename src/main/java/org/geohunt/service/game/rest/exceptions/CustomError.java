/**
 * CustomError
 * @author Ilja.Winokurow
 */
package org.geohunt.service.game.rest.exceptions;

/**
 * Errors enum.
 */
public enum CustomError {
  /**
   * Error 'Not Authorized'.
   */
  NOT_AUTHORIZED(4010, "Not Authorized"),

  /**
   * Error 'Forbidden'.
   */
  FORBIDDEN(4030, "Forbidden"),

  /**
   * Error 'Server is not found.'.
   */
  NOT_FOUND(4040, "Server is not found. Please connect the support for details."),

  /**
   * Error 'Something goes wrong. '.
   */
  SERVER_ERROR(5001, "Something goes wrong. Please connect the support for details."),

  /**
   * Error 'The version of game client is incorrect .'.
   */
  OLD_VERSION_ERROR(6002, "The version of game client is incorrect. Please update the client to play."),

  /**
   * Error 'The game with those name is already exists . ' .
   */
  GAME_EXISTS(6003, "The game with those name is already exists. Please enter other name.");

  /**
   * Error code.
   */
  private final int code;

  /**
   * Error description.
   */
  private final String description;

  /**
   * Constructor.
   *
   * @param newcode
   *          - code
   * @param newdescription
   *          - description.
   */
  private CustomError(final int newcode, final String newdescription) {
    this.code = newcode;
    this.description = newdescription;
  }

  /**
   * Getter.
   *
   * @return description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Getter.
   *
   * @return code
   */
  public int getCode() {
    return code;
  }

  /**
   * Get error string.
   *
   * @return string.
   */
  @Override
  public String toString() {
    return code + ": " + description;
  }
}

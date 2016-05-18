/**
 * Game Entity
 * @author Ilja.Winokurow
 */
package org.geohunt.service.game.entities;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Game.
 */
@XmlRootElement(name = "game")
public class GameData {

  /**
   * gameExist.
   */
  private String gameExist;

  /**
   * Typ.
   */
  private String gametyp;

  /**
   * Name.
   */

  private String name;

  /**
   * password.
   */
  private String password;

  /**
   * user.
   */
  private String user;

  /**
   * time of begin of the game.
   */
  private Date beginGameTime;

  /**
   * @return the typ
   */
  public final String getGametyp() {
    return gametyp;
  }

  /**
   * @param newtyp
   *          the typ to set
   */
  public final void setGametyp(final String newtyp) {
    this.gametyp = newtyp;
  }

  /**
   * @return the name
   */
  public final String getName() {
    return name;
  }

  /**
   * @param newname
   *          - the name to set
   */
  public final void setName(final String newname) {
    this.name = newname;
  }

  /**
   * @return the password
   */
  public final String getPassword() {
    return password;
  }

  /**
   * @param newpassword
   *          the password to set
   */
  public final void setPassword(final String newpassword) {
    this.password = newpassword;
  }

  /**
   * @return the user
   */
  public final String getUser() {
    return user;
  }

  /**
   * @param newuser
   *          the user to set
   */
  public final void setUser(final String newuser) {
    this.user = newuser;
  }

  /**
   * @return the endGameDate
   */
  public final Date getBeginGameTime() {
    return beginGameTime;
  }

  /**
   * @param endGameDate
   *          the endGameDate to set
   */
  public final void setBeginGameTime(final Date endGameDate) {
    this.beginGameTime = endGameDate;
  }

  /**
   * @return the gameExist
   */
  public String getGameExist() {
    return gameExist;
  }

  /**
   * @param gameExist
   *          the gameExist to set
   */
  public void setGameExist(final String gameExist) {
    this.gameExist = gameExist;
  }

}

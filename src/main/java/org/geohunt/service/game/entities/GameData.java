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
   * Typ.
   */
  private String typ;

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
   * date of end of the game.
   */
  private Date endGameDate;

  /**
   * @return the typ
   */
  public final String getTyp() {
    return typ;
  }

  /**
   * @param newtyp
   *          the typ to set
   */
  public final void setTyp(final String newtyp) {
    this.typ = newtyp;
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
  public Date getEndGameDate() {
    return endGameDate;
  }

  /**
   * @param endGameDate
   *          the endGameDate to set
   */
  public void setEndGameDate(final Date endGameDate) {
    this.endGameDate = endGameDate;
  }

}

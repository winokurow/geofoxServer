/**
 * Entity
 * @author Ilja.Winokurow
 */
package org.geohunt.service.game.entities;

/**
 * Entity.
 *
 * @return Response
 */
public class Entity {

  /**
   * id.
   *
   */
  private String entityId;

  /**
   * getter.
   *
   * @return id
   */
  public final String getId() {
    return entityId;
  }

  /**
   * setter.
   *
   * @param newid
   *          - id
   */
  public final void setId(final String newid) {
    this.entityId = newid;
  }

  /**
   * get hash code.
   *
   * @return hashcode
   */
  @Override
  public final int hashCode() {
    int hash = 0;
    if (entityId != null) {
      hash = entityId.hashCode();
    }
    return 31 * hash;
  }

  /**
   * equals.
   *
   * @return is equal
   */
  @Override
  public final boolean equals(final Object obj) {
    //// boolean returnValue = false;
    //// if (this == obj) {
    //// returnValue= true;
    //// }
    //// if (obj != null) {
    //// if (getClass() == obj.getClass()) {
    //// final Entity other = (Entity) obj;
    //// if (entityId == null) {
    //// if (other.entityId == null) {
    //// return false;
    //// }
    //// } else if (!entityId.equals(other.entityId)) {
    //// return false;
    //// }
    //// }
    //
    // }
    //
    // return returnValue;
    return true;
  }

  /**
   * toString.
   *
   * @return string
   */
  @Override
  public final String toString() {
    return "Entity [id=" + entityId + "]";
  }

}

/* 
 * Copyright (C) 2004 University Corporation for Advanced Internet Development, Inc.
 * Copyright (C) 2004 The University Of Chicago
 * All Rights Reserved. 
 *
 * See the LICENSE file in the top-level directory of the 
 * distribution for licensing information.
 */

package edu.internet2.middleware.grouper;

import java.util.*;


/** 
 * {@link Grouper} Naming Interface.
 *
 * @author  blair christensen.
 * @version $Id: GrouperNaming.java,v 1.20 2004-11-23 19:43:26 blair Exp $
 */
public interface GrouperNaming {

  /**
   * Verify whether this implementation of the {@link GrouperNaming}
   * interface can handle this privilege.
   *
   * @param   priv  The privilege to verify.
   * @return  Boolean true if this implementation handles the specified
   * privilege, boolean false otherwise.
   */
  public boolean can(String priv);

  /**
   * Grant a naming privilege on a {@link GrouperStem}.
   * <p>
   * See implementations for more information.
   *
   * @param   s     Act within this {@link GrouperSession}.
   * @param   stem  Grant privileges on this {@link Grouper} stem.
   * @param   m     Grant privileges for this {@link GrouperMember}.
   * @param   priv  Privilege to grant.
   */
  public boolean grant(GrouperSession s, GrouperStem stem, GrouperMember m, String priv);

  /**
   * List stems where the current subject has the specified privilege.
   * <p>
   * See implementations for more information.
   *
   * @param   s     Act within this {@link GrouperSession}.
   * @param   priv  Query for this privilege type.
   * @return  List of {@link GrouperStem} stems.
   */
  public List has(GrouperSession s, String priv);

  /**
   * List naming privileges for current subject on the specified stem.
   * <p>
   * See implementations for more information.
   *
   * @param   s     Act within this {@link GrouperSession}.
   * @param   stem  List privileges on this stem.
   * @return  List of privileges.
   */
  public List has(GrouperSession s, GrouperStem stem);

  /**
   * List access privileges for specified member on the specified stem.
   * <p>
   * See implementations for more information.
   *
   * @param   s     Act within this {@link GrouperSession}.
   * @param   stem  Return privileges for this {@link Grouper} stem.
   * @param   m     List privileges for this @link GrouperMember}.
   * @return  List of privileges.
   */
  public List has(GrouperSession s, GrouperStem stem, GrouperMember m);

  /**
   * List stems where the specified member has the specified
   * privilege.
   * <p>
   * See implementations for more information.
   *
   * @param   s     Act within this {@link GrouperSession}.
   * @param   m     Query for this {@link GrouperMember}.
   * @param   priv  Query for this privilege type.
   * @return  List of {@link GrouperStem} stems.
   */
  public List has(GrouperSession s, GrouperMember m, String priv);

  /**
   * Verify whether current subject has the specified privilege on the
   * specified stem.
   * <p>
   * See implementations for more information.
   *
   * @param   s     Act within this {@link GrouperSession}.
   * @param   stem  Verify privilege for this stem.
   * @param   priv  Verify this privilege.
   * @return  True if subject has this privilege on the stem.
   */
  public boolean has(GrouperSession s, GrouperStem stem, String priv);

  /**
   * Verify whether the specified member has the specified privilege
   * on the specified stem.
   * <p>
   * See implementations for more information.
   *
   * @param   s     Act within this {@link GrouperSession}.
   * @param   stem  Verify privilege for this stem.
   * @param   m     Verify privilege for this member.
   * @param   priv  Verify this privilege.
   * @return  True if subject has this privilege on the stem.
   */
  public boolean has(GrouperSession s, GrouperStem stem, GrouperMember m, String priv);

  /**
   * Revoke a naming privilege on a {@link Grouper} stem.
   * <p>
   * See implementations for more information.
   *
   * @param   s     Act within this {@link GrouperSession}.
   * @param   stem  Revoke privilege on this {@link Grouper} stem.
   * @param   m     Revoke privilege for this{@link GrouperMember}.
   * @param   priv  Privilege to revoke.
   */
  public boolean revoke(GrouperSession s, GrouperStem stem, GrouperMember m, String priv);

}


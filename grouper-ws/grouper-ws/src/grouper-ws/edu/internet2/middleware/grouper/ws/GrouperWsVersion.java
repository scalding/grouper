/*
 * @author mchyzer $Id: GrouperWsVersion.java,v 1.1 2008-03-24 20:19:49 mchyzer Exp $
 */
package edu.internet2.middleware.grouper.ws;

import org.apache.commons.lang.StringUtils;

import edu.internet2.middleware.grouper.util.GrouperUtil;
import edu.internet2.middleware.grouper.ws.exceptions.WsInvalidQueryException;

/**
 * grouper service version
 */
public enum GrouperWsVersion {

  /**
   * grouper version 1.3, first build
   */
  v1_3_000(true, 1);

  /** current version */
  private static GrouperWsVersion currentVersion = null;

  /** ordered version number to know which is more recent etc */
  @SuppressWarnings("unused")
  private int revision;

  /** 
   * the actual string of the version, not the "name" of the enum
   * typcially will be whatever grouper is, then a build number for
   * web services
   */
  private boolean currentVersionBoolean;

  /**
   * constructor
   * @param theCurrentVersion
   * @param theRevision 
   */
  private GrouperWsVersion(boolean theCurrentVersion, int theRevision) {

    this.currentVersionBoolean = theCurrentVersion;
    this.revision = theRevision;

  }

  /**
   * get the current version
   * @return the current version
   */
  public static GrouperWsVersion currentVersion() {

    //lazyload the current version
    if (currentVersion == null) {
      //find current version
      for (GrouperWsVersion grouperServiceVersion : GrouperWsVersion.values()) {
        //make sure not more than one
        if (currentVersion != null) {
          GrouperUtil.assertion(!grouperServiceVersion.currentVersionBoolean,
              "Cant have more than one current version");
        }
        //we found it
        if (grouperServiceVersion.currentVersionBoolean) {
          currentVersion = grouperServiceVersion;
        }
      }
    }
    //there must be one and only one
    GrouperUtil.assertion(currentVersion != null, "There is no current version!");
    return currentVersion;
  }

  /**
   * do a case-insensitive matching
   * 
   * @param string
   * @param exception on null will not allow null or blank entries
   * @param exceptionOnNull
   * @return the enum or null or exception if not found
   */
  public static GrouperWsVersion valueOfIgnoreCase(String string, boolean exceptionOnNull) {

    //maybe blank is ok
    if (!exceptionOnNull && StringUtils.isBlank(string)) {
      return null;
    }

    //blank is not allowed
    if (!StringUtils.isBlank(string)) {
      for (GrouperWsVersion grouperServiceVersion : GrouperWsVersion.values()) {
        if (StringUtils.equalsIgnoreCase(string, grouperServiceVersion.name())) {
          return grouperServiceVersion;
        }
      }
    }
    StringBuilder error = new StringBuilder(
        "Cant find grouperServiceVersion from string: '").append(string);
    error.append("', expecting one of: ");
    for (GrouperWsVersion grouperServiceVersion : GrouperWsVersion.values()) {
      error.append(grouperServiceVersion.name()).append(", ");
    }
    throw new WsInvalidQueryException(error.toString());
  }

}

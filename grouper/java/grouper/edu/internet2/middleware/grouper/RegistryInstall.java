/*
  Copyright 2004-2005 University Corporation for Advanced Internet Development, Inc.
  Copyright 2004-2005 The University Of Chicago

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package edu.internet2.middleware.grouper;

import  java.util.*;
import  net.sf.hibernate.*;


/** 
 * Install the Groups Registry.
 * <p />
 * @author  blair christensen.
 * @version $Id: RegistryInstall.java,v 1.2 2005-11-11 18:32:07 blair Exp $    
 */
public class RegistryInstall {

  // Public Class Methods

  public static void main(String[] args) {
    // Install group types, fields and privileges
    _installFieldsAndTypes();
    // Install root stem
  } // public static void main(args)


  // Private Class Methods
  private static void _installFieldsAndTypes() {
    Set fields  = new HashSet();
    Set types   = new HashSet();
    Set privs   = new HashSet();
   
    // TODO privs
    // TODO GroupType base    = new GroupType("base");

    Field description = new Field("description",      FieldType.ATTRIBUTE);
    Field displayName = new Field("displayName",      FieldType.ATTRIBUTE);
    Field displayExtn = new Field("displayExtension", FieldType.ATTRIBUTE);
    Field extension   = new Field("extension",        FieldType.ATTRIBUTE);
    Field name        = new Field("name",             FieldType.ATTRIBUTE);

    fields.add(description);
    fields.add(displayName);
    fields.add(displayExtn);
    fields.add(extension);
    fields.add(name);

    Field admins    = new Field("admins",   FieldType.LIST);
    // Not needed?  Or maybe just reserve it?
    Field creators  = new Field("creators", FieldType.LIST);
    Field members   = new Field("members",  FieldType.LIST);
    Field optins    = new Field("optins",   FieldType.LIST);
    Field optouts   = new Field("optouts",  FieldType.LIST);
    Field readers   = new Field("readers",  FieldType.LIST);
    // Not needed?  Or maybe just reserve it?
    Field stemmers  = new Field("stemmers", FieldType.LIST);
    Field updaters  = new Field("updaters", FieldType.LIST);
    Field viewers   = new Field("viewers",  FieldType.LIST);

    fields.add(admins);
    fields.add(creators);
    fields.add(members);
    fields.add(optins);
    fields.add(optouts);
    fields.add(readers);
    fields.add(stemmers);
    fields.add(updaters);
    fields.add(viewers); 

/* TODO
    types.add(base);

    Iterator iter = fields.iterator();
    while (iter.hasNext()) {
      Field f = (Field) iter.next();
      f.getGroupTypes().add(base);
      base.getFields().add(f);
    }
*/

    try {
      Session hs = HibernateHelper.getSession();
      Set objects = new HashSet();
      objects.addAll(fields);
      // TODO objects.addAll(types);
      HibernateHelper.save(objects);
      hs.close();
      System.err.println("fields installed: " + fields.size());
      System.err.println("types installed : " + types.size());
      System.err.println("privs installed : " + privs.size());
    }
    catch (HibernateException eH) {
      throw new RuntimeException(
        "error installing schema: " + eH.getMessage()
      );
    }
  } // private static void _installFieldsAndTypes()

}


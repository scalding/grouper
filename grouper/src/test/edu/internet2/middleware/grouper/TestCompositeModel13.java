/*
  Copyright 2004-2006 University Corporation for Advanced Internet Development, Inc.
  Copyright 2004-2006 The University Of Chicago

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

import  edu.internet2.middleware.subject.*;
import  edu.internet2.middleware.subject.provider.*;
import  java.util.*;
import  junit.framework.*;
import  org.apache.commons.logging.*;

/**
 * @author  blair christensen.
 * @version $Id: TestCompositeModel13.java,v 1.1.2.2 2006-04-20 16:16:57 blair Exp $
 */
public class TestCompositeModel13 extends TestCase {

  // Private Static Class Constants
  private static final Log LOG = LogFactory.getLog(TestCompositeModel13.class);

  public TestCompositeModel13(String name) {
    super(name);
  }

  protected void setUp () {
    LOG.debug("setUp");
    RegistryReset.reset();
  }

  protected void tearDown () {
    LOG.debug("tearDown");
  }

  public void testFailOwnerEqualsLeftFactor() {
    LOG.info("testFailOwnerEqualsLeftFactor");
    try {
      R r = R.populateRegistry(1, 2, 0);
      Composite c0 = new Composite(
        r.rs, r.getGroup("a", "a"), r.getGroup("a", "a"), r.getGroup("a", "b"), CompositeType.UNION
      );
      r.rs.stop();
      Assert.fail("created composite where left == owner");
    }
    catch (ModelException eM) {
      Assert.assertTrue("OK: cannot create composites where left == owner", true);
      T.string("error message", CompositeValidator.ERR_CL, eM.getMessage());
    }
    catch (Exception e) {
      Assert.fail("unexpected exception: " + e.getMessage());
    }
  } // public void testFailOwnerEqualsLeftFactor()

}


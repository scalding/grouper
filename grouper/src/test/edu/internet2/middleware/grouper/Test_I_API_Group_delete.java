/*
  Copyright (C) 2004-2007 University Corporation for Advanced Internet Development, Inc.
  Copyright (C) 2004-2007 The University Of Chicago

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
import  edu.internet2.middleware.subject.Subject;
import  edu.internet2.middleware.subject.SourceUnavailableException;
import  edu.internet2.middleware.subject.SubjectNotFoundException;
import  edu.internet2.middleware.subject.SubjectNotUniqueException;

/**
 * Test <code>Group.delete()</code>.
 * @author  blair christensen.
 * @version $Id: Test_I_API_Group_delete.java,v 1.1 2007-05-31 17:59:43 blair Exp $
 * @since   1.2.0
 */
public class Test_I_API_Group_delete extends GrouperTest {

  // PRIVATE INSTANCE VARIABLES //
  private Group           gA, gB;
  private Stem            parent;
  private RegistrySubject rSubjX;
  private GrouperSession  s;
  private Subject         subjX;



  // TESTING INFRASTRUCTURE //

  public void setUp() {
    super.setUp();
    try {
      s       = GrouperSession.start( SubjectFinder.findRootSubject() );
      parent  = StemFinder.findRootStem(s).addChildStem("parent", "parent");
      gA      = parent.addChildGroup("child group a", "child group a");
      gB      = parent.addChildGroup("child group b", "child group b");
      rSubjX  = RegistrySubject.add(s, "subjX", "person", "subjX");
      subjX   = SubjectFinder.findById( rSubjX.getId() );
    }
    catch (Exception eShouldNotHappen) {
      throw new GrouperRuntimeException( eShouldNotHappen.getMessage(), eShouldNotHappen );
    }
  }

  public void tearDown() {
    try {
      s.stop();
    }
    catch (Exception eShouldNotHappen) {
      throw new GrouperRuntimeException( eShouldNotHappen.getMessage(), eShouldNotHappen );
    }
    super.tearDown();
  }


  // TESTS //

  /**
   * Delete group with a <i>Subject</i> that can no longer be resolved.
   * @since   1.2.0
   */
  public void test_delete_withSubjectThatCanNoLongerBeResolved() 
    throws  GroupDeleteException,
            GrouperException,
            InsufficientPrivilegeException,
            MemberAddException,
            SourceUnavailableException,
            SubjectNotFoundException,
            SubjectNotUniqueException
  {
    // SETUP 
    gA.addMember(subjX);
    rSubjX.delete(s);
   
    // TEST 
    gA.delete(); 
    assertTrue(true);
  }

  /**
   * Delete group that causes an effective <i>Subject</i> when the <i>Subject</i> no longer exists.
   * @since   1.2.0
   */
  public void test_delete_causesAnEffectiveSubjectWhenSubjectNoLongerExists() 
    throws  GroupDeleteException,
            GrouperException,
            InsufficientPrivilegeException,
            MemberAddException,
            SubjectNotFoundException,
            SubjectNotUniqueException
  {
    // SETUP 
    gA.addMember(subjX);
    gB.addMember( gA.toSubject() );
    rSubjX.delete(s);
   
    // TEST 
    gA.delete(); 
    assertTrue("no exceptions thrown", true);
  }

  /**
   * Delete group with an effective <i>Subject</i> when the <i>Subject</i> no longer exists.
   * @since   1.2.0
   */
  public void test_delete_withAnEffectiveSubjectWhenSubjectNoLongerExists() 
    throws  GroupDeleteException,
            GrouperException,
            InsufficientPrivilegeException,
            MemberAddException,
            SubjectNotFoundException,
            SubjectNotUniqueException
  {
    // SETUP 
    gA.addMember(subjX);
    gB.addMember( gA.toSubject() );
    rSubjX.delete(s);
   
    // TEST 
    gB.delete(); 
    assertTrue("no exceptions thrown", true);
  }

} 


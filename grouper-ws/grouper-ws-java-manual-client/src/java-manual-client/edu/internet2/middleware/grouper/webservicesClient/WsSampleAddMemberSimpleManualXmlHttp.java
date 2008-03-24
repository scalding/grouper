package edu.internet2.middleware.grouper.webservicesClient;

import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import edu.internet2.middleware.grouper.webservicesClient.util.ManualClientSettings;
import edu.internet2.middleware.grouper.ws.samples.WsSampleManualXmlHttp;

/**
 * @author mchyzer
 */
public class WsSampleAddMemberSimpleManualXmlHttp implements WsSampleManualXmlHttp {

  /**
   * add member simple web service with REST
   */
  public static void addMemberSimpleXmlHttp() {
    Reader xmlReader = null;
    try {
      HttpClient httpClient = new HttpClient();
      
      //generated from GrouperServiceUtilsTest.testQueryStringsAddMemberSimple()
      String queryString = "actAsSubjectId=GrouperSystem&clientVersion=v1_3_000&" +
      		"groupName=aStem%3AaGroup&groupUuid=&subjectId=10021368&subjectSource=&" +
      		"subjectIdentifier=&actAsSubjectSource=&actAsSubjectIdentifier=&" +
      		"fieldName=&includeGroupDetail=&includeSubjectDetail=&subjectAttributeNames=&" +
      		"paramName0=&paramValue0=&paramName1=&paramValue1=";
      
      //e.g. http://localhost:8093/grouper-ws/services/GrouperService
      GetMethod method = new GetMethod(
          ManualClientSettings.URL + "/addMemberSimple?" + queryString);

      httpClient.getParams().setAuthenticationPreemptive(true);
      Credentials defaultcreds = new UsernamePasswordCredentials(ManualClientSettings.USER, 
          ManualClientSettings.PASS);
      //e.g. localhost and 8093
      httpClient.getState()
          .setCredentials(new AuthScope(ManualClientSettings.HOST, ManualClientSettings.PORT), defaultcreds);

      httpClient.executeMethod(method);

      int statusCode = method.getStatusCode();

      // see if request worked or not
      if (statusCode != 200) {
        throw new RuntimeException("Bad response from web service: " + statusCode);
      }

      String response = method.getResponseBodyAsString();

      //lets load this into jdom, since it is xml
      xmlReader = new StringReader(response);

      // process xml
      Document document = new SAXBuilder().build(xmlReader);
      Element addMemberSimpleResponse = document.getRootElement();

      //  <ns:addMemberSimpleResponse
      //  xmlns:ns="http://soap.ws.grouper.middleware.internet2.edu/xsd">
      RunGrouperServiceNonAxisUtils.assertTrue("addMemberSimpleResponse"
          .equals(addMemberSimpleResponse.getName()),
          "root not addMemberSimpleResponse: " + addMemberSimpleResponse.getName());

      Namespace namespace = addMemberSimpleResponse.getNamespace();

      //  <ns:return type="edu.internet2.middleware.grouper.ws.soap.WsAddMemberSimpleResult">
      Element returnElement = addMemberSimpleResponse.getChild("return", namespace);
      String theType = returnElement.getAttributeValue("type");
      RunGrouperServiceNonAxisUtils.assertTrue(
          "edu.internet2.middleware.grouper.ws.soap.WsAddMemberSimpleResult"
              .equals(theType),
          "type not edu.internet2.middleware.grouper.ws.soap.WsAddMemberSimpleResult: "
              + theType);


      
      //    <ns:responseMetadata type="edu.internet2.middleware.grouper.ws.soap.WsResponseMeta">
      //      <ns:resultWarnings></ns:resultWarnings>
      //      <ns:serverVersion>v1_3_000</ns:serverVersion>
      //    </ns:responseMetadata>
      //    <ns:resultMetadata type="edu.internet2.middleware.grouper.ws.soap.WsResultMeta">
      //      <ns:resultCode>SUCCESS</ns:resultCode>
      //      <ns:resultMessage>
      //        Success for: clientVersion: v1_3_000, wsGroupLookup:
      //        edu.internet2.middleware.grouper.ws.soap.WsGroupLookup@d4ff95[group=&lt;null>,uuid=,groupName=aStem:aGroup,groupFindResult=&lt;null>],
      //        subjectLookups: Array size: 1: [0]:
      //        edu.internet2.middleware.grouper.ws.soap.WsSubjectLookup@2f54[subject=&lt;null>,...
      //        , replaceAllExisting: false, actAsSubject:
      //        edu.internet2.middleware.grouper.ws.soap.WsSubjectLookup@235085[subject=&lt;null>,cause=&lt;null>,subjectFindResult=&lt;null>,subjectId=GrouperSystem,subjectIdentifier=,subjectSource=],
      //        fieldName: null, txType: NONE, includeGroupDetail: false, includeSubjectDetail:
      //        false, subjectAttributeNames: null , paramNames: null, paramValues: null
      //      </ns:resultMessage>
      //      <ns:success>T</ns:success>
      //    </ns:resultMetadata>
      
      Element resultMetaElement = returnElement.getChild("resultMetadata", namespace);
      String errorMessage = resultMetaElement.getChildText("resultMessage", namespace);

      String resultCode = resultMetaElement.getChildText("resultCode", namespace);

      String success = resultMetaElement.getChildText("success", namespace);


      
      //    <ns:subjectAttributeNames xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      //      xsi:nil="true" />
      //    <ns:wsGroupAssigned type="edu.internet2.middleware.grouper.ws.soap.WsGroup">
      //      <ns:description>somedescription</ns:description>
      //      <ns:detail xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:nil="true" />
      //      <ns:displayExtension>test group</ns:displayExtension>
      //      <ns:displayName>a stem:test group</ns:displayName>
      //      <ns:extension>aGroup</ns:extension>
      //      <ns:name>aStem:aGroup</ns:name>
      //      <ns:uuid>cd89f7c5-913e-4788-9a67-c78a5fee1fba</ns:uuid>
      //    </ns:wsGroupAssigned>
      //    <ns:wsSubject type="edu.internet2.middleware.grouper.ws.soap.WsSubject">
      //      <ns:attributeValues xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      //        xsi:nil="true" />
      //      <ns:id>10021368</ns:id>
      //      <ns:name xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:nil="true" />
      //      <ns:resultCode>SUCCESS</ns:resultCode>
      //      <ns:source>QSUOB JDBC Source Adapter</ns:source>
      //      <ns:success>T</ns:success>
      //    </ns:wsSubject>
      //  </ns:return>
      //</ns:addMemberSimpleResponse>
      
      Element subjectElement = returnElement.getChild("wsSubject", namespace);

      String subjectId = subjectElement.getChildText("id", namespace);

      System.out.println("Success: " + success + ", resultCode: " + resultCode
          + ", subjectId: " + subjectId
          + ", errorMessage: " + errorMessage);

    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      try {
        xmlReader.close();
      } catch (Exception e) {
      }
    }

  }

  /**
   * @param args
   */
  @SuppressWarnings("unchecked")
  public static void main(String[] args) {
    addMemberSimpleXmlHttp();
  }

  /**
   * @see edu.internet2.middleware.grouper.ws.samples.WsSampleManualXmlHttp#executeSample()
   */
  public void executeSample() {
    addMemberSimpleXmlHttp();
  }
}

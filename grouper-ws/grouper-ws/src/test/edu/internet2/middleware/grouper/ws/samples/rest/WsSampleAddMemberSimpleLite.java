package edu.internet2.middleware.grouper.ws.samples.rest;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;

import edu.internet2.middleware.grouper.webservicesClient.util.ManualClientSettings;
import edu.internet2.middleware.grouper.ws.lite.group.WsLiteAddMemberSimpleRequest;
import edu.internet2.middleware.grouper.ws.samples.WsSampleRest;
import edu.internet2.middleware.grouper.ws.samples.WsSampleRestType;
import edu.internet2.middleware.grouper.ws.soap.WsAddMemberSimpleResult;
import edu.internet2.middleware.grouper.ws.util.RestClientSettings;

/**
 * @author mchyzer
 */
public class WsSampleAddMemberSimpleLite implements WsSampleRest {

  /**
   * add member simple web service with REST
   * @param wsSampleRestType is the type of rest (xml, xhtml, etc)
   */
  public static void addMemberSimpleLite(WsSampleRestType wsSampleRestType) {

    try {
      HttpClient httpClient = new HttpClient();
      
      //URL e.g. http://localhost:8093/grouper-ws/servicesLite
      //NOTE: aStem:aGroup urlencoded substitutes %3A for a colon
      PutMethod method = new PutMethod(
          RestClientSettings.URL + "/v1_3_000/group/aStem%3AaGroup/members/10021368");

      httpClient.getParams().setAuthenticationPreemptive(true);
      Credentials defaultcreds = new UsernamePasswordCredentials(ManualClientSettings.USER, 
          ManualClientSettings.PASS);
      
      //e.g. localhost and 8093
      httpClient.getState()
          .setCredentials(new AuthScope(ManualClientSettings.HOST, ManualClientSettings.PORT), defaultcreds);

      //Make the body of the request, in this case with beans and marshaling, but you can make
      //your request document in whatever language or way you want
      WsLiteAddMemberSimpleRequest addMemberSimple = new WsLiteAddMemberSimpleRequest();

      // set the act as id
      addMemberSimple.setActAsSubjectId("GrouperSystem");

      //get the xml / json / xhtml / paramString
      String requestDocument = wsSampleRestType.getWsLiteRequestContentType().writeString(addMemberSimple);
      
      //make sure right content type is in request (e.g. application/xhtml+xml
      String contentType = wsSampleRestType.getWsLiteRequestContentType().getContentType();
      
      method.setRequestEntity(new StringRequestEntity(requestDocument, contentType, "UTF-8"));
      
      httpClient.executeMethod(method);

      //make sure a request came back
      Header successHeader = method.getResponseHeader("X-Grouper-success");
      String successString = successHeader == null ? null : successHeader.getValue();
      if (StringUtils.isBlank(successString)) {
        throw new RuntimeException("Web service did not even respond!");
      }
      boolean success = "T".equals(successString);
      String responseCode = method.getResponseHeader("X-Grouper-responseCode").getValue();
      
      int statusCode = method.getStatusCode();

      // see if request worked or not
      if (statusCode != 200 || !success) {
        throw new RuntimeException("Bad response from web service: " + statusCode + ", responseCode: " + responseCode);
      }

      String response = method.getResponseBodyAsString();

      //convert to object (from xhtml, xml, json, etc)
      WsAddMemberSimpleResult wsAddMemberSimpleResult = (WsAddMemberSimpleResult)wsSampleRestType
        .getWsLiteResponseContentType().parseString(response);
      
      System.out.println(wsAddMemberSimpleResult);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  /**
   * @param args
   */
  @SuppressWarnings("unchecked")
  public static void main(String[] args) {
    addMemberSimpleLite(WsSampleRestType.xhtml);
  }

  /**
   * @see edu.internet2.middleware.grouper.ws.samples.WsSampleRest#executeSample(edu.internet2.middleware.grouper.ws.samples.WsSampleRestType)
   */
  public void executeSample(WsSampleRestType wsSampleRestType) {
    addMemberSimpleLite(wsSampleRestType);
  }

  /**
   * @see edu.internet2.middleware.grouper.ws.samples.WsSampleRest#validType(edu.internet2.middleware.grouper.ws.samples.WsSampleRestType)
   */
  public boolean validType(WsSampleRestType wsSampleRestType) {
    //allow all
    return true;
  }
}

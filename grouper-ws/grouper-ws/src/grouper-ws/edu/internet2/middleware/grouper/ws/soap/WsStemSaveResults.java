package edu.internet2.middleware.grouper.ws.soap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.internet2.middleware.grouper.hibernate.GrouperTransactionType;
import edu.internet2.middleware.grouper.util.GrouperUtil;
import edu.internet2.middleware.grouper.ws.WsResultCode;
import edu.internet2.middleware.grouper.ws.exceptions.WsInvalidQueryException;
import edu.internet2.middleware.grouper.ws.soap.WsStemSaveResult.WsStemSaveResultCode;

/**
 * <pre>
 * results for the stems save call.
 * 
 * result code:
 * code of the result for this stem overall
 * SUCCESS: means everything ok
 * STEM_NOT_FOUND: cant find the stem
 * STEM_DUPLICATE: found multiple stems
 * </pre>
 * @author mchyzer
 */
public class WsStemSaveResults {

  /**
   * result code of a request
   */
  public enum WsStemSaveResultsCode implements WsResultCode {

    /** found the stems, saved them (lite http status code 201) (success: T) */
    SUCCESS(201),

    /** either overall exception, or one or more stems had exceptions (lite http status code 500) (success: F) */
    EXCEPTION(500),

    /** problem deleting existing stems (lite http status code 500) (success: F) */
    PROBLEM_SAVING_STEMS(500),

    /** invalid query (e.g. if everything blank) (lite http status code 400) (success: F) */
    INVALID_QUERY(400);

    /** http status code for rest/lite e.g. 200 */
    private int httpStatusCode;

    /**
     * status code for rest/lite e.g. 200
     * @param statusCode
     */
    private WsStemSaveResultsCode(int statusCode) {
      this.httpStatusCode = statusCode;
    }

    /**
     * @see edu.internet2.middleware.grouper.ws.WsResultCode#getHttpStatusCode()
     */
    public int getHttpStatusCode() {
      return this.httpStatusCode;
    }

    /**
     * if this is a successful result
     * @return true if success
     */
    public boolean isSuccess() {
      return this == SUCCESS;
    }

  }

  /**
   * logger 
   */
  private static final Log LOG = LogFactory.getLog(WsStemSaveResults.class);

  /**
   * assign the code from the enum
   * @param stemSaveResultsCode
   */
  public void assignResultCode(WsStemSaveResultsCode stemSaveResultsCode) {
    this.getResultMetadata().assignResultCode(stemSaveResultsCode);
  }

  /**
   * convert the result code back to enum
   * @return the enum code
   */
  public WsStemSaveResultsCode retrieveResultCode() {
    if (StringUtils.isBlank(this.getResultMetadata().getResultCode())) {
      return null;
    }
    return WsStemSaveResultsCode.valueOf(this.getResultMetadata().getResultCode());
  }

  /**
   * results for each deletion sent in
   */
  private WsStemSaveResult[] results;

  /**
   * metadata about the result
   */
  private WsResultMeta resultMetadata = new WsResultMeta();

  /**
   * results for each deletion sent in
   * @return the results
   */
  public WsStemSaveResult[] getResults() {
    return this.results;
  }

  /**
   * results for each deletion sent in
   * @param results1 the results to set
   */
  public void setResults(WsStemSaveResult[] results1) {
    this.results = results1;
  }

  /**
   * @return the resultMetadata
   */
  public WsResultMeta getResultMetadata() {
    return this.resultMetadata;
  }

  /**
   * make sure if there is an error, to record that as an error
   * @param grouperTransactionType for request
   * @param theSummary
   * @return true if success, false if not
   */
  public boolean tallyResults(GrouperTransactionType grouperTransactionType,
      String theSummary) {
    //maybe already a failure
    boolean successOverall = GrouperUtil.booleanValue(this.getResultMetadata()
        .getSuccess(), true);
    if (this.getResults() != null) {
      // check all entries
      int successes = 0;
      int failures = 0;
      for (WsStemSaveResult wsStemSaveResult : this.getResults()) {
        boolean theSuccess = "T".equalsIgnoreCase(wsStemSaveResult.getResultMetadata()
            .getSuccess());
        if (theSuccess) {
          successes++;
        } else {
          failures++;
        }
      }

      //if transaction rolled back all line items, 
      if ((!successOverall || failures > 0) && grouperTransactionType.isTransactional()
          && !grouperTransactionType.isReadonly()) {
        for (WsStemSaveResult wsStemSaveResult : this.getResults()) {
          if (GrouperUtil.booleanValue(wsStemSaveResult.getResultMetadata().getSuccess(),
              true)) {
            wsStemSaveResult
                .assignResultCode(WsStemSaveResultCode.TRANSACTION_ROLLED_BACK);
            failures++;
          }
        }
      }

      if (failures > 0) {
        this.getResultMetadata().appendResultMessage(
            "There were " + successes + " successes and " + failures
                + " failures of users added to the group.   ");
        this.assignResultCode(WsStemSaveResultsCode.PROBLEM_SAVING_STEMS);
        //this might not be a problem
        LOG.warn(this.getResultMetadata().getResultMessage());

      } else {
        this.assignResultCode(WsStemSaveResultsCode.SUCCESS);
      }
    } else {
      //none is ok
      this.assignResultCode(WsStemSaveResultsCode.SUCCESS);
    }
    //make response descriptive
    if (GrouperUtil.booleanValue(this.getResultMetadata().getSuccess(), false)) {
      this.getResultMetadata().appendResultMessage("Success for: " + theSummary);
      return true;
    }
    //false if need rollback
    return !grouperTransactionType.isTransactional();
  }

  /**
   * prcess an exception, log, etc
   * @param wsStemSaveResultsCodeOverride
   * @param theError
   * @param e
   */
  public void assignResultCodeException(
      WsStemSaveResultsCode wsStemSaveResultsCodeOverride, String theError, Exception e) {

    if (e instanceof WsInvalidQueryException) {
      wsStemSaveResultsCodeOverride = GrouperUtil.defaultIfNull(
          wsStemSaveResultsCodeOverride, WsStemSaveResultsCode.INVALID_QUERY);
      //      if (e.getCause() instanceof StemNotFoundException) {
      //        wsStemSaveResultsCodeOverride = WsStemSaveResultsCode.STEM_NOT_FOUND;
      //      }
      //a helpful exception will probably be in the getMessage()
      this.assignResultCode(wsStemSaveResultsCodeOverride);
      this.getResultMetadata().appendResultMessage(e.getMessage());
      this.getResultMetadata().appendResultMessage(theError);
      LOG.warn(e);

    } else {
      wsStemSaveResultsCodeOverride = GrouperUtil.defaultIfNull(
          wsStemSaveResultsCodeOverride, WsStemSaveResultsCode.EXCEPTION);
      LOG.error(theError, e);

      theError = StringUtils.isBlank(theError) ? "" : (theError + ", ");
      this.getResultMetadata().appendResultMessage(
          theError + ExceptionUtils.getFullStackTrace(e));
      this.assignResultCode(wsStemSaveResultsCodeOverride);

    }
  }

}

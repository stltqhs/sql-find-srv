package com.yuqing.web.sqlfinder.entity.response;

import com.yuqing.web.sqlfinder.memdb.QueryResult;

/**
 * @author yuqing
 * @date 2024/3/9
 */
public class SQLFindResponse {

  private String reason;
  private QueryResult result;

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public QueryResult getResult() {
    return result;
  }

  public void setResult(QueryResult result) {
    this.result = result;
  }
}

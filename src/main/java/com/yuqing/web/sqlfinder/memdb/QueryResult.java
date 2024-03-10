package com.yuqing.web.sqlfinder.memdb;

import java.util.List;
import java.util.Map;

/**
 * @author yuqing
 * @date 2024/3/10
 */
public class QueryResult {

  private Metadata metadata;

  private List<Map<String, Object>> rows;

  public Metadata getMetadata() {
    return metadata;
  }

  public void setMetadata(Metadata metadata) {
    this.metadata = metadata;
  }

  public List<Map<String, Object>> getRows() {
    return rows;
  }

  public void setRows(List<Map<String, Object>> rows) {
    this.rows = rows;
  }

  public static class Metadata {

    private List<String> names;
    private List<String> types;

    public List<String> getNames() {
      return names;
    }

    public void setNames(List<String> names) {
      this.names = names;
    }

    public List<String> getTypes() {
      return types;
    }

    public void setTypes(List<String> types) {
      this.types = types;
    }
  }
}

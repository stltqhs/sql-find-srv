package com.yuqing.web.sqlfinder.memdb;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yuqing
 * @date 2024/3/10
 */
public class TableRow extends HashMap<String, Object> {

  public TableRow(Map<? extends String, ?> originRows) {
    super(originRows);
  }
}

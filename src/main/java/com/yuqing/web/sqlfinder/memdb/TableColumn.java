package com.yuqing.web.sqlfinder.memdb;

import com.yuqing.web.sqlfinder.util.TypeNameUtil;

/**
 * @author yuqing
 * @date 2024/3/9
 */
public class TableColumn {

  private final String name;
  private final String typeName;
  private final Class type;

  public TableColumn(String name, String typeName) {
    this.name = name;
    this.typeName = typeName;
    this.type = convertType(this.typeName);
  }

  public String getName() {
    return name;
  }

  public String getTypeName() {
    return typeName;
  }

  public Class getType() {
    return type;
  }

  private Class convertType(String typeName) {
    return TypeNameUtil.convertType(typeName);
  }
}

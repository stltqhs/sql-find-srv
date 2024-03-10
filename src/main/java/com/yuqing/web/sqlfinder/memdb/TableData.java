package com.yuqing.web.sqlfinder.memdb;

import java.util.List;

/**
 * @author yuqing
 * @date 2024/3/9
 */
public class TableData {

  private String tableName;
  private List<TableColumn> columns;
  private List<TableRow> rows;

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public List<TableColumn> getColumns() {
    return columns;
  }

  public void setColumns(List<TableColumn> columns) {
    this.columns = columns;
  }

  public List<TableRow> getRows() {
    return rows;
  }

  public void setRows(List<TableRow> rows) {
    this.rows = rows;
  }

  @Override
  public String toString() {
    return "TableData, " + tableName + columns.stream().map(TableColumn::getName).toList();
  }
}

package com.yuqing.web.sqlfinder.entity.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author yuqing
 * @date 2024/3/9
 */
public class SQLFindParam {

  @NotNull
  private SQLInfo sqlInfo;

  @NotNull
  private List<Database> databases;

  public SQLInfo getSqlInfo() {
    return sqlInfo;
  }

  public void setSqlInfo(SQLInfo sqlInfo) {
    this.sqlInfo = sqlInfo;
  }

  public List<Database> getDatabases() {
    return databases;
  }

  public void setDatabases(
      List<Database> databases) {
    this.databases = databases;
  }

  /**
   * SQL 语句
   */
  public static class SQLInfo {

    @NotBlank
    /**
     * SQL 目标
     */
    private String sqlTemplate;
    /**
     * sqlTemplate 中的占位符
     */
    private List<Argument> args;

    public String getSqlTemplate() {
      return sqlTemplate;
    }

    public void setSqlTemplate(String sqlTemplate) {
      this.sqlTemplate = sqlTemplate;
    }

    public List<Argument> getArgs() {
      return args;
    }

    public void setArgs(List<Argument> args) {
      this.args = args;
    }
  }

  public static class Argument {

    private Object value;
    @NotEmpty
    private String type;

    public Object getValue() {
      return value;
    }

    public void setValue(Object value) {
      this.value = value;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }
  }

  public static class Database {

    @NotBlank
    private String name;

    @NotEmpty
    private List<TableAndData> tables;

    public List<TableAndData> getTables() {
      return tables;
    }

    public void setTables(
        List<TableAndData> tables) {
      this.tables = tables;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  /**
   * 表和数据
   */
  public static class TableAndData {

    @NotBlank
    private String tableName;

    @NotEmpty
    private List<Column> columns;

    @NotEmpty
    private List<Map<String, Object>> rows;

    public String getTableName() {
      return tableName;
    }

    public void setTableName(String tableName) {
      this.tableName = tableName;
    }

    public List<Column> getColumns() {
      return columns;
    }

    public void setColumns(List<Column> columns) {
      this.columns = columns;
    }

    public List<Map<String, Object>> getRows() {
      return rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
      this.rows = rows;
    }
  }

  public static class Column {

    public static final String INT8 = "int8";
    public static final String INT16 = "int16";
    public static final String INT32 = "int32";
    public static final String INT64 = "int64";
    public static final String FLOAT32 = "float32";
    public static final String FLOAT64 = "float64";
    public static final String STRING = "string";
    public static final String DATE = "date";

    @NotEmpty
    private String name;

    @NotEmpty
    private String type;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }
  }
}

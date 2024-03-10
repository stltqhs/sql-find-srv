package com.yuqing.web.sqlfinder.memdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.SchemaPlus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author yuqing
 * @date 2024/3/9
 */
@RunWith(JUnit4.class)
public class MapSchemaTest {

  @Test
  public void test() throws ClassNotFoundException, SQLException {
    Class.forName("org.apache.calcite.jdbc.Driver");
    Connection connection =
        DriverManager.getConnection("jdbc:calcite:");
    CalciteConnection calciteConnection =
        connection.unwrap(CalciteConnection.class);
    SchemaPlus rootSchema = calciteConnection.getRootSchema();

    List<TableData> tableDataList = new LinkedList<>();
    TableData t1 = new TableData();
    t1.setTableName("log");
    t1.setColumns(Arrays.asList(new TableColumn("traceid", TableColumn.INT64),
        new TableColumn("content", TableColumn.STRING), new TableColumn("time", TableColumn.DATE)));

    Map<String, Object> row1 = HashMap.newHashMap(3);
    row1.put("traceid", 1234L);
    row1.put("content", "hello");
    row1.put("time", new Date());

    Map<String, Object> row2 = HashMap.newHashMap(3);
    row2.put("traceid", 2345L);
    row2.put("content", "picture");
    row2.put("time", new Date());

    Map<String, Object> row3 = HashMap.newHashMap(3);
    row3.put("traceid", 3456L);
    row3.put("content", "picture");
    row3.put("time", new Date());
    t1.setRows(
        Arrays.asList(new TableRow(row1), new TableRow(row2),
            new TableRow(row3)));

    tableDataList.add(t1);

    rootSchema.add("default", new MapSchema("default", tableDataList));

    Statement statement = connection.createStatement();
    ResultSet resultSet =
        statement.executeQuery("select * from \"default\".\"log\" where \"traceid\" > 1234");
    final StringBuilder buf = new StringBuilder();
    while (resultSet.next()) {
      int n = resultSet.getMetaData().getColumnCount();
      for (int i = 1; i <= n; i++) {
        buf.append(i > 1 ? "; " : "")
            .append(resultSet.getMetaData().getColumnLabel(i))
            .append("=")
            .append(resultSet.getObject(i));
      }
      System.out.println(buf.toString());
      buf.setLength(0);
    }
    resultSet.close();
    statement.close();
    connection.close();
  }
}

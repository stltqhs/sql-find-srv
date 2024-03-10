package com.yuqing.web.sqlfinder.service.impl;

import com.yuqing.web.sqlfinder.entity.request.SQLFindParam.Argument;
import com.yuqing.web.sqlfinder.memdb.MapSchema;
import com.yuqing.web.sqlfinder.memdb.QueryResult;
import com.yuqing.web.sqlfinder.memdb.QueryResult.Metadata;
import com.yuqing.web.sqlfinder.service.MemoryDatabaseService;
import com.yuqing.web.sqlfinder.util.TypeNameUtil;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.calcite.avatica.SqlType;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.SchemaPlus;
import org.springframework.stereotype.Service;

/**
 * @author yuqing
 * @date 2024/3/10
 */
@Service
public class MemoryDatabaseServiceImpl implements MemoryDatabaseService {

  @Override
  public QueryResult executeQuery(String query, List<Argument> args, List<MapSchema> sourceList)
      throws SQLException {

    driverClass();

    Connection connection = null;
    CalciteConnection calciteConnection = null;

    try {
      connection =
          DriverManager.getConnection("jdbc:calcite:lex=MYSQL");
      calciteConnection =
          connection.unwrap(CalciteConnection.class);

      SchemaPlus rootSchema = calciteConnection.getRootSchema();
      for (MapSchema schema : sourceList) {
        rootSchema.add(schema.getName(), schema);
      }

      return doQuery(connection, query, args);
    } finally {
      if (calciteConnection != null && !calciteConnection.isClosed()) {
        calciteConnection.close();
      }
      if (connection != null && !connection.isClosed()) {
        connection.close();
      }
    }
  }

  private QueryResult doQuery(Connection connection, String query, List<Argument> args)
      throws SQLException {
    PreparedStatement statement = null;
    ResultSet rs = null;
    QueryResult queryResult;

    try {
      statement = connection.prepareStatement(query);
      int index = 1;
      for (Argument arg : args) {
        setArg(statement, arg, index);
        index++;
      }

      rs = statement.executeQuery();

      ResultSetMetaData metaData = rs.getMetaData();

      queryResult = new QueryResult();
      QueryResult.Metadata simpleMethodData = new Metadata();
      simpleMethodData.setNames(new ArrayList<>(metaData.getColumnCount()));
      simpleMethodData.setTypes(new ArrayList<>(metaData.getColumnCount()));
      queryResult.setMetadata(simpleMethodData);

      for (int i = 0; i < metaData.getColumnCount(); i++) {
        simpleMethodData.getNames().add(metaData.getColumnName(i + 1));
        simpleMethodData.getTypes().add(metaData.getColumnTypeName(i + 1));
      }

      queryResult.setRows(new LinkedList<>());

      while (rs.next()) {
        Map<String, Object> row = new TreeMap<>();

        for (int i = 0; i < metaData.getColumnCount(); i++) {
          row.put(metaData.getColumnName(i + 1), formatOutValue(rs.getObject(i + 1)));
        }
        queryResult.getRows().add(row);
      }

    } finally {
      if (statement != null) {
        statement.close();
      }
      if (rs != null) {
        rs.close();
      }
    }

    return queryResult;
  }

  private Object formatOutValue(Object value) {
    if (value instanceof Long) {
      return value.toString();
    }

    return value;
  }

  private void setArg(PreparedStatement statement, Argument arg, int index) throws SQLException {
    if (arg.getValue() == null) {
      statement.setNull(index, SqlType.NULL.id);
      return;
    }

    Object value = arg.getValue();

    Class type = TypeNameUtil.convertType(arg.getType());

    if (value instanceof Number) {
      if (type == byte.class || type == short.class || type == int.class) {
        statement.setInt(index, ((Number) value).intValue());
        return;
      }

      if (type == long.class) {
        statement.setLong(index, ((Number) value).longValue());
        return;
      }

      if (type == float.class) {
        statement.setFloat(index, ((Number) value).floatValue());
        return;
      }
      if (type == double.class) {
        statement.setDouble(index, ((Number) value).doubleValue());
        return;
      }
    }

    if (type == Date.class) {
      statement.setDate(index, (Date) value);
      return;
    }

    if (type == String.class) {
      statement.setString(index, (String) value);
    } else {
      statement.setObject(index, value);
    }
  }

  private void driverClass() throws SQLException {
    String driverName = "org.apache.calcite.jdbc.Driver";
    try {
      Class.forName(driverName);
    } catch (ClassNotFoundException e) {
      throw new SQLException("not class found: " + driverName, e);
    }
  }
}

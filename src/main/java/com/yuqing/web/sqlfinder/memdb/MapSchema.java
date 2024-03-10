package com.yuqing.web.sqlfinder.memdb;

import com.google.common.collect.ImmutableMap;
import com.yuqing.web.sqlfinder.util.ValueConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.calcite.DataContext;
import org.apache.calcite.adapter.java.AbstractQueryableTable;
import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.calcite.linq4j.QueryProvider;
import org.apache.calcite.linq4j.Queryable;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.ScannableTable;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.Statistic;
import org.apache.calcite.schema.Statistics;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;
import org.apache.calcite.schema.impl.AbstractTableQueryable;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.util.Pair;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yuqing
 * @date 2024/3/9
 */
public class MapSchema extends AbstractSchema {

  private static final Logger LOG = LoggerFactory.getLogger(MapSchema.class);

  private final String name;

  private final List<TableData> tableDataList;

  private @MonotonicNonNull Map<String, Table> tableMap;

  public MapSchema(String name, List<TableData> tableDataList) {
    this.name = name;
    this.tableDataList = tableDataList;
  }

  public String getName() {
    return name;
  }

  @Override
  protected Map<String, Table> getTableMap() {
    if (tableMap != null) {
      return tableMap;
    }

    tableMap = createTableMap();
    return tableMap;
  }

  private Map<String, Table> createTableMap() {
    ImmutableMap.Builder<String, Table> builder = ImmutableMap.builder();

    for (TableData tableData : tableDataList) {
      if (StringUtils.isBlank(tableData.getTableName())) {
        LOG.info("ignore this table to createTableMap because table name is not presented");
        continue;
      }
      if (CollectionUtils.isEmpty(tableData.getColumns())) {
        LOG.info("ignore this table to createTableMap because columns is not presented");
        continue;
      }
      if (!checkTableColumns(tableData.getColumns())) {
        LOG.info("ignore this table to createTableMap because columns is incorrect");
        continue;
      }

      Table table = createTable(tableData);

      builder.put(tableData.getTableName(), table);
    }

    return builder.build();
  }

  private Table createTable(TableData tableData) {
    if (tableData.getRows() == null) {
      tableData.setRows(Collections.emptyList());
    }

    return new MapTable(tableData, Object[].class);
  }

  private boolean checkTableColumns(List<TableColumn> columns) {
    for (TableColumn column : columns) {
      if (StringUtils.isBlank(column.getName())) {
        LOG.info("check table colum failed because of empty column name");
        return false;
      }
    }
    return true;
  }

  @Override
  public String toString() {
    return "MapSchema{target=" + tableDataList.size() + "}";
  }

  private static class MapTable extends AbstractQueryableTable
      implements Table, ScannableTable {

    private final TableData tableData;

    private final Enumerable enumerable;

    private final Object[] rows;

    protected MapTable(TableData tableData, Class elementType) {
      super(elementType);
      this.tableData = tableData;
      this.rows = toArray(tableData.getRows());
      this.enumerable = createEnumerable(rows);
    }

    @Override
    public <T> Queryable<T> asQueryable(QueryProvider queryProvider, SchemaPlus schemaPlus,
        String tableName) {
      return new AbstractTableQueryable<T>(queryProvider, schemaPlus, this, tableName) {
        @SuppressWarnings("unchecked")
        public Enumerator<T> enumerator() {
          return (Enumerator<T>) enumerable.enumerator();
        }
      };
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory relDataTypeFactory) {
      JavaTypeFactory typeFactory = (JavaTypeFactory) relDataTypeFactory;
      List<String> names = new ArrayList<>(tableData.getColumns().size());
      List<RelDataType> types = new ArrayList<>(tableData.getColumns().size());

      for (TableColumn column : tableData.getColumns()) {
        names.add(column.getName());
        types.add(toSqlType(column.getType(), typeFactory));
      }
      return typeFactory.createStructType(Pair.zip(names, types));
    }

    @Override
    public Statistic getStatistic() {
      return Statistics.UNKNOWN;
    }

    @Override
    public Enumerable<Object[]> scan(DataContext root) {
      return enumerable;
    }

    private Enumerable createEnumerable(Object[] rows) {
      return Linq4j.asEnumerable(rows);
    }

    private Object[] toArray(List<TableRow> mapOfRows) {
      Object[] arr = new Object[mapOfRows.size()];
      int i = 0;
      for (TableRow item : mapOfRows) {
        arr[i] = toFlatRow(item);
        i++;
      }
      return arr;
    }

    private Object[] toFlatRow(TableRow row) {
      Object[] flat = new Object[tableData.getColumns().size()];
      int i = 0;
      for (TableColumn column : tableData.getColumns()) {
        Object value = row.get(column.getName());
        value = ValueConverter.convertValue(column.getType(), value);
        flat[i] = value;
        i++;
      }
      return flat;
    }

    private RelDataType toSqlType(Class type, JavaTypeFactory typeFactory) {
      if (type == byte.class || type == short.class || type == int.class) {
        return typeFactory.createSqlType(SqlTypeName.INTEGER);
      }
      if (type == long.class) {
        return typeFactory.createSqlType(SqlTypeName.BIGINT);
      }
      if (type == float.class) {
        return typeFactory.createSqlType(SqlTypeName.FLOAT);
      }

      if (type == double.class) {
        return typeFactory.createSqlType(SqlTypeName.DOUBLE);
      }

      if (type == String.class) {
        return typeFactory.createSqlType(SqlTypeName.VARCHAR);
      }

      if (type == Date.class) {
        return typeFactory.createSqlType(SqlTypeName.DATE);
      }

      return typeFactory.createSqlType(SqlTypeName.ANY);
    }
  }
}

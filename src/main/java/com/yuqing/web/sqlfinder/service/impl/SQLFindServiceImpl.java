package com.yuqing.web.sqlfinder.service.impl;

import com.yuqing.web.sqlfinder.entity.request.SQLFindParam;
import com.yuqing.web.sqlfinder.entity.request.SQLFindParam.Column;
import com.yuqing.web.sqlfinder.entity.request.SQLFindParam.Database;
import com.yuqing.web.sqlfinder.entity.request.SQLFindParam.TableAndData;
import com.yuqing.web.sqlfinder.entity.response.SQLFindResponse;
import com.yuqing.web.sqlfinder.memdb.MapSchema;
import com.yuqing.web.sqlfinder.memdb.QueryResult;
import com.yuqing.web.sqlfinder.memdb.TableColumn;
import com.yuqing.web.sqlfinder.memdb.TableData;
import com.yuqing.web.sqlfinder.memdb.TableRow;
import com.yuqing.web.sqlfinder.service.MemoryDatabaseService;
import com.yuqing.web.sqlfinder.service.SQLFindService;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yuqing
 * @date 2024/3/9
 */
@Service
public class SQLFindServiceImpl implements SQLFindService {

  private static final Logger LOG = LoggerFactory.getLogger(SQLFindServiceImpl.class);

  @Autowired
  private MemoryDatabaseService memoryDatabaseService;

  @Override
  public SQLFindResponse find(SQLFindParam param) {
    check(param);

    List<MapSchema> schemaList = new ArrayList<>(param.getDatabases().size());

    for (Database database : param.getDatabases()) {
      List<TableData> tableDataList = createTableData(database);
      MapSchema schema = new MapSchema(database.getName(), tableDataList);
      schemaList.add(schema);
    }

    QueryResult queryResult = null;

    try {
      queryResult = memoryDatabaseService.executeQuery(param.getSqlInfo().getSqlTemplate(),
          param.getSqlInfo()
              .getArgs(), schemaList);
    } catch (SQLException e) {
      LOG.error("", e);
    }

    if (queryResult == null) {
      SQLFindResponse response = new SQLFindResponse();
      response.setReason("error");
      return response;
    }

    SQLFindResponse response = new SQLFindResponse();
    response.setReason("success");
    response.setResult(queryResult);

    return response;
  }

  private void check(SQLFindParam param) {

  }

  private List<TableData> createTableData(Database database) {
    List<TableData> tableDataList = new ArrayList<>(database.getTables().size());

    for (TableAndData tableAndData : database.getTables()) {
      TableData tableData = new TableData();
      tableData.setTableName(tableAndData.getTableName());
      tableData.setColumns(new ArrayList<>(tableAndData.getColumns().size()));
      tableData.setRows(new ArrayList<>(tableAndData.getRows().size()));

      for (Column column : tableAndData.getColumns()) {
        TableColumn tableColumn = new TableColumn(column.getName(), column.getType());
        tableData.getColumns().add(tableColumn);
      }

      for (Map<String, Object> row : tableAndData.getRows()) {
        tableData.getRows().add(new TableRow(row));
      }

      tableDataList.add(tableData);
    }

    return tableDataList;
  }
}

package com.yuqing.web.sqlfinder.service;

import com.yuqing.web.sqlfinder.entity.request.SQLFindParam.Argument;
import com.yuqing.web.sqlfinder.memdb.MapSchema;
import com.yuqing.web.sqlfinder.memdb.QueryResult;
import java.sql.SQLException;
import java.util.List;

/**
 * @author yuqing
 * @date 2024/3/10
 */
public interface MemoryDatabaseService {

  QueryResult executeQuery(String query, List<Argument> args, List<MapSchema> sourceList)
      throws SQLException;
}

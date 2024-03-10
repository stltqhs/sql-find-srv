package com.yuqing.web.sqlfinder.entity.request;

import java.util.List;
import java.util.Map;

/**
 * @author yuqing
 * @date 2024/3/9
 */
public class SQLFindParam {
    private SQLInfo sqlInfo;
    private List<TableAndData> tableDataList;

    /**
     * SQL 语句
     */
    public static class SQLInfo {
        /**
         * SQL 目标
         */
        private String sqlTemplate;
        /**
         * sqlTemplate 中的占位符
         */
        private List<Object> args;
    }

    /**
     * 表和数据
     */
    public static class TableAndData {
        private String tableName;
        private List<Column> columns;
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
        private String name;
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

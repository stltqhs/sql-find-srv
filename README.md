# Introduction

sql-find-srv is a web application that can find use submitted data by sql.

# Code Style

Google Code Style

# Example

```shell
curl -X POST -H 'Content-Type: application/json' -d '{"sqlInfo": {"sqlTemplate": "select * from db1.logs where traceid > ?", "args": [{"value": 1, "type": "int64"}]}, "databases": [{"name": "db1", "tables": [{"tableName": "logs", "columns": [{"name": "traceid", "type": "int64"}, {"name": "time", "type": "date"}, {"name": "msg", "type": "string"}], "rows": [{"traceid": 1, "time": "2024-03-8 01:01:01", "msg": "hello word"}, {"traceid": 2, "time": "2024-03-9 02:02:02", "msg": "hello wuz"}, {"traceid": 3, "time": "2024-03-10 03:03:03", "msg": "start application"}]}]}]}' 'http://localhost:8080/sql-finder/find'
```
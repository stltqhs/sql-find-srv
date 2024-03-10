package com.yuqing.web.sqlfinder.controller;

import com.yuqing.web.sqlfinder.entity.request.SQLFindParam;
import com.yuqing.web.sqlfinder.entity.response.SQLFindResponse;
import com.yuqing.web.sqlfinder.service.SQLFindService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuqing
 * @date 2024/3/9
 */
@RestController
@RequestMapping("/sql-finder")
public class SQLFinderController {
    @Resource
    private SQLFindService sqlFindService;

    @PostMapping("/find")
    public SQLFindResponse find(@RequestBody SQLFindParam sqlFindParam) {
        return sqlFindService.find(sqlFindParam);
    }
}

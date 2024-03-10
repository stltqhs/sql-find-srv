package com.yuqing.web.sqlfinder.service;

import com.yuqing.web.sqlfinder.entity.request.SQLFindParam;
import com.yuqing.web.sqlfinder.entity.response.SQLFindResponse;

/**
 * @author yuqing
 * @date 2024/3/9
 */
public interface SQLFindService {
    SQLFindResponse find(SQLFindParam param);
}

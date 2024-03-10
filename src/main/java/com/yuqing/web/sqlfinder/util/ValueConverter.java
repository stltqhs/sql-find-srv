package com.yuqing.web.sqlfinder.util;

import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yuqing
 * @date 2024/3/10
 */
public class ValueConverter {

  private static final Logger LOG = LoggerFactory.getLogger(ValueConverter.class);

  public static Object convertValue(Class type, Object value) {
    if (value == null) {
      return value;
    }

    if (value instanceof Number) {
      if (type == byte.class || type == short.class || type == int.class) {
        return ((Number) value).intValue();
      }

      if (type == long.class) {
        return ((Number) value).longValue();
      }

      if (type == float.class) {
        return ((Number) value).floatValue();
      }
      if (type == double.class) {
        return ((Number) value).doubleValue();
      }

      if (type == String.class) {
        return value.toString();
      }
    }

    if (type == Date.class) {
      value = convertToDate(value);
    }

    return value;
  }

  private static Object convertToDate(Object value) {
    if (value instanceof Date) {
      return new java.sql.Date(((Date) value).getTime());
    }
    if (value instanceof Number) {
      return new java.sql.Date(((Number) value).longValue());
    }
    if (value instanceof String) {
      return new java.sql.Date(parseDate((String) value).getTime());
    }
    return value;
  }

  private static Date parseDate(String value) {
    try {
      return DateUtils.parseDate(value, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS");
    } catch (ParseException e) {
      LOG.warn("parse date {}", value, e);
    }
    return new Date(0);
  }
}

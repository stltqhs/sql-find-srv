package com.yuqing.web.sqlfinder.util;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yuqing
 * @date 2024/3/10
 */
public class TypeNameUtil {

  public static final String INT8 = "int8";
  public static final String INT16 = "int16";
  public static final String INT32 = "int32";
  public static final String INT64 = "int64";
  public static final String FLOAT32 = "float32";
  public static final String FLOAT64 = "float64";
  public static final String STRING = "string";
  public static final String DATE = "date";
  public static final Set<String> SUPPORT_COLUMN_TYPES;

  static {
    SUPPORT_COLUMN_TYPES = HashSet.newHashSet(7);
    SUPPORT_COLUMN_TYPES.add(INT8);
    SUPPORT_COLUMN_TYPES.add(INT16);
    SUPPORT_COLUMN_TYPES.add(INT32);
    SUPPORT_COLUMN_TYPES.add(INT64);
    SUPPORT_COLUMN_TYPES.add(FLOAT32);
    SUPPORT_COLUMN_TYPES.add(FLOAT64);
    SUPPORT_COLUMN_TYPES.add(STRING);
    SUPPORT_COLUMN_TYPES.add(DATE);
  }

  public static Class convertType(String typeName) {
    if (typeName.equals(INT8)) {
      return byte.class;
    }
    if (typeName.equals(INT16)) {
      return short.class;
    }
    if (typeName.equals(INT32)) {
      return int.class;
    }
    if (typeName.equals(INT64)) {
      return long.class;
    }
    if (typeName.equals(STRING)) {
      return String.class;
    }
    if (typeName.equals(FLOAT32)) {
      return float.class;
    }
    if (typeName.equals(FLOAT64)) {
      return double.class;
    }
    if (typeName.equals(DATE)) {
      return Date.class;
    }
    throw new IllegalArgumentException("unsupported type " + typeName);
  }
}

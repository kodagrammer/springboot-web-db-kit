package com.koda.multitranx.database2.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SecondPoolMapper {
    List<Map<String, Object>> selectTransactionTest();
    int insertTransactionTest(String id);
}

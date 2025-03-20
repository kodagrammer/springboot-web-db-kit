package com.koda.multitranx.database1.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mapper
public interface DefaultPoolMapper {
    List<Map<String, Object>> selectTransactionTest();
    int insertTransactionTest(String id);
}

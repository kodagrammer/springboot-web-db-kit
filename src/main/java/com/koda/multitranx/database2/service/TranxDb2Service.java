package com.koda.multitranx.database2.service;

import com.koda.multitranx.database2.mapper.SecondPoolMapper;
import com.koda.multitranx.exception.TransactionTestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TranxDb2Service {
    private final SecondPoolMapper secondPoolMapper;

    public List<Map<String, Object>> getMyData() {
        List<Map<String, Object>> list = secondPoolMapper.selectTransactionTest();
        log.info(list.toString());
        return list;
    }

    public int insertMyData(String id) {
        return secondPoolMapper.insertTransactionTest(id);
    }

    public void throwExceptionDuringInsert(String id) {
        secondPoolMapper.insertTransactionTest(id);
        throw new TransactionTestException(String.format("### Exception 강제 발생 - id: %s", id));
    }
}

package com.koda.multitranx;

import com.koda.multitranx.database1.service.TranxDb1Service;
import com.koda.multitranx.database2.service.TranxDb2Service;
import com.koda.multitranx.exception.TransactionTestException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@SpringBootTest
class MultiTranxApplicationTests {

    @Autowired
    TranxDb1Service tranxDb1Service;
    @Autowired
    TranxDb2Service tranxDb2Service;

    private void checkExistDatabase(List<Map<String, Object>> result, String id, boolean existMode) {
        List<Object> idValues = result.stream()
                .map(map -> map.get("id"))
                .toList();
        assert idValues.contains(id) == existMode;
    }

    @Test
    @DisplayName("[단일 데이터소스 트랜잭션 처리] database1 트랜잭션 데이터 입력 테스트")
    void insertDefaultTrans() {
        String id = UUID.randomUUID().toString();
        int cnt = tranxDb1Service.insertMyData(id);

        assert cnt == 1;
        checkExistDatabase(tranxDb1Service.getMyData(), id, true);
    }

    @Test
    @DisplayName("[단일 데이터소스 트랜잭션 처리] database2 트랜잭션 데이터 입력 테스트")
    void insertSecondTrans() {
        String id = UUID.randomUUID().toString();
        int cnt = tranxDb2Service.insertMyData(id);

        assert cnt == 1;
        checkExistDatabase(tranxDb2Service.getMyData(), id, true);
    }

    @Test
    @DisplayName("[단일 데이터소스 트랜잭션 처리] database1 트랜잭션 에러 롤백 테스트")
    void rollbackExceptionDefaultTrans() {
        String id = UUID.randomUUID().toString();

        try {
            tranxDb1Service.throwExceptionDuringInsert(id);
        } catch (TransactionTestException e) {
            log.info(e.getMessage());
            assert e.getMessage().contains(id);
        }

        checkExistDatabase(tranxDb1Service.getMyData(), id, false);
    }

    @Test
    @DisplayName("[단일 데이터소스 트랜잭션 처리] database2 트랜잭션 에러 롤백 테스트")
    void rollbackExceptionSecondTrans() {
        String id = UUID.randomUUID().toString();

        try {
            tranxDb2Service.throwExceptionDuringInsert(id);
        } catch (TransactionTestException e) {
            log.info(e.getMessage());
            assert e.getMessage().contains(id);
        }

        checkExistDatabase(tranxDb2Service.getMyData(), id, false);
    }

    @Test
    @DisplayName("[단일 데이터소스 트랜잭션 처리] 멀티 데이터베이스 로직 롤백 테스트(Default -> Second 순서)")
    void rollbackExceptionDefaultToSecondTrans(){
        String firstId = UUID.randomUUID().toString();
        String secondId = UUID.randomUUID().toString();

        try {
            // Default Trans Pool 정상 INSERT
            tranxDb1Service.insertMyData(firstId);
            // Second Trans Pool 에러 INSERT
            tranxDb2Service.throwExceptionDuringInsert(secondId);
        } catch (TransactionTestException e) {
            log.info(e.getMessage());
            assert e.getMessage().contains(secondId);
        }

        checkExistDatabase(tranxDb1Service.getMyData(), firstId, true);
        checkExistDatabase(tranxDb2Service.getMyData(), secondId, false);
    }

    @Test
    @DisplayName("[단일 데이터소스 트랜잭션 처리] 멀티 데이터베이스 로직 롤백 테스트(Second -> Default 순서)")
    void rollbackExceptionSecoundToDefaultTrans(){
        String firstId = UUID.randomUUID().toString();
        String secondId = UUID.randomUUID().toString();

        try {
            // Second Trans Pool 정상 INSERT
            tranxDb2Service.insertMyData(secondId);
            // Default Trans Pool 에러 INSERT
            tranxDb1Service.throwExceptionDuringInsert(firstId);
        } catch (TransactionTestException e) {
            log.info(e.getMessage());
            assert e.getMessage().contains(firstId);
        }

        checkExistDatabase(tranxDb1Service.getMyData(), firstId, false);
        checkExistDatabase(tranxDb2Service.getMyData(), secondId, true);
    }
}

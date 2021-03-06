package com.diego.hernando.orchestTest.business.worksign.service;

import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.model.WorkSignRecordType;
import com.diego.hernando.orchestTest.model.WorkSignType;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class WorkSignOperationsServiceTest {

    private final WorkSignOperationsService wSignOpSrv = new WorkSignOperationsService();
    private final WorkSignDto.WorkSignDtoBuilder builderDto = WorkSignDto.builder().businessId("01").employeeId("1")
            .date(new Date()).serviceId("service");

    @Test
    public void true_when_dto_rType_is_IN_with_isInRecordTypeWorkSign () {
        assertThat(wSignOpSrv.isInRecordTypeWSign(
                builderDto.recordType(WorkSignRecordType.IN)
                        .type(WorkSignType.WORK).build()),
                is(true));
        assertThat(wSignOpSrv.isInRecordTypeWSign(
                builderDto.recordType(WorkSignRecordType.IN)
                        .type(WorkSignType.REST).build()),
                is(true));
    }

    @Test
    public void false_when_dto_rType_is_OUT_with_isInRecordTypeWorkSign () {
        assertThat(wSignOpSrv.isInRecordTypeWSign(
                builderDto.recordType(WorkSignRecordType.OUT)
                        .type(WorkSignType.WORK).build()),
                is(false));
        assertThat(wSignOpSrv.isInRecordTypeWSign(
                builderDto.recordType(WorkSignRecordType.OUT)
                        .type(WorkSignType.REST).build()),
                is(false));
    }

    @Test
    public void true_when_dto_rType_is_OUT_isOutRecordTypeWorkSign () {
        assertThat(wSignOpSrv.isOutRecordTypeWSign(
                builderDto.recordType(WorkSignRecordType.OUT)
                        .type(WorkSignType.WORK).build()),
                is(true));
        assertThat(wSignOpSrv.isOutRecordTypeWSign(
                builderDto.recordType(WorkSignRecordType.OUT)
                        .type(WorkSignType.REST).build()),
                is(true));
    }

    @Test
    public void false_when_dto_rType_is_IN_isOutRecordTypeWorkSign () {
        assertThat(wSignOpSrv.isOutRecordTypeWSign(
                builderDto.recordType(WorkSignRecordType.IN)
                        .type(WorkSignType.WORK).build()),
                is(false));
        assertThat(wSignOpSrv.isOutRecordTypeWSign(
                builderDto.recordType(WorkSignRecordType.IN)
                        .type(WorkSignType.REST).build()),
                is(false));
    }

    @Test
    public void true_when_dto_rType_is_IN_and_type_is_WORK_isInRecordTypeAndWorkTypeWorkSign () {
        assertThat(wSignOpSrv.isInRecordTypeAndWorkTypeWSign(
                builderDto.recordType(WorkSignRecordType.IN)
                        .type(WorkSignType.WORK).build()),
                is(true));

    }

    @Test
    public void false_when_dto_rType_not_is_IN_or_type_is_not_WORK_isInRecordTypeAndWorkTypeWorkSign () {
        assertThat(wSignOpSrv.isInRecordTypeAndWorkTypeWSign(
                builderDto.recordType(WorkSignRecordType.IN)
                        .type(WorkSignType.REST).build()),
                is(false));
        assertThat(wSignOpSrv.isInRecordTypeAndWorkTypeWSign(
                builderDto.recordType(WorkSignRecordType.OUT)
                        .type(WorkSignType.WORK).build()),
                is(false));
        assertThat(wSignOpSrv.isInRecordTypeAndWorkTypeWSign(
                builderDto.recordType(WorkSignRecordType.OUT)
                        .type(WorkSignType.REST).build()),
                is(false));

    }

    @Test
    public void true_when_dto_rType_is_IN_and_type_is_REST_isInRecordTypeAndWorkTypeWorkSign () {
        assertThat(wSignOpSrv.isInRecordTypeAndRestTypeWSign(
                builderDto.recordType(WorkSignRecordType.IN)
                        .type(WorkSignType.REST).build()),
                is(true));

    }

    @Test
    public void false_when_dto_rType_not_is_IN_or_type_is_not_REST_isInRecordTypeAndWorkTypeWorkSign () {
        assertThat(wSignOpSrv.isInRecordTypeAndRestTypeWSign(
                builderDto.recordType(WorkSignRecordType.IN)
                        .type(WorkSignType.WORK).build()),
                is(false));
        assertThat(wSignOpSrv.isInRecordTypeAndRestTypeWSign(
                builderDto.recordType(WorkSignRecordType.OUT)
                        .type(WorkSignType.REST).build()),
                is(false));
        assertThat(wSignOpSrv.isInRecordTypeAndRestTypeWSign(
                builderDto.recordType(WorkSignRecordType.OUT)
                        .type(WorkSignType.WORK).build()),
                is(false));
    }

    @Test
    public void true_when_dto_rType_is_OUT_and_type_is_WORK_isInRecordTypeAndWorkTypeWorkSign () {
        assertThat(wSignOpSrv.isOutRecordTypeAndWorkTypeWSign(
                builderDto.recordType(WorkSignRecordType.OUT)
                        .type(WorkSignType.WORK).build()),
                is(true));
    }

    @Test
    public void false_when_dto_rType_not_is_OUT_or_type_is_not_WORK_isInRecordTypeAndWorkTypeWorkSign () {
        assertThat(wSignOpSrv.isOutRecordTypeAndWorkTypeWSign(
                builderDto.recordType(WorkSignRecordType.OUT)
                        .type(WorkSignType.REST).build()),
                is(false));
        assertThat(wSignOpSrv.isOutRecordTypeAndWorkTypeWSign(
                builderDto.recordType(WorkSignRecordType.IN)
                        .type(WorkSignType.WORK).build()),
                is(false));
        assertThat(wSignOpSrv.isOutRecordTypeAndWorkTypeWSign(
                builderDto.recordType(WorkSignRecordType.IN)
                        .type(WorkSignType.REST).build()),
                is(false));
    }

    @Test
    public void true_when_dto_rType_is_OUT_and_type_is_REST_isInRecordTypeAndWorkTypeWorkSign () {
        assertThat(wSignOpSrv.isOutRecordTypeAndRestTypeWSign(
                builderDto.recordType(WorkSignRecordType.OUT)
                        .type(WorkSignType.REST).build()),
                is(true));
    }

    @Test
    public void false_when_dto_rType_not_is_OUT_or_type_is_not_REST_isInRecordTypeAndWorkTypeWorkSign () {
        assertThat(wSignOpSrv.isOutRecordTypeAndRestTypeWSign(
                builderDto.recordType(WorkSignRecordType.OUT)
                        .type(WorkSignType.WORK).build()),
                is(false));
        assertThat(wSignOpSrv.isOutRecordTypeAndRestTypeWSign(
                builderDto.recordType(WorkSignRecordType.IN)
                        .type(WorkSignType.REST).build()),
                is(false));
        assertThat(wSignOpSrv.isOutRecordTypeAndRestTypeWSign(
                builderDto.recordType(WorkSignRecordType.IN)
                        .type(WorkSignType.WORK).build()),
                is(false));
    }





}

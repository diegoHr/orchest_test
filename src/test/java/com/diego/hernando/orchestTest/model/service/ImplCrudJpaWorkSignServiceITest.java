package com.diego.hernando.orchestTest.model.service;

import com.diego.hernando.orchestTest.model.WorkSignEntity;
import com.diego.hernando.orchestTest.model.WorkSignRecordType;
import com.diego.hernando.orchestTest.model.WorkSignType;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.Matchers.is;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Transactional //limpia la bd en cada test
public class ImplCrudJpaWorkSignServiceITest {

    @Autowired
    private ICrudWorkSignService service;
    private final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");

    private final String defaultBusinessId = "orchest_01";
    private final String defaultDate = "5/05/2020 10:20:20";

    private WorkSignEntity.WorkSignEntityBuilder builderEntity = WorkSignEntity.builder()
            .businessId(defaultBusinessId)
            .employeeId("01")
            .date(parseDate(defaultDate))
            .recordType(WorkSignRecordType.IN)
            .serviceId("phone")
            .type(WorkSignType.WORK);


    @Test
    public void save_and_recover_ok_workSignEntities (){

        service.save(builderEntity.build());
        service.save(builderEntity.
                date(parseDate("25/05/2020 16:20:20"))
                .recordType(WorkSignRecordType.OUT)
                .build());
        service.save(builderEntity.
                date(parseDate("25/05/2020 10:20:20"))
                .employeeId("02")
                .recordType(WorkSignRecordType.OUT)
                .build());

        assertThat(service.count(), is(3L));
        assertThat(service.findWorkSignsOfWorker(defaultBusinessId, "01").size(), is(2));

    }

    @Test
    public void when_save_one_workSignEntity_with_id_that_exists_yet_modify_old(){
        WorkSignEntity entitySaved = service.save(builderEntity.build());
        assertThat(entitySaved.getId(), Matchers.notNullValue());
        service.save(builderEntity.id(entitySaved.getId()).employeeId("02").build());
        assertThat(service.count(),is(1L));
        assertThat(service.findWorkSignsOfWorker( defaultBusinessId,"01").size(),is(0));
    }

    @Test
    public void when_save_two_workSignEntities_with_same_id_throw_error_and_transaction_is_refused(){
        List<WorkSignEntity> entities = new ArrayList<>();
        entities.add(WorkSignEntity.builder().id(1L).build());
        entities.add(WorkSignEntity.builder().id(1L).build());
        assertThrows(DataIntegrityViolationException.class,()->{
            service.saveAll(entities);
        });
        assertThat(service.findById(1L).isPresent(), is(false));
    }

    private Date parseDate (String date){
        return formatter.parseDateTime(date).toDate();
    }


}

package com.diego.hernando.orchestTest.business.service;

import com.diego.hernando.orchestTest.business.WorkSignDto;
import com.diego.hernando.orchestTest.model.WorkSignEntity;
import com.diego.hernando.orchestTest.model.service.ICrudWorkSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier("TransformJsonCrudWorkingSignService")
public class ImplTransformJsonCrudWorkSignService implements ITransformJsonCrudWorkSignService {
    @Autowired
    private ICrudWorkSignService crudWorkSignService;

    @Override
    public WorkSignDto getDto(WorkSignEntity entity) {
        return WorkSignDto.builder()
                .businessId(entity.getBusinessId())
                .date(entity.getDate())
                .employeeId(entity.getEmployeeId())
                .recordType(entity.getRecordType())
                .serviceId(entity.getServiceId())
                .type(entity.getType())
                .build();
    }

    @Override
    public WorkSignEntity getEntity(WorkSignDto dto) {
        return WorkSignEntity.builder()
                .businessId(dto.getBusinessId())
                .date(dto.getDate())
                .employeeId(dto.getEmployeeId())
                .recordType(dto.getRecordType())
                .serviceId(dto.getServiceId())
                .type(dto.getType())
                .build();
    }

    @Override
    public WorkSignEntity getEntitySaved(WorkSignDto dto) {

        return crudWorkSignService.save(getEntity(dto));
    }

    @Override
    public List<WorkSignDto> getListDto(List<WorkSignEntity> entities) {
        return entities.stream().map(this::getDto).collect(Collectors.toList());
    }

    @Override
    public List<WorkSignEntity> getListEntities(List<WorkSignDto> dtos) {
        return dtos.stream().map(this::getEntity).collect(Collectors.toList());
    }

    @Override
    public List<WorkSignEntity> getListEntitiesSaved(List<WorkSignDto> dtos) {
        return crudWorkSignService.saveAll(dtos.stream().map(this::getEntity).collect(Collectors.toList()));
    }
}

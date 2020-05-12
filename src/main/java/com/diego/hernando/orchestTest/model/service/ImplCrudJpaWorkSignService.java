package com.diego.hernando.orchestTest.model.service;

import com.diego.hernando.orchestTest.model.IJpaWorkSignRepository;
import com.diego.hernando.orchestTest.model.WorkSignEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Qualifier("CrudJpaWorkSignService")
public class ImplCrudJpaWorkSignService implements ICrudWorkSignService {

    @Autowired
    private IJpaWorkSignRepository repository;

    @Override
    public Optional<WorkSignEntity> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public WorkSignEntity save(WorkSignEntity entity) {
        return repository.save(entity);
    }

    @Override
    public List<WorkSignEntity> saveAll(List<WorkSignEntity> collection) {
        List<WorkSignEntity> entities = new ArrayList<>(collection.size());
        repository.saveAll(collection).iterator().forEachRemaining(entities::add);
        return entities;
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public List<WorkSignEntity> findWorkSignsOfWorker(String businessId, String workerId) {
        return repository.findByBusinessIdAndEmployeeIdOrderByDateAsc(businessId, workerId);
    }


}

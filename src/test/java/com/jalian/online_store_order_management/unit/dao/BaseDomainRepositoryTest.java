package com.jalian.online_store_order_management.unit.dao;

import com.jalian.online_store_order_management.domain.BaseDomain;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public abstract class BaseDomainRepositoryTest<Value, Id, Repository extends JpaRepository<Value, Id>> {

    protected Repository repository;
    protected Value instanceToTest;

    public abstract void initializeValue();

    protected abstract Value updateValue();

    protected abstract void updateMethodValidation(Value updatedInstance);

    protected abstract List<Value> findAllEntities();

    protected Id id() {
        if (instanceToTest instanceof BaseDomain baseDomain) {
            return (Id) baseDomain.getId();
        }
        return null;
    }

    public BaseDomainRepositoryTest(Repository repository) {
        this.repository = repository;
    }

    @Test
    protected void save() {
        Value savedValue = repository.save(instanceToTest);
        assertThat(savedValue).isNotNull();
        if (savedValue instanceof BaseDomain baseDomain) {
            assertThat(baseDomain.getId()).isGreaterThan(0);
        }
    }

    @Test
    protected void findById() {
        repository.save(instanceToTest);
        Optional<Value> founded = repository.findById(id());
        assertThat(founded).isNotNull();
        assertThat(founded.isPresent()).isTrue();
    }

    @Test
    protected void update() {
        instanceToTest = repository.save(instanceToTest);
        instanceToTest = updateValue();
        Value updated = repository.save(instanceToTest);
        updateMethodValidation(updated);
    }

    @Test
    void delete() {
        repository.save(instanceToTest);
        repository.deleteById(id());
        Optional<Value> deleted = repository.findById(id());
        assertThat(deleted).isEmpty();
    }

    @Test
    void findAll() {
        var allEntities = findAllEntities();
        repository.deleteAll();
        repository.saveAll(allEntities);
        List<Value> values = repository.findAll();
        assertThat(values).isNotNull();
        assertThat(values.size()).isEqualTo(allEntities.size());
    }
}

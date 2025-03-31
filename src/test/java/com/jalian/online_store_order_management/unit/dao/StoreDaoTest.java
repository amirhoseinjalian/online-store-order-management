package com.jalian.online_store_order_management.unit.dao;

import com.jalian.online_store_order_management.dao.StoreDao;
import com.jalian.online_store_order_management.domain.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StoreDaoTest extends BaseDomainRepositoryTest<Store, Long, StoreDao> {

    public StoreDaoTest(@Autowired StoreDao repository) {
        super(repository);
    }

    @Override
    @BeforeEach
    public void initializeValue() {
        instanceToTest = new Store("Test Store " + UUID.randomUUID());
        instanceToTest.setUsers(new ArrayList<>());
    }

    @Test
    public void testFindByIdSafe_whenStoreExists_returnsStore() {
        Store store = new Store("Store A");
        store.setUsers(new ArrayList<>());
        Store savedStore = repository.save(store);

        var foundStore = repository.findByIdSafe(savedStore.getId());

        assertThat(foundStore).isPresent();
        assertThat(foundStore.get().getId()).isEqualTo(savedStore.getId());
        assertThat(foundStore.get().getName()).isEqualTo("Store A");
    }

    @Test
    public void testFindByIdSafe_whenStoreDoesNotExist_returnsEmptyOptional() {
        var foundStore = repository.findByIdSafe(999L);
        assertThat(foundStore).isNotPresent();
    }

    @Test
    public void testFindByNameSafe_whenStoreExists_returnsStore() {
        var store = new Store("Unique Store");
        store.setUsers(new ArrayList<>());
        Store savedStore = repository.save(store);

        var foundStore = repository.findByNameSafe("Unique Store");

        assertThat(foundStore).isPresent();
        assertThat(foundStore.get().getId()).isEqualTo(savedStore.getId());
        assertThat(foundStore.get().getName()).isEqualTo("Unique Store");
    }

    @Test
    public void testFindByNameSafe_whenStoreDoesNotExist_returnsEmptyOptional() {
        var foundStore = repository.findByNameSafe("NonExistent Store");
        assertThat(foundStore).isNotPresent();
    }

    @Override
    protected Store updateValue() {
        instanceToTest.setName("Updated Test Store");
        return instanceToTest;
    }

    @Override
    protected void updateMethodValidation(Store updatedInstance) {
        assertThat(updatedInstance).isNotNull();
        assertThat(updatedInstance.getName()).isEqualTo("Updated Test Store");
    }

    @Override
    protected List<Store> findAllEntities() {
        var store1 = new Store("Store 1");
        store1.setUsers(new ArrayList<>());

        var store2 = new Store("Store 2");
        store2.setUsers(new ArrayList<>());

        var store3 = new Store("Store 3");
        store3.setUsers(new ArrayList<>());

        return List.of(store1, store2, store3);
    }
}
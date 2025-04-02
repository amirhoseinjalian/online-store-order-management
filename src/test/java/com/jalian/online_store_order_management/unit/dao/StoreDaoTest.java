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

/**
 * The StoreDaoTest class provides unit tests for the {@link StoreDao} repository.
 * <p>
 * It extends the {@link BaseDomainRepositoryTest} to leverage common CRUD test cases, and it includes
 * additional tests for custom query methods such as {@code findByIdSafe} and {@code findByNameSafe}.
 * </p>
 *
 * @author amirhosein jalian
 */
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StoreDaoTest extends BaseDomainRepositoryTest<Store, Long, StoreDao> {

    /**
     * Constructs a new StoreDaoTest with the specified {@link StoreDao} repository.
     *
     * @param repository the {@link StoreDao} repository to be tested.
     */
    public StoreDaoTest(@Autowired StoreDao repository) {
        super(repository);
    }

    /**
     * Initializes the {@code instanceToTest} with a new {@link Store} instance and an empty list of users.
     */
    @Override
    @BeforeEach
    public void initializeValue() {
        instanceToTest = new Store("Test Store " + UUID.randomUUID());
        instanceToTest.setUsers(new ArrayList<>());
    }

    /**
     * Tests that {@code findByIdSafe} returns the expected store when it exists.
     */
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

    /**
     * Tests that {@code findByIdSafe} returns an empty Optional when the store does not exist.
     */
    @Test
    public void testFindByIdSafe_whenStoreDoesNotExist_returnsEmptyOptional() {
        var foundStore = repository.findByIdSafe(999L);
        assertThat(foundStore).isNotPresent();
    }

    /**
     * Tests that {@code findByNameSafe} returns the expected store when a store with the given name exists.
     */
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

    /**
     * Tests that {@code findByNameSafe} returns an empty Optional when no store with the given name exists.
     */
    @Test
    public void testFindByNameSafe_whenStoreDoesNotExist_returnsEmptyOptional() {
        var foundStore = repository.findByNameSafe("NonExistent Store");
        assertThat(foundStore).isNotPresent();
    }

    /**
     * Updates the {@code instanceToTest} with new values for update testing.
     *
     * @return the updated {@link Store} instance.
     */
    @Override
    protected Store updateValue() {
        instanceToTest.setName("Updated Test Store");
        return instanceToTest;
    }

    /**
     * Validates that the updated {@link Store} instance has the expected new name.
     *
     * @param updatedInstance the updated {@link Store} instance.
     */
    @Override
    protected void updateMethodValidation(Store updatedInstance) {
        assertThat(updatedInstance).isNotNull();
        assertThat(updatedInstance.getName()).isEqualTo("Updated Test Store");
    }

    /**
     * Provides a list of {@link Store} entities for testing the {@code findAll} operation.
     *
     * @return a list containing multiple {@link Store} instances.
     */
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

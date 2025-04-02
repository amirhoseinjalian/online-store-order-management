package com.jalian.online_store_order_management.unit.dao;

import com.jalian.online_store_order_management.domain.BaseDomain;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * BaseDomainRepositoryTest is an abstract test class providing common CRUD test cases for JPA repositories.
 * <p>
 * It is designed to work with entities extending {@link BaseDomain} and allows testing of standard operations
 * like save, findById, update, delete, and findAll.
 * </p>
 *
 * @param <Value>      the type of the entity to be tested, which extends {@link BaseDomain}.
 * @param <Id>         the type of the entity's identifier.
 * @param <Repository> the type of the repository that extends {@link JpaRepository}.
 *
 * @author amirhosein jalian
 */
public abstract class BaseDomainRepositoryTest<Value, Id, Repository extends JpaRepository<Value, Id>> {

    /**
     * The repository instance used for CRUD operations in tests.
     */
    protected Repository repository;

    /**
     * The instance of the entity to be tested.
     */
    protected Value instanceToTest;

    /**
     * Initializes the entity instance to be tested.
     * <p>
     * This method should be implemented to create and initialize the entity instance.
     * </p>
     */
    public abstract void initializeValue();

    /**
     * Returns an updated instance of the entity for update testing.
     *
     * @return an updated instance of the entity.
     */
    protected abstract Value updateValue();

    /**
     * Validates the updated instance of the entity.
     * <p>
     * This method should assert that the update operation has been applied correctly.
     * </p>
     *
     * @param updatedInstance the updated entity instance.
     */
    protected abstract void updateMethodValidation(Value updatedInstance);

    /**
     * Returns a list of all entities for findAll testing.
     *
     * @return a list of entities.
     */
    protected abstract List<Value> findAllEntities();

    /**
     * Retrieves the identifier of the current instance under test.
     *
     * @return the identifier of the entity.
     */
    protected Id id() {
        if (instanceToTest instanceof BaseDomain baseDomain) {
            return (Id) baseDomain.getId();
        }
        return null;
    }

    /**
     * Constructs a new BaseDomainRepositoryTest with the specified repository.
     *
     * @param repository the repository to be used in the tests.
     */
    public BaseDomainRepositoryTest(Repository repository) {
        this.repository = repository;
    }

    /**
     * Tests saving an entity.
     * <p>
     * The test asserts that the saved entity is not null and that its identifier is greater than 0.
     * </p>
     */
    @Test
    protected void save() {
        Value savedValue = repository.save(instanceToTest);
        assertThat(savedValue).isNotNull();
        if (savedValue instanceof BaseDomain baseDomain) {
            assertThat(baseDomain.getId()).isGreaterThan(0);
        }
    }

    /**
     * Tests finding an entity by its identifier.
     * <p>
     * The test asserts that the entity is found successfully.
     * </p>
     */
    @Test
    protected void findById() {
        repository.save(instanceToTest);
        Optional<Value> founded = repository.findById(id());
        assertThat(founded).isNotNull();
        assertThat(founded.isPresent()).isTrue();
    }

    /**
     * Tests updating an entity.
     * <p>
     * The test saves the entity, updates it using the {@link #updateValue()} method, and then saves it again.
     * The updated entity is validated using the {@link #updateMethodValidation(Value)} method.
     * </p>
     */
    @Test
    protected void update() {
        instanceToTest = repository.save(instanceToTest);
        instanceToTest = updateValue();
        Value updated = repository.save(instanceToTest);
        updateMethodValidation(updated);
    }

    /**
     * Tests deleting an entity.
     * <p>
     * The test saves the entity, deletes it by its identifier, and asserts that the entity cannot be found.
     * </p>
     */
    @Test
    void delete() {
        repository.save(instanceToTest);
        repository.deleteById(id());
        Optional<Value> deleted = repository.findById(id());
        assertThat(deleted).isEmpty();
    }

    /**
     * Tests retrieving all entities.
     * <p>
     * The test deletes all entities from the repository, saves a list of entities using {@link #findAllEntities()},
     * and asserts that the number of retrieved entities matches the expected count.
     * </p>
     */
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

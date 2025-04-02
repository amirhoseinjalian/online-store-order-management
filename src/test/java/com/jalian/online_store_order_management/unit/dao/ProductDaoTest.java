package com.jalian.online_store_order_management.unit.dao;

import com.jalian.online_store_order_management.dao.ProductDao;
import com.jalian.online_store_order_management.dao.StoreDao;
import com.jalian.online_store_order_management.domain.Product;
import com.jalian.online_store_order_management.domain.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The ProductDaoTest class provides unit tests for the {@link ProductDao} repository.
 * <p>
 * It extends {@link BaseDomainRepositoryTest} to reuse common CRUD test cases. In addition, it
 * tests the custom method {@code findByIdSafe} to ensure it behaves as expected when a product exists or does not exist.
 * </p>
 *
 * @author amirhosein jalian
 */
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductDaoTest extends BaseDomainRepositoryTest<Product, Long, ProductDao> {

    private final Store store = new Store("store to test products");

    /**
     * Constructor for ProductDaoTest.
     *
     * @param repository the {@link ProductDao} repository to be tested.
     * @param storeDao   the {@link StoreDao} repository used to save the store.
     */
    public ProductDaoTest(@Autowired ProductDao repository, @Autowired StoreDao storeDao) {
        super(repository);
        storeDao.save(store);
    }

    /**
     * Tests that {@code findByIdSafe} returns a non-empty {@link Optional} when the product exists.
     */
    @Test
    public void testFindByIdSafe_whenProductExists_returnsProduct() {
        var savedProduct = repository.save(instanceToTest);
        Optional<Product> foundProduct = repository.findByIdSafe(savedProduct.getId());
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getId()).isEqualTo(savedProduct.getId());
    }

    /**
     * Tests that {@code findByIdSafe} returns an empty {@link Optional} when the product does not exist.
     */
    @Test
    public void testFindByIdSafe_whenProductDoesNotExist_returnsEmptyOptional() {
        Optional<Product> foundProduct = repository.findByIdSafe(999L);
        assertThat(foundProduct).isNotPresent();
    }

    /**
     * Initializes the instance of the product to be tested.
     */
    @Override
    @BeforeEach
    public void initializeValue() {
        instanceToTest = new Product();
        instanceToTest.setName("Test Product");
        instanceToTest.setDescription("Test Description");
        instanceToTest.setPrice(99.99);
        instanceToTest.setStore(store);
    }

    /**
     * Updates the current instance of the product with new values for update testing.
     *
     * @return the updated product instance.
     */
    @Override
    protected Product updateValue() {
        instanceToTest.setName("Updated Test Product");
        instanceToTest.setDescription("Updated Test Description");
        instanceToTest.setPrice(1000);
        return instanceToTest;
    }

    /**
     * Validates that the updated product instance has the expected updated values.
     *
     * @param updatedInstance the updated product instance.
     */
    @Override
    protected void updateMethodValidation(Product updatedInstance) {
        assertThat(updatedInstance).isNotNull();
        assertThat(updatedInstance.getName()).isEqualTo("Updated Test Product");
        assertThat(updatedInstance.getDescription()).isEqualTo("Updated Test Description");
        assertThat(updatedInstance.getPrice()).isEqualTo(1000);
    }

    /**
     * Provides a list of product entities for testing {@code findAll} operation.
     *
     * @return a list containing several product instances.
     */
    @Override
    protected List<Product> findAllEntities() {
        var productOne = new Product();
        productOne.setName("Test Product");
        productOne.setDescription("Test Description");
        productOne.setPrice(99.99);
        productOne.setStore(store);

        var productTwo = new Product();
        productTwo.setName("Test Product");
        productTwo.setDescription("Test Description");
        productTwo.setPrice(99.99);
        productTwo.setStore(store);

        var productThree = new Product();
        productThree.setName("Test Product");
        productThree.setDescription("Test Description");
        productThree.setPrice(99.99);
        productThree.setStore(store);

        return List.of(productOne, productTwo, productThree);
    }
}

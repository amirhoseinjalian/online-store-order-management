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

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductDaoTest extends BaseDomainRepositoryTest<Product, Long, ProductDao> {

    private final Store store = new Store("store to test products");

    public ProductDaoTest(@Autowired ProductDao repository, @Autowired StoreDao storeDao) {
        super(repository);
        storeDao.save(store);
    }

    @Test
    public void testFindByIdSafe_whenProductExists_returnsProduct() {
        var savedProduct = repository.save(instanceToTest);
        Optional<Product> foundProduct = repository.findByIdSafe(savedProduct.getId());
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getId()).isEqualTo(savedProduct.getId());
    }

    @Test
    public void testFindByIdSafe_whenProductDoesNotExist_returnsEmptyOptional() {
        Optional<Product> foundProduct = repository.findByIdSafe(999L);
        assertThat(foundProduct).isNotPresent();
    }

    @Override
    @BeforeEach
    public void initializeValue() {
        instanceToTest = new Product();
        instanceToTest.setName("Test Product");
        instanceToTest.setDescription("Test Description");
        instanceToTest.setPrice(99.99);
        instanceToTest.setStore(store);
    }

    @Override
    protected Product updateValue() {
        instanceToTest.setName("Updated Test Product");
        instanceToTest.setDescription("Updated Test Description");
        instanceToTest.setPrice(1000);
        return instanceToTest;
    }

    @Override
    protected void updateMethodValidation(Product updatedInstance) {
        assertThat(updatedInstance).isNotNull();
        assertThat(updatedInstance.getName()).isEqualTo("Updated Test Product");
        assertThat(updatedInstance.getDescription()).isEqualTo("Updated Test Description");
        assertThat(updatedInstance.getPrice()).isEqualTo(1000);
    }

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


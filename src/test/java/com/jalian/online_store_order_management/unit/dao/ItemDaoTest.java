package com.jalian.online_store_order_management.unit.dao;

import com.jalian.online_store_order_management.dao.ItemDao;
import com.jalian.online_store_order_management.dao.OrderDao;
import com.jalian.online_store_order_management.dao.ProductDao;
import com.jalian.online_store_order_management.dao.StoreDao;
import com.jalian.online_store_order_management.dao.UserDao;
import com.jalian.online_store_order_management.domain.Item;
import com.jalian.online_store_order_management.domain.Order;
import com.jalian.online_store_order_management.domain.Product;
import com.jalian.online_store_order_management.domain.Store;
import com.jalian.online_store_order_management.domain.User;
import com.jalian.online_store_order_management.domain.key.ItemKey;
import com.jalian.online_store_order_management.constant.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The ItemDaoTest class contains unit tests for the {@link ItemDao} repository.
 * <p>
 * These tests validate the basic CRUD operations (save, find, update, delete, and findAll)
 * as well as custom query methods such as {@code findAllByOrder}.
 * </p>
 *
 * @author amirhosein jalian
 */
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ItemDaoTest {

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private StoreDao storeDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TestEntityManager em;

    private Store store;
    private User user;
    private Product product;
    private Order order;
    private Item item;

    @BeforeEach
    public void setup() {
        store = storeDao.save(new Store("Store " + UUID.randomUUID()));
        user = userDao.save(new User("user" + UUID.randomUUID(), "pass"));
        product = productDao.save(new Product(store, 100.0, "Prod Desc", "Prod Name"));
        order = orderDao.save(new Order(OrderStatus.INITIALIZED, user, store));
        item = new Item(new ItemKey(order.getId(), product.getId()), product, order, 5, product.getInventory(), product.getPrice());
    }

    @Test
    public void testSaveItem() {
        var savedItem = itemDao.save(item);
        assertThat(savedItem).isNotNull();
        assertThat(savedItem.getPrimaryKey()).isNotNull();
    }

    @Test
    public void testFindById() {
        var savedItem = itemDao.save(item);
        var found = itemDao.findById(savedItem.getPrimaryKey());
        assertThat(found).isPresent();
        assertThat(found.get().getCount()).isEqualTo(5);
    }

    @Test
    public void testUpdateItem() {
        var savedItem = itemDao.save(item);
        savedItem.setCount(10);
        savedItem.setPrice(80.0);
        var updated = itemDao.save(savedItem);
        assertThat(updated.getCount()).isEqualTo(10);
        assertThat(updated.getPrice()).isEqualTo(80.0);
    }

    @Test
    public void testDeleteItem() {
        var savedItem = itemDao.save(item);
        itemDao.deleteById(savedItem.getPrimaryKey());
        var found = itemDao.findById(savedItem.getPrimaryKey());
        assertThat(found).isEmpty();
    }

    @Test
    public void testFindAll() {
        itemDao.save(item);
        var product2  = productDao.save(new Product(store, 100.0, "Prod Desc", "Prod Name"));
        itemDao.save(new Item(new ItemKey(order.getId(), product2.getId()), product2, order, 7, product.getInventory(), product.getPrice()));
        var allItems = itemDao.findAll();
        assertThat(allItems.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testFindAllByOrder() {
        itemDao.save(item);
        List<Item> items = itemDao.findAllByOrder(order.getId());
        assertThat(items).isNotEmpty();
        var savedItem = items.get(0);
        assertThat(savedItem.getOrder().getId()).isEqualTo(order.getId());
    }
}

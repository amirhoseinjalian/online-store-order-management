package com.jalian.online_store_order_management.unit.dao;

import com.jalian.online_store_order_management.dao.UserDao;
import com.jalian.online_store_order_management.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The UserDaoTest class provides unit tests for the {@link UserDao} repository.
 * <p>
 * It extends the {@link BaseDomainRepositoryTest} to reuse common CRUD test cases, and it includes
 * additional tests for custom query methods such as {@code findByUsername}, {@code findUserById}, and
 * {@code findUserByIdForUpdate}.
 * </p>
 *
 * @author amirhosein jalian
 */
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoTest extends BaseDomainRepositoryTest<User, Long, UserDao> {

    /**
     * Constructor for UserDaoTest.
     *
     * @param repository the {@link UserDao} repository to be tested.
     */
    public UserDaoTest(@Autowired UserDao repository) {
        super(repository);
    }

    /**
     * Initializes the {@code instanceToTest} with a new {@link User} instance before each test.
     */
    @Override
    @BeforeEach
    public void initializeValue() {
        instanceToTest = new User("test_user" + UUID.randomUUID(), "password");
    }

    /**
     * Tests the {@code findByUsername} method when the user exists.
     * <p>
     * It verifies that the user can be found by their username.
     * </p>
     */
    @Test
    public void testFindByUsername_whenUserExists_returnsUser() {
        var user = new User("user1", "password");
        var savedUser = repository.save(user);

        var foundUser = repository.findByUsername("user1");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());
        assertThat(foundUser.get().getUsername()).isEqualTo("user1");
    }

    /**
     * Tests the {@code findByUsername} method when the user does not exist.
     * <p>
     * It verifies that the method returns an empty {@link Optional} when the user is not found.
     * </p>
     */
    @Test
    public void testFindByUsername_whenUserDoesNotExist_returnsEmptyOptional() {
        var foundUser = repository.findByUsername("nonexistent");
        assertThat(foundUser).isNotPresent();
    }

    /**
     * Tests the {@code findUserById} method when the user exists.
     * <p>
     * It verifies that the user can be found by their ID.
     * </p>
     */
    @Test
    public void testFindUserById_whenUserExists_returnsUser() {
        var user = new User("user2", "password");
        var savedUser = repository.save(user);

        var foundUser = repository.findUserById(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());
    }

    /**
     * Tests the {@code findUserById} method when the user does not exist.
     * <p>
     * It verifies that the method returns an empty {@link Optional} when the user is not found.
     * </p>
     */
    @Test
    public void testFindUserById_whenUserDoesNotExist_returnsEmptyOptional() {
        var foundUser = repository.findUserById(999L);
        assertThat(foundUser).isNotPresent();
    }

    /**
     * Tests the {@code findUserByIdForUpdate} method when the user exists.
     * <p>
     * It verifies that the user can be found for update.
     * </p>
     */
    @Test
    public void testFindUserByIdForUpdate_whenUserExists_returnsUserForUpdate() {
        var user = new User("user3", "password");
        var savedUser = repository.save(user);

        var foundUser = repository.findUserByIdForUpdate(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());
    }

    /**
     * Updates the {@code instanceToTest} with new values for update testing.
     *
     * @return the updated {@link User} instance.
     */
    @Override
    protected User updateValue() {
        instanceToTest.setUsername("updatedUser");
        return instanceToTest;
    }

    /**
     * Validates that the updated {@link User} instance has the expected new username.
     *
     * @param updatedInstance the updated {@link User} instance.
     */
    @Override
    protected void updateMethodValidation(User updatedInstance) {
        assertThat(updatedInstance).isNotNull();
        assertThat(updatedInstance.getUsername()).isEqualTo("updatedUser");
    }

    /**
     * Provides a list of {@link User} entities for testing the {@code findAll} operation.
     *
     * @return a list containing multiple {@link User} instances.
     */
    @Override
    protected List<User> findAllEntities() {
        var user1 = new User("user4", "password");
        var user2 = new User("user5", "password");
        var user3 = new User("user6", "password");

        return List.of(user1, user2, user3);
    }
}

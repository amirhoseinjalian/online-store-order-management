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

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoTest extends BaseDomainRepositoryTest<User, Long, UserDao> {

    public UserDaoTest(@Autowired UserDao repository) {
        super(repository);
    }

    @Override
    @BeforeEach
    public void initializeValue() {
        instanceToTest = new User("test_user" + UUID.randomUUID(), "password");
    }

    @Test
    public void testFindByUsername_whenUserExists_returnsUser() {
        var user = new User("user1", "password");
        var savedUser = repository.save(user);

        var foundUser = repository.findByUsername("user1");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());
        assertThat(foundUser.get().getUsername()).isEqualTo("user1");
    }

    @Test
    public void testFindByUsername_whenUserDoesNotExist_returnsEmptyOptional() {
        var foundUser = repository.findByUsername("nonexistent");
        assertThat(foundUser).isNotPresent();
    }

    @Test
    public void testFindUserById_whenUserExists_returnsUser() {
        var user = new User("user2", "password");
        var savedUser = repository.save(user);

        var foundUser = repository.findUserById(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());
    }

    @Test
    public void testFindUserById_whenUserDoesNotExist_returnsEmptyOptional() {
        var foundUser = repository.findUserById(999L);
        assertThat(foundUser).isNotPresent();
    }

    @Test
    public void testFindUserByIdForUpdate_whenUserExists_returnsUserForUpdate() {
        var user = new User("user3", "password");
        var savedUser = repository.save(user);

        var foundUser = repository.findUserByIdForUpdate(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());
    }

    @Override
    protected User updateValue() {
        instanceToTest.setUsername("updatedUser");
        return instanceToTest;
    }

    @Override
    protected void updateMethodValidation(User updatedInstance) {
        assertThat(updatedInstance).isNotNull();
        assertThat(updatedInstance.getUsername()).isEqualTo("updatedUser");
    }

    @Override
    protected List<User> findAllEntities() {
        var user1 = new User("user4", "password");
        var user2 = new User("user5", "password");
        var user3 = new User("user6", "password");

        return List.of(user1, user2, user3);
    }
}

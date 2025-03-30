package com.jalian.online_store_order_management.dao;

import com.jalian.online_store_order_management.domain.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select u from User u where u.id = :id")
    Optional<User> findByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select u from User u where u.id = :id")
    Optional<User> findUserById(Long id);
}

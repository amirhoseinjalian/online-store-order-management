package com.jalian.online_store_order_management.dao;

import com.jalian.online_store_order_management.domain.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);
    boolean existsById(Long id);
    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
    @Query("select u from User u where u.id = :id")
    User findByUsername(String username);
    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
    @Query("select u from User u where u.id = :id")
    User findUserById(Long id);
}

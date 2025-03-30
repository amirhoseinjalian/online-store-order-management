package com.jalian.online_store_order_management.dao;

import com.jalian.online_store_order_management.domain.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select u from User u where u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select u from User u where u.id = :id")
    Optional<User> findUserById(@Param("id") Long id);

    @Query("select u from User u where u.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<User> findUserByIdForUpdate(@Param("id") Long idt);
}

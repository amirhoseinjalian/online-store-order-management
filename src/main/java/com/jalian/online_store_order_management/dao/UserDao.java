package com.jalian.online_store_order_management.dao;

import com.jalian.online_store_order_management.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);
}

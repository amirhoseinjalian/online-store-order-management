package com.jalian.online_store_order_management.dao;

import com.jalian.online_store_order_management.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreDao extends JpaRepository<Store, Long> {

    boolean existsById(Long id);
    boolean existsByName(String storeName);
}

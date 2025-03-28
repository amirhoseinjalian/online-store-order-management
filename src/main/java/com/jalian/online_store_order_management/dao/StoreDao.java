package com.jalian.online_store_order_management.dao;

import com.jalian.online_store_order_management.domain.Store;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreDao extends JpaRepository<Store, Long> {

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("select s from Store s where s.id = :id")
    Store findByIdSafe(@Param("id") Long id);
    boolean existsById(Long id);
    boolean existsByName(String storeName);
}

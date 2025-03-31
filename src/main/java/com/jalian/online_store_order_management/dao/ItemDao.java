package com.jalian.online_store_order_management.dao;

import com.jalian.online_store_order_management.domain.Item;
import com.jalian.online_store_order_management.domain.key.ItemKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemDao extends JpaRepository<Item, ItemKey> {

    @Query("select i from Item i where i.order.id = :id")
    List<Item> findAllByOrder(@Param("id") Long id);
}

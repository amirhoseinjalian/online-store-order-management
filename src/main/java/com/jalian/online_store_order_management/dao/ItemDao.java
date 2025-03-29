package com.jalian.online_store_order_management.dao;

import com.jalian.online_store_order_management.domain.Item;
import com.jalian.online_store_order_management.domain.key.ItemKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemDao extends JpaRepository<Item, ItemKey> {
}

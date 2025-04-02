package com.jalian.online_store_order_management.dto;

import com.jalian.online_store_order_management.domain.Item;

import java.util.LinkedList;
import java.util.List;

/**
 * The ItemFetchDto record is a data transfer object that encapsulates the details
 * of an item fetched from an order.
 * <p>
 * It provides information about the associated product such as its ID, name, description,
 * along with the count and price of the item in the order.
 * </p>
 *
 * @param productId          the unique identifier of the product.
 * @param productName        the name of the product.
 * @param productDescription the description of the product.
 * @param count              the quantity of the product ordered.
 * @param price              the price of the product at the time of ordering.
 *
 * @author amirhosein jalian
 */
public record ItemFetchDto(Long productId, String productName, String productDescription, long count, double price) {

    /**
     * Creates an instance of {@code ItemFetchDto} from an {@link Item} entity.
     *
     * @param item the item entity from which to extract the data.
     * @return a new instance of {@code ItemFetchDto} containing the mapped values.
     */
    public static ItemFetchDto of(Item item) {
        var product = item.getProduct();
        return new ItemFetchDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                item.getCount(),
                item.getPrice()
        );
    }

    /**
     * Converts a list of {@link Item} entities to a list of {@code ItemFetchDto} records.
     *
     * @param items the list of item entities.
     * @return a list of {@code ItemFetchDto} instances.
     */
    public static List<ItemFetchDto> of(List<Item> items) {
        var result = new LinkedList<ItemFetchDto>();
        items.forEach(item -> result.add(of(item)));
        return result;
    }
}

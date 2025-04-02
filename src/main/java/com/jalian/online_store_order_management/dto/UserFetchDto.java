package com.jalian.online_store_order_management.dto;

import com.jalian.online_store_order_management.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * The UserFetchDto record is a data transfer object that encapsulates detailed information about a user.
 * <p>
 * It includes the user's first name, last name, email, unique identifier, username, and account balance.
 * This DTO is typically used to transfer user data from the backend to clients in a structured format.
 * </p>
 *
 * @param firstName the first name of the user.
 * @param lastName  the last name of the user.
 * @param email     the email address of the user.
 * @param id        the unique identifier of the user as a string.
 * @param username  the username of the user.
 * @param balance   the current balance of the user.
 *
 * @author amirhosein jalian
 */
public record UserFetchDto(String firstName, String lastName, String email, String id, String username, double balance) {

    /**
     * Creates an instance of {@code UserFetchDto} from a {@link User} entity.
     *
     * @param user the user entity from which to extract the details.
     * @return a new {@code UserFetchDto} populated with data from the user entity.
     */
    public static UserFetchDto of(User user) {
        return new UserFetchDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getId().toString(),
                user.getUsername(),
                user.getBalance()
        );
    }

    /**
     * Converts a list of {@link User} objects into a list of {@link UserFetchDto} objects.
     * <p>
     * This method iterates over the provided list of users, converting each one to its corresponding DTO
     * representation using the {@code of(User)} method, and collects them into a new list.
     * </p>
     *
     * @param users the list of {@link User} objects to be converted.
     * @return a list of {@link UserFetchDto} objects corresponding to the provided users.
     */
    public static List<UserFetchDto> of(List<User> users) {
        var userFetchDtos = new ArrayList<UserFetchDto>();
        for (var user : users) {
            userFetchDtos.add(of(user));
        }
        return userFetchDtos;
    }
}

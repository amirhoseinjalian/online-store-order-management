package com.jalian.online_store_order_management.dto;

/**
 * The UserRegisterDto record is a data transfer object that encapsulates
 * the information required to register a new user in the system.
 * <p>
 * It includes the user's first name, last name, email, password, and username.
 * </p>
 *
 * @param firstName the first name of the user.
 * @param lastName  the last name of the user.
 * @param email     the email address of the user.
 * @param password  the password for the user account.
 * @param username  the unique username for the user.
 *
 * @author amirhosein jalian
 */
public record UserRegisterDto(String firstName, String lastName, String email, String password, String username) {
}

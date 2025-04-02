package com.jalian.online_store_order_management.domain;

import com.jalian.online_store_order_management.dto.UserRegisterDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

import java.util.Objects;

/**
 * The User class represents a user within the online store order management system.
 * <p>
 * It extends {@link BaseDomain} to inherit common identifier and auditing properties.
 * A user has credentials, personal information, a balance, and an email address.
 * </p>
 *
 * @author amirhosein jalian
 */
@Entity
@Table(name = "users")
public class User extends BaseDomain {

    /**
     * The unique username of the user.
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * The password for the user.
     */
    @Column(nullable = false)
    private String password;

    /**
     * The first name of the user.
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * The last name of the user.
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * The balance associated with the user's account.
     */
    private double balance;

    /**
     * The email address of the user.
     * <p>
     * The email is validated using the {@code @Email} annotation.
     * </p>
     */
    @Email
    private String email;

    /**
     * Default constructor for JPA.
     */
    public User() {
        super();
    }

    /**
     * Constructs a User with the specified username and password.
     *
     * @param username the unique username of the user.
     * @param password the password of the user.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Constructs a User with detailed information.
     *
     * @param username  the unique username of the user.
     * @param password  the password of the user.
     * @param firstName the first name of the user.
     * @param email     the email address of the user.
     * @param lastName  the last name of the user.
     */
    public User(String username, String password, String firstName, String email, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.email = email;
        this.lastName = lastName;
        balance = 0.0;
    }

    /**
     * Creates a new User instance from the provided {@link UserRegisterDto}.
     * <p>
     * This method maps the data transfer object to a User entity.
     * </p>
     *
     * @param userRegisterDto the DTO containing user registration details.
     * @return a new User instance with initial balance set to zero.
     */
    public static User of(UserRegisterDto userRegisterDto) {
        var user = new User();
        user.setEmail(userRegisterDto.email());
        user.setFirstName(userRegisterDto.firstName());
        user.setLastName(userRegisterDto.lastName());
        user.setUsername(userRegisterDto.username());
        user.setPassword(userRegisterDto.password());
        user.setBalance(0.0);
        return user;
    }

    /**
     * Determines whether another object is equal to this User.
     * <p>
     * Two users are considered equal if they have the same base properties (from {@link BaseDomain}),
     * and their usernames and email addresses are equal.
     * </p>
     *
     * @param o the object to compare with.
     * @return {@code true} if the users are equal; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(username, user.username) && Objects.equals(email, user.email);
    }

    /**
     * Returns the hash code value for this User.
     *
     * @return the hash code based on the base properties, username, and email.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, email);
    }

    /**
     * Returns a string representation of the User.
     *
     * @return a string that includes the user's username, first name, last name, balance, and email.
     */
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        appendFields(sb);
        sb.append(", username='").append(username).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", balance=").append(balance);
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * Gets the username of the user.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the first name of the user.
     *
     * @return the first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName the first name to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the user.
     *
     * @return the last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName the last name to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the email address of the user.
     *
     * @return the email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the balance of the user's account.
     *
     * @return the account balance.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Sets the balance of the user's account.
     *
     * @param balance the balance to set.
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }
}

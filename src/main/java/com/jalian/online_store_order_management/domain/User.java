package com.jalian.online_store_order_management.domain;

import com.jalian.online_store_order_management.dto.UserRegisterDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

import java.util.Objects;

@Entity
@Table(name = "users")
public class User extends BaseDomain {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Email
    private String email;

    public User() {
        super();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String firstName, String email, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.email = email;
        this.lastName = lastName;
    }

    public static User of(UserRegisterDto userRegisterDto) {
        var user = new User();
        user.setEmail(userRegisterDto.email());
        user.setFirstName(userRegisterDto.firstName());
        user.setLastName(userRegisterDto.lastName());
        user.setUsername(userRegisterDto.username());
        user.setPassword(userRegisterDto.password());
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(username, user.username) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, email);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        appendFields(sb);
        sb.append(", username='").append(username).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

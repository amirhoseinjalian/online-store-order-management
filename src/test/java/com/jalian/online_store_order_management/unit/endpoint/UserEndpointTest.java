package com.jalian.online_store_order_management.unit.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jalian.online_store_order_management.dto.UserFetchDto;
import com.jalian.online_store_order_management.dto.UserRegisterDto;
import com.jalian.online_store_order_management.endpoint.UserEndpoint;
import com.jalian.online_store_order_management.exception.DuplicateUsername;
import com.jalian.online_store_order_management.exception.EntityNotFoundException;
import com.jalian.online_store_order_management.exception.handler.GlobalExceptionHandler;
import com.jalian.online_store_order_management.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the {@link UserEndpoint} class.
 * <p>
 * This test class verifies the user-related endpoints including registration,
 * fetching user details by username and by id, as well as proper error handling.
 * </p>
 *
 * @author amirhosein jalian
 */
@ExtendWith(MockitoExtension.class)
public class UserEndpointTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private UserEndpoint userEndpoint;

    @Mock
    private UserService userService;

    /**
     * Sets up the MockMvc instance with the UserEndpoint and global exception handler.
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userEndpoint)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    /**
     * Tests successful user registration.
     */
    @Test
    void register_success() throws Exception {
        UserRegisterDto dto = new UserRegisterDto("John", "Doe", "john@example.com", "password", "john");
        when(userService.registerUser(any(UserRegisterDto.class))).thenReturn(1L);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result", is(1)))
                .andExpect(jsonPath("$.message", is("User registered successfully")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    /**
     * Tests user registration when a duplicate username is provided.
     */
    @Test
    void register_duplicateUsername_exception() throws Exception {
        UserRegisterDto dto = new UserRegisterDto("John", "Doe", "john@example.com", "password", "john");
        when(userService.registerUser(any(UserRegisterDto.class)))
                .thenThrow(new DuplicateUsername("john"));

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("john")));
    }

    /**
     * Tests successful fetching of a user by username.
     */
    @Test
    void findUserByUsername_success() throws Exception {
        UserFetchDto fetchDto = new UserFetchDto("John", "Doe", "john@example.com", "1", "john", 100.0);
        when(userService.findUser("john")).thenReturn(fetchDto);

        mockMvc.perform(get("/users/username/john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.username", is("john")))
                .andExpect(jsonPath("$.result.firstName", is("John")))
                .andExpect(jsonPath("$.result.lastName", is("Doe")))
                .andExpect(jsonPath("$.result.email", is("john@example.com")))
                .andExpect(jsonPath("$.result.id", is("1")))
                .andExpect(jsonPath("$.result.balance", is(100.0)))
                .andExpect(jsonPath("$.message", is("User fetched successfully")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    /**
     * Tests fetching a user by username when the user does not exist.
     */
    @Test
    void findUserByUsername_notFound_exception() throws Exception {
        when(userService.findUser("john"))
                .thenThrow(new EntityNotFoundException("User", "username", "john"));

        mockMvc.perform(get("/users/username/john"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("User")));
    }

    /**
     * Tests successful fetching of a user by id.
     */
    @Test
    void findUserById_success() throws Exception {
        UserFetchDto fetchDto = new UserFetchDto("John", "Doe", "john@example.com", "1", "john", 100.0);
        when(userService.findUserById(1L)).thenReturn(fetchDto);

        mockMvc.perform(get("/users/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id", is("1")))
                .andExpect(jsonPath("$.result.username", is("john")))
                .andExpect(jsonPath("$.message", is("User fetched successfully")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    /**
     * Tests fetching a user by id when the user is not found.
     */
    @Test
    void findUserById_notFound_exception() throws Exception {
        when(userService.findUserById(1L))
                .thenThrow(new EntityNotFoundException("User", "id", "1"));

        mockMvc.perform(get("/users/id/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("User")));
    }
}

package orlov.oleksandr.programming.userrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import orlov.oleksandr.programming.userrest.model.User;
import orlov.oleksandr.programming.userrest.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    private static final String EMAIL = "email@email.com";
    private static final String FIRST_NAME = "FIRSTNAME";
    private static final String LAST_NAME = "LASTNAME";
    private static final LocalDate VALID_DATE = LocalDate.of(2000, 1, 1);
    private String classPath = "/api/users";

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_InvalidUser_ShouldReturnBadRequest() throws Exception {
        User user = new User();

        mockMvc.perform(post(classPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errors").isMap())
                .andExpect(jsonPath("$.errors.email").value("must not be null"));
    }

    @Test
    void create_ValidUser_ShouldReturnUser() throws Exception {
        User user = new User();
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setBirthDate(VALID_DATE);

        when(userService.create(any())).thenReturn(user);

        mockMvc.perform(post(classPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(LAST_NAME))
                .andExpect(jsonPath("$.birthDate").value(VALID_DATE.toString()));
    }

    @Test
    void update_InvalidUser_ShouldReturnBadRequest() throws Exception {
        User user = new User();
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setBirthDate(VALID_DATE);

        User wrongUser = new User();

        when(userService.create(any())).thenReturn(user);

        mockMvc.perform(post(classPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(LAST_NAME))
                .andExpect(jsonPath("$.birthDate").value(VALID_DATE.toString()));

        mockMvc.perform(put(classPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errors").isMap())
                .andExpect(jsonPath("$.errors.email").value("must not be null"));
    }

    @Test
    void update_ValidUser_ShouldReturnUser() throws Exception {
        User user = new User();
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setBirthDate(VALID_DATE.plusYears(1));

        user.setFirstName("new" + FIRST_NAME);
        user.setLastName("new" + LAST_NAME);
        user.setBirthDate(VALID_DATE.plusYears(1));

        when(userService.create(any())).thenReturn(user);
        when(userService.update(any())).thenReturn(user);

        mockMvc.perform(post(classPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());

        mockMvc.perform(put(classPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.birthDate").value(user.getBirthDate().toString()));
    }

    @Test
    void partialUpdate_ValidUser_ShouldReturnUser() throws Exception {
        User user = new User();
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setBirthDate(VALID_DATE.plusYears(1));

        user.setFirstName("new" + FIRST_NAME);
        user.setBirthDate(VALID_DATE.plusYears(1));

        when(userService.create(any())).thenReturn(user);
        when(userService.partialUpdate(any())).thenReturn(user);

        mockMvc.perform(post(classPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());

        mockMvc.perform(patch(classPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.birthDate").value(user.getBirthDate().toString()));
    }


    @Test
    void delete_InvalidUser_ShouldReturnBadRequest() throws Exception {
        User user = new User();

        mockMvc.perform(delete(classPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errors").isMap())
                .andExpect(jsonPath("$.errors.email").value("must not be null"));
        ;
        ;
    }

    @Test
    void delete_ValidUser_ShouldString() throws Exception {
        User user = new User();
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setBirthDate(VALID_DATE.plusYears(1));

        user.setFirstName("new" + FIRST_NAME);
        user.setLastName("new" + LAST_NAME);
        user.setBirthDate(VALID_DATE.plusYears(1));

        when(userService.create(any())).thenReturn(user);

        mockMvc.perform(post(classPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());

        mockMvc.perform(delete(classPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNoContent());
    }

    @Test
    void getUsersInDateRange_ShouldReturnListOfUsers() throws Exception {
        User user1 = User.builder()
                .email("john@example.com")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(2000, 5, 10))
                .build();
        User user2 = User.builder()
                .email("jane@example.com")
                .firstName("Jane")
                .lastName("Smith")
                .birthDate(LocalDate.of(2000, 7, 15))
                .build();

        LocalDate START_DATE = LocalDate.of(2000, 1, 1);
        LocalDate END_DATE = LocalDate.of(2000, 12, 31);

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        when(userService.getUsersWithBirthDateInBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(userList);

        mockMvc.perform(get(classPath)
                        .param("startDate", START_DATE.toString())
                        .param("endDate", END_DATE.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"))
                .andExpect(jsonPath("$.length()").value(2));
    }
}
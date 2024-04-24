package orlov.oleksandr.programming.userrest.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;
import orlov.oleksandr.programming.userrest.model.User;

import java.lang.reflect.Field;
import java.time.LocalDate;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    private static final String EMAIL = "EMAIL";
    private static final String FIRST_NAME = "FIRSTNAME";
    private static final String LAST_NAME = "LASTNAME";
    private static final LocalDate VALID_DATE = LocalDate.of(2000, 1, 1);

    private UserServiceImpl userService;

    @Autowired
    private int minimalAge;

    @BeforeEach
    void beforeEach() {
        userService = new UserServiceImpl(minimalAge);
    }

    @Test
    void create_UserNull(){
        String errorMessage = "User must not be null";

        var error = assertThrows(NullPointerException.class, () -> userService.create(null));

        assertEquals(errorMessage, error.getMessage());
    }

    @Test
    void create_NullBirthDate(){
        User user = new User();
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);

        String errorMessage = "BirthDate must not be null";

        var error = assertThrows(NullPointerException.class, () -> userService.create(user));

        assertEquals(errorMessage, error.getMessage());
    }

    @Test
    void create_BirthDateLessThenMinimalAge(){
        User user = new User();
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setBirthDate(LocalDate.now());

        String errorMessage = "User's age is less than " + minimalAge + ": " + user.getBirthDate();

        var error = assertThrows(IllegalArgumentException.class, () -> userService.create(user));

        assertEquals(errorMessage, error.getMessage());
    }

    @Test
    void create_ValidUser(){
        User user = new User();
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setBirthDate(VALID_DATE);

        User savedUser = userService.create(user);

        assertEquals(user, savedUser);
    }

    @Test
    void create_UserWithSameEmail(){
        User user = new User();
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setBirthDate(VALID_DATE);

        String expected = "User already exists with email: " + EMAIL;

        User savedUser = userService.create(user);

        var e = assertThrows(IllegalArgumentException.class, () -> userService.create(savedUser));

        assertEquals(expected, e.getMessage());
    }

    @Test
    void update_NullUser(){
        User user = null;

        String expected = "User must not be null";

        var e = assertThrows(NullPointerException.class, () -> userService.update(user));

        assertEquals(expected, e.getMessage());
    }

    @Test
    void update_InvalidUserBirthDate(){
        User user = new User();
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        LocalDate wrongDate = LocalDate.now();
        user.setBirthDate(wrongDate);

        String expected = "User's age is less than " + minimalAge + ": " + wrongDate;

        var e = assertThrows(IllegalArgumentException.class, () -> userService.update(user));

        assertEquals(expected, e.getMessage());
    }

    @Test
    void update_UserDoesntExistByEmail(){
        User user = new User();
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setBirthDate(VALID_DATE);

        String expected = "User not found";

        var e = assertThrows(IllegalArgumentException.class, () -> userService.update(user));

        assertEquals(expected, e.getMessage());
    }

    @Test
    void update_ValidUser(){
        User user1 = new User();
        user1.setEmail(EMAIL);
        user1.setFirstName(FIRST_NAME);
        user1.setLastName(LAST_NAME);
        user1.setBirthDate(VALID_DATE);

        User user2 = new User();
        user2.setEmail(EMAIL);
        user2.setFirstName(FIRST_NAME + "UPDATED");
        user2.setLastName(LAST_NAME + "UPDATED");
        user2.setBirthDate(VALID_DATE.minusDays(1));

        userService.create(user1);
        userService.update(user2);

        assertEquals(user2, userService.getUserByEmail(EMAIL));
    }

    @Test
    void partialUpdate_PartialUpdateUserNotFound(){
        User user = new User();
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setBirthDate(VALID_DATE);

        String expected = "User not found";

        var e = assertThrows(IllegalArgumentException.class, () -> userService.partialUpdate(user));

        assertEquals(expected, e.getMessage());
    }

    @Test
    void partialUpdate_BirthDateNull() throws IllegalAccessException {
        User user = new User();
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setBirthDate(VALID_DATE);

        User updateUser = new User();
        updateUser.setEmail(EMAIL);
        updateUser.setFirstName(FIRST_NAME + "UPDATED");
        updateUser.setBirthDate(null);

        userService.create(user);
        userService.partialUpdate(updateUser);

        assertEquals(updateUser.getFirstName(), userService.getUserByEmail(EMAIL).getFirstName());
        assertEquals(VALID_DATE, userService.getUserByEmail(EMAIL).getBirthDate());
    }

    @Test
    void partialUpdate_BirthDateLessThenMinimalAge(){
        User user = new User();
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setBirthDate(VALID_DATE);

        LocalDate wrongDate = LocalDate.now();
        User updateUser = new User();
        updateUser.setEmail(EMAIL);
        updateUser.setBirthDate(wrongDate);

        String expected = "User's age is less than " + minimalAge + ": " + wrongDate;

        userService.create(user);

        var e = assertThrows(IllegalArgumentException.class, () -> userService.partialUpdate(updateUser));

        assertEquals(expected, e.getMessage());
    }

    @Test
    void partialUpdate_Valid() throws IllegalAccessException {
        User user = new User();
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setBirthDate(VALID_DATE);

        LocalDate updatedDate = VALID_DATE.plusDays(1);
        User updateUser = new User();
        updateUser.setEmail(EMAIL);
        updateUser.setBirthDate(updatedDate);

        userService.create(user);
        userService.partialUpdate(updateUser);

        assertEquals(updatedDate, userService.getUserByEmail(EMAIL).getBirthDate());
    }

    @Test
    void delete_Valid(){
        User user = new User();
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setBirthDate(VALID_DATE);

        String expected = "User not found";

        userService.create(user);
        userService.delete(user);

        var e = assertThrows(IllegalArgumentException.class, () -> userService.getUserByEmail(EMAIL));

        assertEquals(expected, e.getMessage());
    }

    @Test
    void getUsersWithBirthDateInBetween_ThrowsExceptioForInvalidDate(){
        LocalDate to = VALID_DATE.plusDays(10);

        String expected = "From = " + to + " must be before To = " + VALID_DATE;

        var e = assertThrows(IllegalArgumentException.class,
                () -> userService.getUsersWithBirthDateInBetween(to, VALID_DATE));

        assertEquals(expected, e.getMessage());
    }

    @Test
    void getUsersWithBirthDateInBetween_findTen(){
        LocalDate to = VALID_DATE.plusDays(10);

        for(int i = 0; i < 10; i++){
            User user = User.builder()
                    .email(EMAIL + i)
                    .firstName(String.valueOf(i))
                    .lastName(String.valueOf(i))
                    .birthDate(VALID_DATE.plusDays(i))
                    .build();

            userService.create(user);
        }

        assertEquals(10, userService.getUsersWithBirthDateInBetween(VALID_DATE, to).size());
        assertEquals(5, userService.getUsersWithBirthDateInBetween(VALID_DATE.plusDays(5), to).size());
        assertEquals(5, userService.getUsersWithBirthDateInBetween(VALID_DATE, to.minusDays(6)).size());
        assertEquals(0, userService.getUsersWithBirthDateInBetween(to.plusDays(100),
                to.plusDays(100)).size());
    }
}
package orlov.oleksandr.programming.userrest.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static User validUser;

    @BeforeAll
    static void init() {
        validUser = User.builder()
                .email("valid@cv.edu.ua")
                .firstName("Valid-Name")
                .lastName("Valid-Name")
                .birthDate(LocalDate.MIN)
                .address("Valid-Address")
                .phoneNumber("Valid-Phone")
                .build();
    }

    @Test
    void createValidUserTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);

        assertEquals(0, violations.size());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidEmailUser")
    void createUser_invalidEmailTests(String input, String errorValue) {
        User user = User.builder()
                .email(input)
                .firstName(validUser.getFirstName())
                .lastName(validUser.getLastName())
                .birthDate(validUser.getBirthDate())
                .build();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals(errorValue, violations.iterator().next().getInvalidValue());
    }

    private static Stream<Arguments> provideInvalidEmailUser() {
        return Stream.of(
                Arguments.of("invalidEmail", "invalidEmail"),
                Arguments.of("email@", "email@"),
                Arguments.of("", ""),
                Arguments.of("invalid", "invalid"),
                Arguments.of("@com", "@com"),
                Arguments.of("123@com", "123@com"),
                Arguments.of(null, null)
        );
    }

    @Test
    void createUser_NullFirstName() {
        String errorValue = null;

        User user = User.builder()
                .email(validUser.getEmail())
                .firstName(null)
                .lastName(validUser.getLastName())
                .birthDate(validUser.getBirthDate())
                .build();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(errorValue, violations.iterator().next().getInvalidValue());
    }

    @Test
    void createUser_NullLastName() {
        String errorValue = null;

        User user = User.builder()
                .email(validUser.getEmail())
                .firstName(validUser.getFirstName())
                .lastName(null)
                .birthDate(validUser.getBirthDate())
                .build();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(errorValue, violations.iterator().next().getInvalidValue());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidBirthDateUser")
    void createUser_invalidBirthDateTests(LocalDate input, LocalDate errorValue) {
        User user = User.builder()
                .email(validUser.getEmail())
                .firstName(validUser.getFirstName())
                .lastName(validUser.getLastName())
                .birthDate(input)
                .build();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals(errorValue, violations.iterator().next().getInvalidValue());
    }

    private static Stream<Arguments> provideInvalidBirthDateUser() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(LocalDate.now().plusDays(1), LocalDate.now().plusDays(1)),
                Arguments.of(LocalDate.MAX, LocalDate.MAX)
        );
    }

    @Test
    void equalsAndHashCode() {
        User tempUser = new User();
        tempUser.setEmail(validUser.getEmail());
        tempUser.setFirstName(validUser.getFirstName());
        tempUser.setLastName(validUser.getLastName());
        tempUser.setBirthDate(validUser.getBirthDate());
        tempUser.setAddress(validUser.getAddress());
        tempUser.setPhoneNumber(validUser.getPhoneNumber());

        assertEquals(tempUser, validUser);
        assertEquals(tempUser.hashCode(), validUser.hashCode());
    }

    @Test
    void equals_sameObject() {
        assertEquals(validUser, validUser);
    }

    @Test
    void equals_nullObject() {
        assertNotEquals(validUser, null);
    }

    @Test
    void equals_differentClass() {
        assertNotEquals(validUser, "someString");
    }

    @Test
    void equals_withDifferent_email() {
        User tempUser = User.builder()
                .build();

        assertNotEquals(tempUser, validUser);
    }

    @Test
    void equals_withDifferent_FirstName() {
        User tempUser = User.builder()
                .email(validUser.getEmail())
                .build();

        assertNotEquals(tempUser, validUser);
    }

    @Test
    void equals_withDifferent_LastName() {
        User tempUser = User.builder()
                .email(validUser.getEmail())
                .firstName(validUser.getFirstName())
                .build();

        assertNotEquals(tempUser, validUser);
    }

    @Test
    void equals_withDifferent_BirthDate() {
        User tempUser = User.builder()
                .email(validUser.getEmail())
                .firstName(validUser.getFirstName())
                .lastName(validUser.getLastName())
                .build();

        assertNotEquals(tempUser, validUser);
    }

    @Test
    void equals_withDifferent_Address() {
        User tempUser = User.builder()
                .email(validUser.getEmail())
                .firstName(validUser.getFirstName())
                .lastName(validUser.getLastName())
                .birthDate(validUser.getBirthDate())
                .build();

        assertNotEquals(tempUser, validUser);
    }

    @Test
    void equals_withDifferent_PhoneNumber() {
        User tempUser = User.builder()
                .email(validUser.getEmail())
                .firstName(validUser.getFirstName())
                .lastName(validUser.getLastName())
                .birthDate(validUser.getBirthDate())
                .address(validUser.getAddress())
                .build();

        assertNotEquals(tempUser, validUser);
    }

    @Test
    void toStringTest() {
        String expected = "User{email='valid@cv.edu.ua', firstName='Valid-Name', lastName='Valid-Name'," +
                " birthDate=-999999999-01-01, address='Valid-Address', phoneNumber='Valid-Phone'}";

        assertEquals(expected, validUser.toString());
    }

    @Test
    void userBuilder_toStringTest() {
        User.UserBuilder userBuilder = User.builder()
                .email(validUser.getEmail())
                .firstName(validUser.getFirstName())
                .lastName(validUser.getLastName())
                .birthDate(validUser.getBirthDate())
                .address(validUser.getAddress())
                .phoneNumber(validUser.getPhoneNumber());

        String expected = "User.UserBuilder(email=valid@cv.edu.ua, firstName=Valid-Name, lastName=Valid-Name," +
                " birthDate=-999999999-01-01, address=Valid-Address, phoneNumber=Valid-Phone)";

        assertEquals(expected, userBuilder.toString());
    }
}
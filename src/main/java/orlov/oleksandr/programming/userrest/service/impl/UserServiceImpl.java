package orlov.oleksandr.programming.userrest.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import orlov.oleksandr.programming.userrest.model.User;
import orlov.oleksandr.programming.userrest.service.UserService;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private List<User> users;

    @Value("${minimal.age}")
    private static int minimalAge;

    public UserServiceImpl() {
        users = new ArrayList<>();
    }

    @Override
    public User create(User user) {
        validateUserHasAllRequiredFields(user);

        validateUserDoesNotExistByEmail(user.getEmail());

        validateAgeLessThenMinimalAge(user.getBirthDate());

        users.add(user);
        return user;
    }

    @Override
    public User update(User user) {
        validateUserHasAllRequiredFields(user);
        validateAgeLessThenMinimalAge(user.getBirthDate());
        User foundUser = getUserByEmail(user.getEmail());
        users.set(users.indexOf(foundUser), user);
        return user;
    }

    @Override
    public User partialUpdate(User user) throws IllegalAccessException {
        User foundUser = getUserByEmail(user.getEmail());

        Class<?> userClass = User.class;
        Field[] userFields = userClass.getDeclaredFields();
        for (Field userField : userFields) {
            if(userField.getName().equals("birthDate")){
                validateAgeLessThenMinimalAge(user.getBirthDate());
            }
            userField.setAccessible(true);
            Object value = userField.get(user);
            if (value != null) {
                userField.set(foundUser, value);
            }
            userField.setAccessible(false);
        }
        return foundUser;
    }

    @Override
    public void delete(User user) {
        users.remove(user);
    }

    @Override
    public List<User> getUsersWithBirthDateInBetween(LocalDate from, LocalDate to) {
        if(!from.isBefore(to) && !from.isEqual(to)) {
            throw new IllegalArgumentException("From = " + from + " must be before To = " + to);
        }

        return users.stream()
                .filter(user -> !user.getBirthDate().isBefore(from) && !user.getBirthDate().isAfter(to))
                .toList();
    }

    @Override
    public User getUserByEmail(String email) {
        Objects.requireNonNull(email, "Email must not be null");

        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private void validateAgeLessThenMinimalAge(LocalDate date){
        if (!isDateMoreThanMinimalAge(date)) {
            throw new IllegalArgumentException("User's age is less than " + minimalAge + ": " + date);
        }
    }

    private boolean isDateMoreThanMinimalAge(LocalDate date) {
        return (LocalDate.now().getYear() - date.getYear()) >= minimalAge;
    }

    private void validateUserDoesNotExistByEmail(String email) {
        if (users.stream().anyMatch(user -> user.getEmail().equals(email))) {
            throw new IllegalArgumentException("User already exists with email: " + email);
        }
    }

    private void validateUserHasAllRequiredFields(User user) {
        Objects.requireNonNull(user, "User must not be null");
        Objects.requireNonNull(user.getEmail(), "Email must not be null");
        Objects.requireNonNull(user.getFirstName(), "FirstName must not be null");
        Objects.requireNonNull(user.getLastName(), "LastName must not be null");
        Objects.requireNonNull(user.getBirthDate(), "BirthDate must not be null");
    }
}

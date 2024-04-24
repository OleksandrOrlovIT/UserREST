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
        Objects.requireNonNull(user, "user must not be null");
        Objects.requireNonNull(user.getBirthDate(), "birthDate must not be null");

        if(getUserByEmail(user.getEmail()) != null){
            throw new IllegalArgumentException("User already exists with email = " + user.getEmail());
        }

        if(!isDateMoreThenMinimalAge(user.getBirthDate())){
            throw new IllegalArgumentException("User has an invalid birth date for " + user.getBirthDate());
        }

        users.add(user);

        return user;
    }

    @Override
    public User update(User user) {
        User foundUser = validateUserForUpdateAndFindByEmail(user);

        users.set(users.indexOf(foundUser), user);

        return user;
    }

    @Override
    public User partialUpdate(User user) throws IllegalAccessException {
        User foundUser = validateUserForUpdateAndFindByEmail(user);

        Class<?> userClass = User.class;
        Field[] userFields = userClass.getDeclaredFields();
        for (Field userField : userFields) {
            userField.setAccessible(true);

            Object value = userField.get(user);

            if(value != null){
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
        return users.stream()
                .filter(user -> !user.getBirthDate().isBefore(from) && !user.getBirthDate().isAfter(to))
                .toList();
    }

    @Override
    public User getUserByEmail(String email) {
        Objects.requireNonNull(email, "email must not be null");

        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private boolean isDateMoreThenMinimalAge(LocalDate date) {
        LocalDate dateMinusMinimalAge = LocalDate.now().minusDays(minimalAge);
        return date.isBefore(dateMinusMinimalAge) || date.isEqual(dateMinusMinimalAge);
    }

    private User validateUserForUpdateAndFindByEmail(User user){
        Objects.requireNonNull(user, "user must not be null");

        User foundUser = getUserByEmail(user.getEmail());

        if(getUserByEmail(user.getEmail()) == null){
            throw new IllegalArgumentException("User doesn't exist in database = " + user.getEmail());
        }

        return foundUser;
    }
}

package orlov.oleksandr.programming.userrest.service;

import orlov.oleksandr.programming.userrest.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface UserService {
    User create(User user);

    User update(User user);

    User partialUpdate(User user) throws IllegalAccessException;

    void delete(User user);

    List<User> getUsersWithBirthDateInBetween(LocalDate from, LocalDate to);

    User getUserByEmail(String email);
}

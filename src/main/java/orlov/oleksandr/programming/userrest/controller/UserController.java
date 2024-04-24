package orlov.oleksandr.programming.userrest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import orlov.oleksandr.programming.userrest.exception.ValidationException;
import orlov.oleksandr.programming.userrest.model.User;
import orlov.oleksandr.programming.userrest.service.UserService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody @Validated User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            getErrors(bindingResult);
        }

        return userService.create(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Validated User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            getErrors(bindingResult);
        }

        return userService.update(user);
    }

    @PatchMapping
    public User updateUser(@RequestBody User user) throws IllegalAccessException {
        return userService.partialUpdate(user);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestBody @Validated User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            getErrors(bindingResult);
        }

        userService.delete(user);

        return ResponseEntity.ok().body("User deleted successfully with email = " + user.getEmail());
    }

    @GetMapping
    public List<User> getUsersInDateRange(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {

        return userService.getUsersWithBirthDateInBetween(startDate, endDate);
    }

    private void getErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        throw new ValidationException(errors);
    }
}

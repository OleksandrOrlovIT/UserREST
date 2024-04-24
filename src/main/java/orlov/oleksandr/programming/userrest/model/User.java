package orlov.oleksandr.programming.userrest.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
public class User {

    @Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")
    @NotNull
    private String email;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @Past
    @NotNull
    private LocalDate birthDate;

    private String address;

    private String phoneNumber;

    @Builder
    public User(String email, String firstName, String lastName, LocalDate birthDate, String address, String phoneNumber) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return Objects.equals(email, user.email) && Objects.equals(firstName, user.firstName)
                && Objects.equals(lastName, user.lastName) && Objects.equals(birthDate, user.birthDate)
                && Objects.equals(address, user.address) && Objects.equals(phoneNumber, user.phoneNumber);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(firstName);
        result = 31 * result + Objects.hashCode(lastName);
        result = 31 * result + Objects.hashCode(birthDate);
        result = 31 * result + Objects.hashCode(address);
        result = 31 * result + Objects.hashCode(phoneNumber);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}

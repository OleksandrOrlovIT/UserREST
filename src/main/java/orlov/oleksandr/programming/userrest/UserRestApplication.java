package orlov.oleksandr.programming.userrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication
public class UserRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserRestApplication.class, args);
    }

}

package brain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"brain.repos"})
@ComponentScan(basePackages = {"brain.service"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
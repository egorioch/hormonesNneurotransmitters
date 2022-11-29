package brain;

import brain.service.MailSender;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"brain.repos"})
@EntityScan("brain.domain")
@ComponentScan(basePackages = {"brain.*"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
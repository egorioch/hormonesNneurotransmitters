package brain;

import brain.service.MailSender;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"brain.repos"})
@EntityScan("brain.domain")
//В ComponentScan нужно указать вообще все модули, иначе спринг их просто не увидит(для Heroku)
@ComponentScan(basePackages = {"brain.service", "brain.config", "brain.controller"})
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
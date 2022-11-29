package brain.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.protocol}")
    private String protocol;

    @Value("${mail.debug}")
    private String debug;

    @Bean
    public JavaMailSenderImpl getMailSender() {
        //jetBrain-овский класс-почта
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        System.out.println("Data:");
        System.out.println(
                "host=" + host + "\n" +
                        "username=" + username + "\n" +
                        "password=" + password + "\n" +
                        "port=" + port + "\n" +
                        "protocol=" + protocol + "\n" +
                        "debug=" + debug + "\n");

        mailSender.setHost(host);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setPort(port);

        Properties properties = mailSender.getJavaMailProperties();

        properties.setProperty("mail.transport.protocol", protocol);
        //когда установлен debug, можно посмотреть в проперти, в чём именно дело...(но в продакшене не используется)
        properties.setProperty("mail.debug", debug);

        mailSender.setJavaMailProperties(properties);

        return mailSender;
    }

}
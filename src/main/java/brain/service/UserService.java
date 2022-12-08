package brain.service;

import brain.domain.Role;
import brain.domain.User;
import brain.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MailSender mailSender;
    @Value("${hostname}")
    private String hostname;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public boolean addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }
        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(user.getPassword()); //тут должен быть .encode

        userRepo.save(user);

        sendMessage(user);

        return true;
    }

    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Sweater. Please, visit next link: http://%s/activate/%s",
                    user.getUsername(),
                    hostname,
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);
        System.out.println("findByActivationCode: " + user);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        user.setActive(true);
        System.out.println("activateUser(после)" + user);
        userRepo.save(user);

        return true;
    }

    public boolean updateProfile(User user, String email, String password) {
        System.out.println("old mail: " + user.getEmail());
        System.out.println("user(controller): " + user);
        System.out.println("new email: " + email);
        System.out.println("password: " + password);
        String userEmail = user.getEmail();

        boolean isEmailChanged = !StringUtils.isEmpty(email) && (email.equals(userEmail))
                || !StringUtils.isEmpty(userEmail) && (userEmail.equals(email));
        if (isEmailChanged) {
            user.setEmail(email);
        }

        if(!StringUtils.isEmpty(password)) {
            user.setPassword(password);
        }
        userRepo.save(user);

        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUserEdit(User user, String username,
                             Map<String, String> form) {
        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        for (var key : form.keySet()) {
            if(roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepo.save(user);
    }

    public boolean isSubscriber(User userChannel, User user) {
        return userChannel.getSubscribers().contains(user);
    }

    public void subscribe(User userChannel, User user) {

        //В контроллере не работает метод CONTAINS, когда получаем булевый тип. Нужно проверить(мб беда с хешом).

        if (userChannel.getSubscribers().contains(user)) {
            System.out.println("Текущий пользователь является подписчиком");
        } else {
            System.out.println("не подписчик");
        }
        userChannel.getSubscribers().add(user);
        /*System.out.println("текущие подписчики: ");
        userChannel.getSubscribers().forEach(us -> System.out.println(us.getUsername()));*/

        //сохраняем изменения
        userRepo.save(userChannel);
    }

    public void unsubscribe(User userChannel, User user) {
        userChannel.getSubscribers().remove(user);

        //сохраняем изменения
        userRepo.save(userChannel);
    }

    public User findByUsername(String username) {
        User user = userRepo.findByUsername(username);
        System.out.println("id найденного user'а" + user.getId());
        return user;
    }
}

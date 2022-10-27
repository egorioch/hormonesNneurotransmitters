package brain.service;

import brain.domain.Role;
import brain.domain.User;
import brain.repos.UserRepo;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.AssertTrue;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private MailSender mailSender;

    @MockBean
    private UserRepo userRepo;


    @Test
    public void addUserTest() {
        User user = new User();

        //если не устанавливать email, он будет null
        user.setEmail("123123");

        /*вообще, при добавлении(addUser) - происходит ещё куча методов.
        поэтому, нужно заменить всё это дело с помощью Mock
        C помощью мока, мы как раз все эти методы заменим*/
        boolean isUserCreated = userService.addUser(user);
        System.out.println("created user: " + user.getUsername());

        //Assert.assertTrue(isUserCreated);
        //пользователю установлен активейшн код
        Assert.assertNotNull(user.getActivationCode());
        System.out.println("user get Activation code: " + user.getActivationCode());
        //он имеет роль юзера
        Assert.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
        Mockito.verify(mailSender, Mockito.times(1))
                .send(
                        ArgumentMatchers.eq(user.getEmail()),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );
    }

    //пользователь уже есть в бд. сервисы вызывались 0 раз
    @Test
    public void addUserFailTest() {
        User user = new User();
        user.setUsername("hallow");

        doReturn(user)
                .when(userRepo)
                .findByUsername("hallow");

        //после добавления объекта в бд, проверяем: можно ли добавить его ещё раз
        boolean isConsist = userService.addUser(user);
        Assert.assertFalse(isConsist);

        Mockito.verify(userRepo, Mockito.times(0)).save(user);
        Mockito.verify(mailSender, Mockito.times(0))
                .send(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );
    }


    @Test
    public void activateUserTest() {
        //создаем пользователя и присваиваем ему activation code
        User user = new User();
        //user.setActivationCode("bebrad");

        //"возвращаем" user'а по activation cod'у
        //точнее, мы кладем юзера в бд по этому активейшн коду
        doReturn(user)
                .when(userRepo)
                .findByActivationCode("bebra");

        //а тут мы проверяем, чтобы в бд правда имелся такой активейшн, чтобы его занулить
        boolean isUserActivated = userService.activateUser("bebra");
        Assert.assertTrue(isUserActivated);
        //проверяем зануление:
        Assert.assertNull(user.getActivationCode());

    }

    @Test
    public void activateUserFailTest() {
        boolean isUserActivated = userService.activateUser("bebra");
        Assert.assertFalse(isUserActivated);

        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }


}
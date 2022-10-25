package brain;

import brain.controller.NoteController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest //комбо-аннотация, которая содержит информацию об окружении и куче всего остального
@AutoConfigureMockMvc //Spring самостоятельно будет создавать структуру классов, которая подменит слой MVC
public class LoginTest {
    @Autowired
    private MockMvc mockMvc;
    StatusAssertions statusAssertions;

    @Autowired
    private NoteController controller;

    @Test
    public void test() throws Exception {
        this.mockMvc.perform(get("/")) //возвращает get-методом ResultActions --
                .andDo(print()) // печатает полученный результат в консоль
                .andExpect(status().isOk()) //ожидаем ответ 200 от сервера
                //ищет на ВСЕЙ странице эту строку
                .andExpect(content().string(containsString("Home")));
    }

    @Test
    public void loginTest() throws Exception{
        this.mockMvc.perform(get("/home"))
                .andDo(print())
                //302 код -- перенаправление на страницу логина
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    //взаимодействие идёт с данными бд, которая установлена в пропертях
    @Test
    public void correctLogin() throws Exception {
        //метод смотрит как в контексте объявили login-page и вызывает обращение к странице
        this.mockMvc.perform(formLogin().user("beregNevi").password("123"))
                .andDo(print())
                //после "вхождения" в систему, нас, 302 запросом, перенаправит на главную страницу
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    //отбивка на неправильные данные пользователя
    @Test
    public void badCredentials() throws Exception {
        this.mockMvc.perform(post("/login").param("user", "burunduk"))
                .andDo(print())
                .andExpect(redirectedUrl("/login?error=true"));
    }

}

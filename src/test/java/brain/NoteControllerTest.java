package brain;

import brain.controller.NoteController;
import brain.controller.UserController;
import brain.domain.User;
import brain.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest //комбо-аннотация, которая содержит информацию об окружении и куче всего остального
@AutoConfigureMockMvc
//@WithUserDetails(value = "admin")
//спринг будет во время тестов подключаться к этим пропертям
@TestPropertySource("/application-test.yml")
//аннотация указывает на sql-скрипт, который нужно выполнить
//@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class NoteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteController noteController;

    @MockBean
    private UserService userService;


    @Test
    public void mainPageTest() throws Exception {
        this.mockMvc.perform(get("/home").with(user("admin").password("123").roles("USER", "ADMIN")))
                .andDo(print())
                //authenticated -- если пользователь корректно авторизирован и у него висит сессия
                //это будет работать, если указать аннотацию withUserDetails
                //.andExpect(authenticated())
                .andExpect(xpath("//*[@id='navbarTogglerDemo03']/ul/li[2]/a")
                        .string("admin"));
        //для проверки этого пользователя(после загрузки приложения) нужно тыкнуть на имя пользователя и скопировать xpath
    }

    //корректное отображение списка сообщений
    @Test
    public void noteListTest() throws Exception {
        this.mockMvc.perform(get("/").with(user("admin").password("123")))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='card-table']").nodeCount(1));

    }

    @Test
    public void addNoteToListTest() throws Exception{
        MockHttpServletRequestBuilder multipart = multipart("/notes")
                .with(user("admin").password("123"))
                .param("tag", "shlypa")
                .param("text", "kak raz");
        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(xpath("//*[@id='table-notes']/tbody/tr[1]/td[2]").exists());
    }

    @Test
    public void testPostInputFieldsNodesByUpdateUser() throws Exception{
        User user = new User();
        user.setUsername("admin");
        user.setPassword("123");
        user.setEmail("bebraSlhypa@mail.ru");


        this.mockMvc.perform(post("/user/profile").with(user(user))
                        .param("email", "suslik@bebera.ru")
                        .param("password", "1234"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id=\"my-fields-class\"]").exists());
    }

    @Test
    public void testGetInputFieldsNodesByUpdateUser() throws Exception{
        User user = new User();
        user.setUsername("admin");
        user.setPassword("123");
        user.setEmail("bebraSlhypa@mail.ru");

        this.mockMvc.perform(get("/user/profile").with(user(user)))
                .andDo(print())
                .andExpect(content().string(containsString("change")));
    }

    // нужно ещё протестить отображаемый Login, но это уже, наверное, с помощью UserDetails.
    @Test
    public void testQuantityNodesUpdateProfile() throws Exception {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("123");
        user.setEmail("bebraSlhypa@mail.ru");

        this.mockMvc.perform(get("/user/profile").with(user(user)))
                .andDo(print())
                .andExpect(xpath("//*[@id=\"my-fields-class\"]/div[1]/label").nodeCount(2));
    }
}

package brain;

import brain.controller.NoteController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest //комбо-аннотация, которая содержит информацию об окружении и куче всего остального
@AutoConfigureMockMvc
@WithUserDetails("beregNevi")
public class NoteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteController noteController;

    @Test
    public void mainPageTest() throws Exception {
        this.mockMvc.perform(get("/home"))
                .andDo(print())
                //authenticated -- если пользователь корректно авторизирован и у него висит сессия
                //это будет работать, если указать аннотацию withUserDetails
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id=\"navbarTogglerDemo03\"]/ul/li[2]/a")
                        .string("beregNevi"));
        //для проверки этого пользователя(после загрузки приложения) нужно тыкнуть на имя пользователя и скопировать xpath

    }
}

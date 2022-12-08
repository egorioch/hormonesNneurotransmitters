package brain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import brain.websocket.SubWebSocket;

//если URL работать не будет, можно вместо "*" унаследовать методы
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Bean
    public SubWebSocket subWebSocket() {
        return new SubWebSocket();
    }

    //указываем, какой бин-обработчик будет добавлен в контекст по обрабатываемому адресу
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(subWebSocket(), "/sub_action");
    }
}
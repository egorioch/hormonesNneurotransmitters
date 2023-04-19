package brain.websocket;

import brain.repos.UserRepo;
import brain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class SubWebSocket extends TextWebSocketHandler {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("connection established");
        session.sendMessage(new TextMessage("connection established"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception  {
        System.out.println("message: " + message.getPayload());
        String[] messageArray = message.getPayload().split(",");

        String btnName = messageArray[0];
        boolean isSubscriber =  Boolean.parseBoolean(messageArray[1]);
        String userChannelUsername = messageArray[2];
        String userUsername = messageArray[3];
        //System.out.println("isSubscriber(SubWebSocket): " + isSubscriber);

        //значение, которое должно принять кнопка(зависит от того, что вернется с бд -- sub/unsub)
        String nextBtnValue = null;

        //если не является подписчиком
        if (btnName.equals("Unsubscribe")) {
            userService.unsubscribe(
                    userRepo.findByUsername(userChannelUsername),
                    userRepo.findByUsername(userUsername)
            );
            System.out.println("Юзер теперь не является подписчиком");
            nextBtnValue = "Unsubscribe";

        } else if(btnName.equals("Subscribe")) {
            userService.subscribe(
                    userRepo.findByUsername(userChannelUsername),
                    userRepo.findByUsername(userUsername)
            );
            nextBtnValue = "Subscribe";
            System.out.println("Юзер пока не является подписчиком");
        }
        session.sendMessage(new TextMessage(nextBtnValue));
    }
}

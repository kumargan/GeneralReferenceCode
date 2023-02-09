package transact.controllers;

import javax.websocket.Session;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import transact.beans.request.SubscriptionReq;
import transact.beans.response.SubscriptionRes;

@Controller
public class GreetingController {

//    @MessageMapping("/chat")
//    @SendTo("/topic/greetings")
//    public SubscriptionRes greeting(SubscriptionReq message) throws Exception {
//        Thread.sleep(1000); // simulated delay
//        return new SubscriptionRes(new Integer[]);
//    }
    
    @MessageMapping("/subscribe")
    @SendToUser("/queue/reply")
    public SubscriptionRes greetingNew(SubscriptionReq message,Session session) throws Exception {  
        return new SubscriptionRes(true);
    }
    
    
}

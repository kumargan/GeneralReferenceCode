package transact.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import transact.beans.request.HelloMessage;
import transact.beans.response.Greeting;

@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
    
    @MessageMapping("/hello1")
    @SendToUser("/queue/reply")
    public Greeting greetingNew(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("my my Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
}

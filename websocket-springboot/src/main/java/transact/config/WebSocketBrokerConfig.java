package transact.config;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {
	

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue/");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket").withSockJS();
        registry
        .addEndpoint("/greeting").withSockJS();
//        .setHandshakeHandler(new DefaultHandshakeHandler() {
//       
//            public boolean beforeHandshake(
//              ServerHttpRequest request, 
//              ServerHttpResponse response, 
//              WebSocketHandler wsHandler,
//              Map attributes) throws Exception {
//        
//                  if (request instanceof ServletServerHttpRequest) {
//                      ServletServerHttpRequest servletRequest
//                       = (ServletServerHttpRequest) request;
//                      HttpSession session = servletRequest
//                        .getServletRequest().getSession();
//                      attributes.put("sessionId", session.getId());
//                  }
//                      return true;
//              }
//            
//            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
//        			Exception ex) {
//            	if(ex!=null)
//            		System.out.println(" connection threw error in after handshake method "+ex.getMessage());
//        	}
//            
//        }).withSockJS();
          }

}
package transact.config;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import transact.components.CountActiveConnectionsComponent;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic", "/queue/");
    config.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    //registry.addEndpoint("/ticks1").withSockJS();
    registry
    .addEndpoint("/ticks")
    .setHandshakeHandler(new DefaultHandshakeHandler() {

      public boolean beforeHandshake(
          ServerHttpRequest request, 
          ServerHttpResponse response, 
          WebSocketHandler wsHandler,
          Map attributes) throws Exception {

        if (request instanceof ServletServerHttpRequest) {
          ServletServerHttpRequest servletRequest
          = (ServletServerHttpRequest) request;
          HttpSession session = servletRequest
              .getServletRequest().getSession();
          attributes.put("sessionId", session.getId());
        }
        return true;
      }

      public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
          Exception ex) {

        if(ex!=null)
          System.out.println(" connection threw error in after handshake method "+ex.getMessage());
      }
      

    }).withSockJS();
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void configureClientOutboundChannel(ChannelRegistration arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean configureMessageConverters(List<MessageConverter> arg0) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void configureWebSocketTransport(WebSocketTransportRegistration arg0) {
    // TODO Auto-generated method stub

  }

}
package transact.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import transact.components.WebSocketAdapter;

@Configuration
public class WebSocketConfig {

  @Bean
  public ServerEndpointExporter endpointExporter() {
    return new ServerEndpointExporter();
  }

  @Bean
  public WebSocketAdapter webSocketAdapterBean(){
    return new WebSocketAdapter();
  }

}

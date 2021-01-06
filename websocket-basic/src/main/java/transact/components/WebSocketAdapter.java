package transact.components;

import java.io.IOException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import transact.beans.request.SubscriptionReq;
import transact.config.CustomSpringConfigurator;
import transact.util.MessageDecoder;
import transact.util.MessageEncoder;

@Slf4j
@ServerEndpoint(value = "/ticks",
    configurator = CustomSpringConfigurator.class,
    decoders = MessageDecoder.class,
    encoders = MessageEncoder.class )
public class WebSocketAdapter {

  @Autowired
  CountActiveConnectionsComponent activeConnectionsComponent;

  @Autowired
  UserSessionService userSessionService;

  @OnOpen
  public void onOpen(Session session) throws IOException {
    activeConnectionsComponent.incConnCount();
    log.info(" Session Opened, sessionId {}",session.getId());
  }

  @OnMessage
  public void onMessage(Session session, SubscriptionReq message) throws IOException {
    log.info(" SessionId: {}, received message: {}",session.getId(), message);
    userSessionService.handleUserMessage( session, message );
  }

  @OnClose
  public void onClose(Session session) throws IOException {
    activeConnectionsComponent.decConnCount();
    userSessionService.handleDisconnectEvent( session );
    log.info(" Closed sessionId {}",session.getId());
  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    log.info(" session {}, error {}",session,throwable);
  }
}
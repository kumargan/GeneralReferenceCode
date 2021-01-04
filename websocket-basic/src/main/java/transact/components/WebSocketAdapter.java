package transact.components;

import java.io.IOException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.apache.logging.log4j.message.Message;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ServerEndpoint(value = "/ticks")
public class WebSocketAdapter {

  @OnOpen
  public void onOpen(Session session) throws IOException {
    log.info(" opened {}",session);
  }

  @OnMessage
  public void onMessage(Session session, Message message) throws IOException {
    log.info(" message {}",message.getFormattedMessage());
  }

  @OnClose
  public void onClose(Session session) throws IOException {
    log.info(" closed {}",session);
  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    log.info(" session {}, error {}",session,throwable);
  }
}
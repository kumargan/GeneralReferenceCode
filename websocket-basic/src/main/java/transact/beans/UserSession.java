package transact.beans;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import javax.websocket.Session;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class UserSession implements Closeable {

  private static int QUEUE_SIZE = 10000;

  private String sessionId;
  private Session session;
  private BlockingDeque<String> messageQueue = new LinkedBlockingDeque<>( 10000 );

  public UserSession( Session session){
    this.session = session;
    this.sessionId = session.getId();
  }

  @Override
  public boolean equals( Object other){
    if (other == null){
      return false;
    }
    if (this.getClass() != other.getClass()){
      return false;
    }
    UserSession otherUserSession = (UserSession) other;
    if (this.session.getClass() != otherUserSession.getSession().getClass()){
      return false;
    }
    if (this.getSession().getId().equals( otherUserSession.getSession().getId())){
      return true;
    }
    return false;
  }

  @Override
  public int hashCode(){
    return Objects.hash( this.session.getId() );
  }

  @Override
  public void close() throws IOException {
    log.info("Object being destroyed for sessionId {} ", this.session.getId());
  }
}

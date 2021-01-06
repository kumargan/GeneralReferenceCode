package transact.util;

import static transact.constants.CommonConstants.objectMapper;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import lombok.SneakyThrows;
import transact.beans.request.SubscriptionReq;

public class MessageEncoder implements Encoder.Text<SubscriptionReq> {

  @SneakyThrows
  @Override
  public String encode(SubscriptionReq message) throws EncodeException {
    return objectMapper.writeValueAsString( message );
  }

  @Override
  public void init(EndpointConfig endpointConfig) {
    // Custom initialization logic
  }

  @Override
  public void destroy() {
    // Close resources
  }
}
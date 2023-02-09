package transact.util;

import static transact.constants.CommonConstants.objectMapper;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import lombok.SneakyThrows;
import transact.beans.request.SubscriptionReq;

public class MessageDecoder implements Decoder.Text<SubscriptionReq> {


  @SneakyThrows
  @Override
  public SubscriptionReq decode(String s) throws DecodeException {
    return objectMapper.readValue( s, SubscriptionReq.class);
  }

  @Override
  public boolean willDecode(String s) {
    return (s != null);
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

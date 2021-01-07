package transact.util;

import lombok.SneakyThrows;
import transact.beans.response.SubscriptionRes;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import static transact.constants.CommonConstants.objectMapper;

public class MessageEncoder implements Encoder.Text<SubscriptionRes> {

    @SneakyThrows
    @Override
    public String encode(SubscriptionRes message) throws EncodeException {
        return objectMapper.writeValueAsString(message);
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
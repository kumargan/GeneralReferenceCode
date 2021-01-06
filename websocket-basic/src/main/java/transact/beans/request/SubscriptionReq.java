package transact.beans.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import transact.beans.enums.Type;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionReq {

    private Long pmlId;
    private Type type;

}


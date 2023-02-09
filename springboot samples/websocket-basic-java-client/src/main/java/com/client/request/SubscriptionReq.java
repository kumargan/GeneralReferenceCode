package com.client.request;

import com.client.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionReq {

    private Long pmlId;
    private Type type;

}


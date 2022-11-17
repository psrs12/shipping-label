package com.amazon.seller.shipping.web.request;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShippingLabelRequest {
    private String clientId;
    private String clientSecret;
    private String refreshToken;
    private String accessKey;
    private String secretKey;
    private String region;
    private String orderNumber;
    private String sellingPartyId;
    private String shipFromPartyId;
    private String containerList;
}

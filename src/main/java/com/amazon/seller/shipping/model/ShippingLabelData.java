package com.amazon.seller.shipping.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShippingLabelData {
    private String shipMethod;
    private String packageIdentifier;
    private String trackingNumber;
    private String content;
}

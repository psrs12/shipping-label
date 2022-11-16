package com.amazon.seller.shipping.service;

import com.amazon.seller.shipping.model.ShipFromParty;
import com.amazon.seller.shipping.model.SellingParty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShippingLabelModelRequest {
    private SellingParty sellingParty;
    private ShipFromParty shipFromParty;
}




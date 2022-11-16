package com.amazon.seller.shipping.service;

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

class SellingParty{
    private String partyId;

    public SellingParty() {
    }
    public SellingParty(String partyId) {
        this.partyId = partyId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }
}

class ShipFromParty{
    private String partyId;

    public ShipFromParty() {

    }
    public ShipFromParty(String partyId) {
        this.partyId = partyId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }
}
package com.amazon.seller.shipping.service;

import com.amazon.seller.shipping.model.SellingParty;
import com.amazon.seller.shipping.model.ShipFromParty;
import com.amazon.seller.shipping.model.ShippingLabelData;
import com.amazon.seller.shipping.model.Error;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShippingLabelModelResponse {

    private ArrayList<ShippingLabelData> labelData;
    private SellingParty sellingParty;
    private String labelFormat;
    private String purchaseOrderNumber;
    public ShipFromParty shipFromParty;
    public List<Error> errors;
}

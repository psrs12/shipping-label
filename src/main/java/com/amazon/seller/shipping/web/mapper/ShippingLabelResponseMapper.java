package com.amazon.seller.shipping.web.mapper;

import com.amazon.seller.shipping.web.response.ShippingLabelResponse;

public class ShippingLabelResponseMapper {

    public static ShippingLabelResponse newShippingLabelResponse(String shippingLabel){
        return ShippingLabelResponse.builder().shippingLabel(shippingLabel).build();
    }

}

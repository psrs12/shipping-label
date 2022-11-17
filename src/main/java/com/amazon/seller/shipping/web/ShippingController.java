package com.amazon.seller.shipping.web;

import com.amazon.seller.shipping.service.ShippingLabelModelResponse;
import com.amazon.seller.shipping.service.ShippingService;
import com.amazon.seller.shipping.web.mapper.ShippingLabelResponseMapper;
import com.amazon.seller.shipping.web.request.ShippingLabelRequest;
import com.amazon.seller.shipping.web.response.ShippingLabelResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@CrossOrigin(origins = "*",allowedHeaders = "*",maxAge=3600)
public class ShippingController {
    @Autowired
    private ShippingService shippingService;

    @PostMapping(value = "/createLabel")
    public ShippingLabelModelResponse createShippingLabel(@RequestBody ShippingLabelRequest request) throws Exception {
        log.info("The shipping label request from vendor is "+request.toString());
        ShippingLabelModelResponse response = shippingService.createShippingLabel(request);
            return response;
    }
}

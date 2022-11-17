package com.amazon.seller.shipping.service;

import com.amazon.SellingPartnerAPIAA.AWSSigV4Signer;
import com.amazon.seller.shipping.model.SellingParty;
import com.amazon.seller.shipping.model.ShipFromParty;
import com.amazon.seller.shipping.service.helper.TokenCreator;
import com.amazon.seller.shipping.web.request.ShippingLabelRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.squareup.okhttp.*;
import io.swagger.client.model.RestrictedResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class ShippingService {

    @Value("${shipping_label_endpoint}")
    private String shippingLabelEndpoint;

    @Value("${restricted.resource_path}")
    private String resourcePath;

    @Value("${restricted.resource_method}")
    private String resourceMethod;


    @Autowired
    TokenCreator tokenCreator;

    public ShippingLabelModelResponse createShippingLabel(ShippingLabelRequest shippingLabelRequest) throws Exception {
        String[] dataElements = {};
        String restrictedDataToken = tokenCreator.getRestrictedDataToken(shippingLabelRequest, resourcePath, dataElements);
        log.info("The restricted data token is " + restrictedDataToken);
        RestrictedResource restrictedResource = new RestrictedResource();
        restrictedResource.setMethod(RestrictedResource.MethodEnum.fromValue(resourceMethod));
        restrictedResource.setPath(shippingLabelRequest.getOrderNumber());
        Response response = buildAndExecuteRestrictedRequest(shippingLabelRequest, restrictedResource, restrictedDataToken, getRequestBody(shippingLabelRequest));
        log.info("Shipping label success");
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body().string(),ShippingLabelModelResponse.class);

    }

    private RequestBody getRequestBody(ShippingLabelRequest shippingLabelRequest){
        RequestBody requestBody=null;
        Gson gson = new Gson();
        if(StringUtils.isEmpty(shippingLabelRequest.getContainerList()) ){
            ShippingLabelModelRequest shippingLabelModelRequest = ShippingLabelModelRequest
                    .builder()
                    .shipFromParty(new ShipFromParty(shippingLabelRequest.getShipFromPartyId()))
                    .sellingParty(new SellingParty(shippingLabelRequest.getSellingPartyId()))
                    .build();

             requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(shippingLabelModelRequest));
        }else{
            String contentList=shippingLabelRequest.getContainerList().replaceAll("\n", "");
            log.info("the content list "+contentList);
            requestBody = RequestBody.create(MediaType.parse("application/json"), contentList);
        }
        return requestBody;
    }
    // An example of a helper method to build, sign, and execute a restricted operation, specifying RestrictedResource, (String) RDT, and RequestBody.
    // Returns the restricted operation Response object.
    private Response buildAndExecuteRestrictedRequest(ShippingLabelRequest request, RestrictedResource resource, String restrictedDataToken, RequestBody requestBody) throws IOException {
        // Construct a request with the specified RestrictedResource, RDT, and RequestBody.
        Request signedRequest = new Request.Builder()
                .url(shippingLabelEndpoint + resource.getPath())  // Define the URL for the request, based on the endpoint and restricted resource path.
                .method(resource.getMethod().getValue(), requestBody)  // Define the restricted resource method value, and requestBody, if required by the restricted operation.
                .addHeader("x-amz-access-token", restrictedDataToken) // Sign the request with the RDT by adding it to the "x-amz-access-token" header.
                .build(); // Build the request.

        // Initiate an AWSSigV4Signer instance using your AWS credentials. This example is for an application registered using an AIM Role.
        //  AWSSigV4Signer awsSigV4Signer = new AWSSigV4Signer(awsAuthenticationCredentials, awsAuthenticationCredentialsProvider);


        // Or, if the application was registered using an IAM User, use the following example:
        AWSSigV4Signer awsSigV4Signer = new AWSSigV4Signer(tokenCreator.getAWSAuthenticationCredentials(request));


        // Sign the request with the AWSSigV4Signer.
        signedRequest = awsSigV4Signer.sign(signedRequest);

        // Execute the signed request.
        OkHttpClient okHttpClient = new OkHttpClient();
        Response response = okHttpClient.newCall(signedRequest).execute();
        return response;
    }


}

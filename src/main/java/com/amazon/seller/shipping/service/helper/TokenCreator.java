package com.amazon.seller.shipping.service.helper;

import com.amazon.SellingPartnerAPIAA.AWSAuthenticationCredentials;
import com.amazon.SellingPartnerAPIAA.LWAAuthorizationCredentials;
import com.amazon.seller.shipping.web.request.ShippingLabelRequest;
import io.swagger.client.api.TokensApi;
import io.swagger.client.model.CreateRestrictedDataTokenRequest;
import io.swagger.client.model.CreateRestrictedDataTokenResponse;
import io.swagger.client.model.RestrictedResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TokenCreator {


    @Value("${lwa.endpoint}")
    private String endPoint;

    @Value("${RDT_endpoint}")
    private String RDTEndPoint;


    public String getRestrictedDataToken(ShippingLabelRequest request, String resourcePath, String[] resources) throws Exception {

        // Define the dataElements to indicate the type of Personally Identifiable Information requested.
        // This parameter is required only when getting an RDT for use with the getOrder, getOrders, or getOrderItems operation of the Orders API.
        final List<String> dataElements = Arrays.asList(resources);

        // Build the RestrictedResource object specifying method (MethodEnum from RestrictedResource class), path and dataElements parameters.
        RestrictedResource resource = buildRestrictedResource(RestrictedResource.MethodEnum.POST, resourcePath, dataElements);

        // Make a list of the RestrictedResource objects that will be included in the request to create the RDT.
        List<RestrictedResource> resourceList = Arrays.asList(resource);


        TokensApi tokensApi = new TokensApi.Builder()
                .awsAuthenticationCredentials(getAWSAuthenticationCredentials(request))
                //.awsAuthenticationCredentialsProvider(awsAuthenticationCredentialsProvider)  // If the application uses User ARN, this line is not needed. Remove it or pass a null value.
                .lwaAuthorizationCredentials(getLWAAuthorizationCredentials(request))
                .endpoint(RDTEndPoint)
                .build();

        // Get an RDT for the list of restricted resources.
        return getRestrictedDataToken(resourceList, tokensApi);

    }


    private LWAAuthorizationCredentials getLWAAuthorizationCredentials(ShippingLabelRequest request) {
        return LWAAuthorizationCredentials.builder()
                .clientId(request.getClientId())
                .clientSecret(request.getClientSecret())
                .refreshToken(request.getRefreshToken())
                .endpoint(endPoint).build();
    }

    public AWSAuthenticationCredentials getAWSAuthenticationCredentials(ShippingLabelRequest request) {
        return AWSAuthenticationCredentials.builder()
                // If you registered your application using an IAM Role ARN, use the AWS credentials of an IAM User that is linked to the IAM Role through the AWS Security Token Service policy.
                // Or, if you registered your application using an IAM User ARN, use the AWS credentials of that IAM User. Be sure that the IAM User has the correct IAM policy attached.
                .accessKeyId(request.getAccessKey())
                .secretKey(request.getSecretKey())
                .region(request.getRegion())
                .build();
    }

    // An example of a helper method for building RestrictedResource objects with dataElements parameters.
    private static RestrictedResource buildRestrictedResource(RestrictedResource.MethodEnum method, String path, List<String> dataElements) {
        RestrictedResource resource = buildRestrictedResource(method, path);
        resource.dataElements(dataElements);
        return resource;
    }

    // An example of a helper method for building RestrictedResource objects, specifying the method (MethodEnum from RestrictedResource class) and path parameters.
    private static RestrictedResource buildRestrictedResource(RestrictedResource.MethodEnum method, String path) {
        RestrictedResource resource = new RestrictedResource();
        resource.setMethod(method);
        resource.setPath(path);
        return resource;
    }

    // An example of a helper method for creating an RDT for a list of RestrictedResource objects.
    private String getRestrictedDataToken(List<RestrictedResource> resourceList, TokensApi tokensApi) throws Exception {
        // Initialize a CreateRestrictedDataTokenRequest object that represents the Restricted Data Token request body.
        CreateRestrictedDataTokenRequest restrictedDataTokenRequest = new CreateRestrictedDataTokenRequest();
        // Add a resource list to the CreateRestrictedDataTokenRequest object.
        restrictedDataTokenRequest.setRestrictedResources(resourceList);
        CreateRestrictedDataTokenResponse response = tokensApi.createRestrictedDataToken(restrictedDataTokenRequest);
        return response.getRestrictedDataToken();


    }
}
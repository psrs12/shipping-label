# Shipping Label Service

A Spring Boot service that generates shipping labels for Amazon Vendor Direct Fulfillment orders by calling the Amazon Selling Partner API (SP-API). It handles LWA authorization, requests a Restricted Data Token (RDT), signs the request with AWS SigV4, and forwards it to the SP-API `shippingLabels` endpoint.

## Tech Stack

- Java 11
- Spring Boot 2.7.5 (Web, Autoconfigure, Configuration Processor)
- springdoc-openapi (Swagger UI)
- Amazon Selling Partner API Java client (`swagger-java-client`)
- OkHttp, Gson, Jackson
- Lombok
- Maven

## Prerequisites

- JDK 11+
- Maven 3.6+
- Amazon SP-API credentials (LWA client ID/secret, refresh token) and AWS IAM credentials with access to the Vendor Direct Fulfillment Shipping API

## Configuration

Application configuration lives in `src/main/resources/application.yaml`:

```yaml
lwa:
  client_id:
  client_secret:
  endpoint: https://api.amazon.com/auth/O2/token
  refresh_token:
aws:
  access_key:
  secret_key:
  region:
restricted:
  resource_path: /vendor/directFulfillment/shipping/2021-12-28/shippingLabels/{something}
  resource_method: POST

RDT_endpoint: https://sellingpartnerapi-na.amazon.com
shipping_label_endpoint: https://sellingpartnerapi-na.amazon.com/vendor/directFulfillment/shipping/2021-12-28/shippingLabels/
```

Note: LWA and AWS credentials can also be supplied per-request in the request body (see [API](#api) below), which takes precedence over static configuration.

## Building

```bash
mvn clean install
```

This produces an executable jar at `target/shipping-label-1.0-SNAPSHOT.jar`.

## Running

```bash
mvn spring-boot:run
```

or

```bash
java -jar target/shipping-label-1.0-SNAPSHOT.jar
```

By default the app starts on `http://localhost:8080`.

## API

### `POST /createLabel`

Creates a shipping label for a Direct Fulfillment order.

**Request body:**

```json
{
  "clientId": "",
  "clientSecret": "",
  "refreshToken": "",
  "accessKey": "",
  "secretKey": "",
  "region": "",
  "orderNumber": "",
  "sellingPartyId": "",
  "shipFromPartyId": "",
  "containerList": ""
}
```

- `orderNumber` — the purchase order number for the shipment; used as the resource path suffix.
- `sellingPartyId` / `shipFromPartyId` — used to build a default request body when `containerList` is not provided.
- `containerList` — optional raw JSON container/package list; if provided, it is sent as-is instead of the default `shipFromParty`/`sellingParty` payload.

**Response:** the SP-API shipping label response, including label data, selling party, ship-from party, label format, and purchase order number.

Interactive API docs (Swagger UI) are available at `/swagger-ui.html` once the app is running.

## Project Structure

```
src/main/java/com/amazon/seller/shipping/
├── App.java                          # Spring Boot entry point
├── model/                            # Shared domain models (SellingParty, ShipFromParty, ShippingLabelData, Error)
├── service/
│   ├── ShippingService.java          # Builds/signs/executes the SP-API request
│   ├── helper/TokenCreator.java      # Obtains LWA-authorized Restricted Data Tokens
│   ├── ShippingLabelModelRequest.java
│   └── ShippingLabelModelResponse.java
└── web/
    ├── ShippingController.java       # REST controller (POST /createLabel)
    ├── request/ShippingLabelRequest.java
    ├── response/ShippingLabelResponse.java
    └── mapper/ShippingLabelResponseMapper.java
```

A static frontend (Angular build output) is also bundled under `src/main/resources/static/`.

## Testing

```bash
mvn test
```

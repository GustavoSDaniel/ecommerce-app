package com.gustavosdaniel.ecommerce_api.payment;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "method"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreditCardDetails.class, name = "CREDIT_CARD"),
        @JsonSubTypes.Type(value = DebitCardDetails.class, name = "DEBIT_CARD"),
        @JsonSubTypes.Type(value = PixDetails.class, name = "PIX"),
        @JsonSubTypes.Type(value = BoletoDetails.class, name = "BOLETO")
})
public sealed interface PaymentDetails
        permits
        CreditCardDetails,
        DebitCardDetails,
        PixDetails,
        BoletoDetails {
}

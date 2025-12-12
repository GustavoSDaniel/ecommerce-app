package com.gustavosdaniel.ecommerce_api.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    @Value("${stripe.api-key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {

        if (stripeApiKey == null || stripeApiKey.isBlank()) {

        System.out.println("❌❌❌ ERRO GRAVE: A chave do Stripe está VAZIA! ❌❌❌");

    } else {

        System.out.println("✅ Configuração do Stripe carregada. Chave começa com: "
                + stripeApiKey.substring(0, Math.min(stripeApiKey.length(), 7)) + "...");
    }

        Stripe.apiKey = stripeApiKey;
    }
}

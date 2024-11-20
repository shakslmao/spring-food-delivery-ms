package com.devshaks.delivery.payments;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Integer> createPayment(@RequestBody @Valid PaymentRequest paymentRequest) {
        log.info("Creating payment: {}", paymentRequest);
        Integer request = paymentService.createPayment(paymentRequest);
        log.info("Payment created with ID: {}", request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/stripe")
    public ResponseEntity<StripePaymentResponse> createStripePayment(
            @RequestBody @Valid PaymentRequest paymentRequest) {
        log.info("Processing Stripe payment: {}", paymentRequest);
        StripePaymentResponse response = paymentService.createStripePayment(paymentRequest);
        log.info("Stripe payment processed with ID: {}", response.paymentIntentId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stripe/webhook")
    public ResponseEntity<String> handleStripeWebHook(@RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature) {
        try {
            Event event = Webhook.constructEvent(payload, signature, "");
            if ("payment_intent.succeeded".equals(event.getType())) {
                PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject().orElseThrow();
                log.info("Payment succeeded for PaymentIntent ID: {}", intent.getId());

                // update database status

            }

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error handling Stripe webhook: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook error");
        }
    }
}

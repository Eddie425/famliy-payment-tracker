package com.eddie.famliy_payment_tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment APIs", description = "General payment-related endpoints")
public class PaymentController {

	@Operation(
			summary = "Get payment status",
			description = "Returns a simple status message indicating the API is operational"
	)
	@GetMapping
	public ResponseEntity<Map<String, String>> getAllPayments() {
		Map<String, String> response = new HashMap<>();
		response.put("message", "This is an API-only project");
		response.put("status", "success");
		return ResponseEntity.ok(response);
	}
}






















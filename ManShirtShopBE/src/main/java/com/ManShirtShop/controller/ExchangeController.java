package com.ManShirtShop.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/exchange")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "exchange api")
public class ExchangeController {
}

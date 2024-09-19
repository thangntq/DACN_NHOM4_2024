package com.ManShirtShop.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/exchangeDetail")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "exchangeDetail api")
public class ExchangeDetailController {
}

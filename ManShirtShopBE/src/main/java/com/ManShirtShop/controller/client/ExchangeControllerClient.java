package com.ManShirtShop.controller.client;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "client/api/exchange")
@Tag(name = "Echange Client api")
public class ExchangeControllerClient {
}

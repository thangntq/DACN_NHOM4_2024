package com.ManShirtShop.dto.client.check_out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CheckOutRequestClient {
    private Integer id;
    private int quantity;
}

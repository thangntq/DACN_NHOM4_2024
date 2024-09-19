package com.ManShirtShop.dto.returnDetail;

import com.ManShirtShop.dto.order_the_store.OrderDetailResponseAdminTemp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnDetailAdminUpdateRequest {
    private Integer returnDetailId ;
    private Integer quantity;
}

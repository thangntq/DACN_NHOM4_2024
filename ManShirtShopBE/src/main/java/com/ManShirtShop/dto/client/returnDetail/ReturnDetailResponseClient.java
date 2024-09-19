package com.ManShirtShop.dto.client.returnDetail;

import com.ManShirtShop.dto.client.oderDetailDto.OrderDetailResponseClient;
import com.ManShirtShop.dto.client.returns.ReturnResponseClient;
import com.ManShirtShop.dto.order_the_store.OderDetailResponseAdmin;
import com.ManShirtShop.dto.returns.ReturnResponseAdmin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnDetailResponseClient {
    private Integer id ;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;
    private Integer quantity;
    private ReturnResponseClient returnResponseClient;
    private OrderDetailResponseClient orderDetailResponseClient;
}

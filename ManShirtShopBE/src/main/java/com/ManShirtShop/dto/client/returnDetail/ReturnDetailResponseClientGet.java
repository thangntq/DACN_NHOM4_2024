package com.ManShirtShop.dto.client.returnDetail;

import com.ManShirtShop.dto.client.oderDto.OderDetailResponseClientGet;
import com.ManShirtShop.dto.client.returns.ReturnResponseClient;
import com.ManShirtShop.dto.order_the_store.OderDetailResponseAdminGet;
import com.ManShirtShop.dto.returns.ReturnResponseAdmin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnDetailResponseClientGet {
    private Integer id ;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;
    private Integer quantity;
    private ReturnResponseClient returnResponseAdmin;
    private OderDetailResponseClientGet oderDetailResponseAdmin;
}

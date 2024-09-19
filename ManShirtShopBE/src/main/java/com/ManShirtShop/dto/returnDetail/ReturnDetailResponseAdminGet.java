package com.ManShirtShop.dto.returnDetail;

import com.ManShirtShop.dto.order_the_store.OderDetailResponseAdmin;
import com.ManShirtShop.dto.order_the_store.OderDetailResponseAdminGet;
import com.ManShirtShop.dto.returns.ReturnResponseAdmin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnDetailResponseAdminGet {
    private Integer id ;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;
    private Integer quantity;
    private ReturnResponseAdmin returnResponseAdmin;
    private OderDetailResponseAdminGet oderDetailResponseAdmin;
}

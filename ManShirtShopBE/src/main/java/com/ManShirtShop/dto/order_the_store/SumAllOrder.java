package com.ManShirtShop.dto.order_the_store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SumAllOrder {
    private Integer choDuyet;
    private Integer ChuanBiHang;
    private Integer dangGiao;
    private Integer thanhCong;
    private Integer daHuy;
    private Integer thatBai;
}

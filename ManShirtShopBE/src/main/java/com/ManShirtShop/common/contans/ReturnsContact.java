package com.ManShirtShop.common.contans;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class ReturnsContact {
    public static final Integer STATUS_DEFAULT = 0;// Status tạo yêu cầu trả hàng (Chờ xác nhận )

    public static final Integer STATUS_DANG_GIAO = 1;// Status đã xác nhận yêu cầu trả hàng và đồng ý cho giao hàng

    public static final Integer STATUS_THANH_CONG = 2;// Status trả hàng thành công

    public static final Integer STATUS_DA_HUY = 3;// Status từ chối trả hàng

    public static final Integer STATUS_TAO_DON_LOI = 4;// Status tạo đơn lỗi

}

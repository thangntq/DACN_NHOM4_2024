package com.ManShirtShop.service.client.returns;

import com.ManShirtShop.dto.client.returnDetail.ReturnDetailCLientAddRequest;
import com.ManShirtShop.dto.client.returnDetail.ReturnDetailResponseClient;
import com.ManShirtShop.dto.client.returns.ReturnRequestClient;
import com.ManShirtShop.dto.client.returns.ReturnResponseClient;
import com.ManShirtShop.dto.client.returns.ReturnResponseClientGetAll;
import com.ManShirtShop.dto.returnDetail.ReturnDetailAdminAddRequest;
import com.ManShirtShop.dto.returnDetail.ReturnDetailResponseAdmin;
import com.ManShirtShop.dto.returns.ReturnResponseAdmin;

import java.util.List;

public interface ReturnServiceClient {
    ReturnResponseClient addReturns(ReturnRequestClient returnRequestClient);

    ReturnResponseClient updateDangGiao(Integer returnId);

    ReturnResponseClient updateSuccess(Integer returnId);

    ReturnResponseClient updateTuChoi(Integer returnId);

    ReturnResponseClient updateRefuse(Integer returnId);
    ReturnDetailResponseClient addReturnsDetail(ReturnDetailCLientAddRequest returnDetailCLientAddRequest);

    ReturnResponseClientGetAll findReturnAndDetailByCode(String code);
}

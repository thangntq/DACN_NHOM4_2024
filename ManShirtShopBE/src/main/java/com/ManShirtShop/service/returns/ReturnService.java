package com.ManShirtShop.service.returns;

import com.ManShirtShop.dto.returnDetail.ReturnDetailAdminUpdateRequest;
import com.ManShirtShop.dto.returnDetail.ReturnDetailResponseAdmin;
import com.ManShirtShop.dto.returnDetail.ReturnDetailAdminAddRequest;
import com.ManShirtShop.dto.returns.ReturnFullAdmin;
import com.ManShirtShop.dto.returns.ReturnRequestAdmin;
import com.ManShirtShop.dto.returns.ReturnResponseAdmin;
import com.ManShirtShop.dto.returns.ReturnResponseAdminGetAll;

import java.util.List;

public interface ReturnService {
    ReturnResponseAdmin addReturns(ReturnRequestAdmin returnRequestAdmin);

    ReturnResponseAdmin updateSuccess(Integer returnId);

    ReturnResponseAdmin updateRefuse(Integer returnId);

//    List<ReturnDetailResponseAdmin> findAllReturnDetailsByStatusOfReturn(Integer returnId, Integer status);

    ReturnDetailResponseAdmin updateReturnsDeatail(ReturnDetailAdminUpdateRequest returnDetailAdminUpdateRequest);

    ReturnDetailResponseAdmin addReturnsDetail(ReturnDetailAdminAddRequest returnDetailAdminAddRequest);

    ReturnResponseAdminGetAll findReturnAndDetailByCode(String code);

    List<ReturnFullAdmin> findAllByAdmin(Integer status);
}

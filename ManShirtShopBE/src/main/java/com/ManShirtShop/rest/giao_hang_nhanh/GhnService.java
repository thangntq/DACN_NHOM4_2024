package com.ManShirtShop.rest.giao_hang_nhanh;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface GhnService {
    String ghncreate(Integer lstId);
    String getprintA5(String code);
}

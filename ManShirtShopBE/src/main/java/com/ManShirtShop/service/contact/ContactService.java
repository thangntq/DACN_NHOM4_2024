package com.ManShirtShop.service.contact;

import com.ManShirtShop.dto.contact.ContactRequest;
import com.ManShirtShop.dto.contact.ContactResponse;

import java.util.List;

public interface ContactService {

    List<ContactResponse> getAll();

    ContactResponse create(ContactRequest request);

    ContactResponse update(ContactRequest request);

    ContactResponse delete(Integer id);

    ContactResponse findById(Integer id);



}

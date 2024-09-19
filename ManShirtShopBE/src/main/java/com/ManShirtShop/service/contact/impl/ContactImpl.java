package com.ManShirtShop.service.contact.impl;

import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.contact.ContactRequest;
import com.ManShirtShop.dto.contact.ContactResponse;
import com.ManShirtShop.dto.design.DesignResponse;
import com.ManShirtShop.dto.voucher.VoucherResponse;
import com.ManShirtShop.entities.Contact;
import com.ManShirtShop.entities.Design;
import com.ManShirtShop.entities.Voucher;
import com.ManShirtShop.repository.ContactRepository;
import com.ManShirtShop.service.contact.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
@Service
public class ContactImpl implements ContactService {
    @Autowired
    ContactRepository contactRepository;
    @Override
    public List<ContactResponse> getAll() {
        List<ContactResponse> listAll = ObjectMapperUtils.mapAll(contactRepository.findAll(), ContactResponse.class)
                .stream().filter(e->e.getStatus()==0).toList();;
        return listAll;
    }

    @Override
    public ContactResponse create(ContactRequest request) {
            request.setId(null);
            Contact entity = ObjectMapperUtils.map(request, Contact.class);
            entity.setCreateBy("admin");
            entity.setCreateTime(Timestamp.from(Instant.now()));
            entity = contactRepository.save(entity);
            ContactResponse respone = ObjectMapperUtils.map(entity, ContactResponse.class);
            return respone;
    }


    public Boolean checkIb(Integer id) {
        if (id == 0 || id == null) {
            return false;
        }
        if (!contactRepository.existsById(id)) { // check id db
            return false;
        }
        return true;
    }
    @Override
    public ContactResponse update(ContactRequest request) {
        if (!checkIb(request.getId())) {
            return null;
        }
        Contact eDb = contactRepository.findById(request.getId()).get();
        Contact e = ObjectMapperUtils.map(request, Contact.class);
        e.setUpdateBy("admin");
        e.setUpdateTime(Timestamp.from(Instant.now()));
        e.setCreateBy(eDb.getCreateBy());
        e.setCreateTime(eDb.getCreateTime());
        return ObjectMapperUtils.map(contactRepository.save(e), ContactResponse.class);
    }

    @Override
    public ContactResponse delete(Integer id) {
        if (!checkIb(id)) {
            return null;
        }
        Contact e = contactRepository.findById(id).get();
        e.setStatus(1);
        e = contactRepository.save(e);
        return ObjectMapperUtils.map(e, ContactResponse.class);
    }

    @Override
    public ContactResponse findById(Integer id) {
        if (!checkIb(id)) {
            return null;
        }
        Contact contact = contactRepository.findById(id).get();
        return ObjectMapperUtils.map(contact, ContactResponse.class);
    }
    
    
}

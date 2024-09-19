package com.ManShirtShop.service.adress.Impl;

import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.adress.AdressRequest;
import com.ManShirtShop.dto.adress.AdressResponse;
import com.ManShirtShop.dto.customer.CustomerResponse;
import com.ManShirtShop.entities.Address;
import com.ManShirtShop.entities.Customer;
import com.ManShirtShop.repository.AdressRepository;
import com.ManShirtShop.repository.CustomerRepository;
import com.ManShirtShop.service.adress.AdressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AdressServiceImpl implements AdressService {

    @Autowired
    AdressRepository adressRepository;

    @Autowired
    CustomerRepository customerRepository;

    public Boolean checkIb(Integer id) {
        if (id == 0 || id == null) {
            return false;
        }
        if (!adressRepository.existsById(id)) { // check id db
            return false;
        }
        return true;
    }

    @Override
    public AdressResponse addAdress(AdressRequest adressRequest) {
        // Bước 1: Kiểm tra xem đã có địa chỉ mặc định cho customer_id hay chưa
        Integer customerId = 1;
        boolean isDefault = adressRequest.isDefault();
        boolean hasDefaultAddress = adressRepository.existsByCustomerIdAndIsDefaultTrue(customerId);

        // Bước 2: Kiểm tra và xử lý việc đặt isDefault cho địa chỉ mới
        if (isDefault) {
            if (hasDefaultAddress) {
                // Nếu đã có địa chỉ mặc định cho customer_id, cập nhật địa chỉ mặc định cũ thành không mặc định
                Address defaultAddress = adressRepository.findByCustomerIdAndIsDefaultTrue(customerId);
                defaultAddress.setIsDefault(false);
                adressRepository.save(defaultAddress);
            }
        }

        // Bước 3: Tạo mới địa chỉ và lưu vào cơ sở dữ liệu
        Address adress = new Address();
        adress.setCreateBy("Admin");
        adress.setCreateTime(Timestamp.from(Instant.now()));
        adress.setStatus(0);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer ID không hợp lệ."));
        adress.setCustomer(customer);
        adress.setAddress(adressRequest.getAddress());
        adress.setCityName(adressRequest.getCityName());
        adress.setDistrictName(adressRequest.getDistrictName());
        adress.setWardName(adressRequest.getWardName());
        adress.setOther(adressRequest.getOther());
        adress.setIdCity(adressRequest.getIdCity());
        adress.setIdDistrict(adressRequest.getIdDistrict());
        adress.setIdWard(adressRequest.getIdWard());
        adress.setIsDefault(isDefault);

        adress = adressRepository.save(adress);

        // Bước 4: Tạo và trả về đối tượng AdressResponse
        AdressResponse adressResponse = ObjectMapperUtils.map(adress, AdressResponse.class);
        adressResponse.setCustomerResponse(ObjectMapperUtils.map(customer, CustomerResponse.class));
        return adressResponse;
    }



    @Override
    public AdressResponse updateAdress(AdressRequest adressRequest) {
        Integer addressId = adressRequest.getId();

        // Bước 1: Kiểm tra xem địa chỉ muốn cập nhật có tồn tại trong cơ sở dữ liệu không
        Address existingAddress = adressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy địa chỉ với ID: " + addressId));

        // Bước 2: Kiểm tra xem người dùng muốn cập nhật địa chỉ mới thành mặc định hay không
        boolean isDefaultNewAddress = adressRequest.isDefault();

        // Bước 3: Kiểm tra xem đã có địa chỉ mặc định nào cho customer_id trong cơ sở dữ liệu hay chưa
        boolean hasDefaultAddress = adressRepository.existsByCustomerIdAndIsDefaultTrue(existingAddress.getCustomer().getId());

        // Bước 4: Xử lý việc cập nhật địa chỉ mặc định
        if (!hasDefaultAddress && isDefaultNewAddress) {
            // Nếu chưa có địa chỉ mặc định nào cho customer_id, và người dùng muốn cập nhật địa chỉ mới thành mặc định,
            // thì ta đặt địa chỉ mới là mặc định và lưu cập nhật vào cơ sở dữ liệu
            existingAddress.setIsDefault(true);
            existingAddress.setStatus(0);
            existingAddress.setAddress(adressRequest.getAddress());
            existingAddress.setCityName(adressRequest.getCityName());
            existingAddress.setDistrictName(adressRequest.getDistrictName());
            existingAddress.setWardName(adressRequest.getWardName());
            existingAddress.setOther(adressRequest.getOther());
            existingAddress.setIdCity(adressRequest.getIdCity());
            existingAddress.setIdDistrict(adressRequest.getIdDistrict());
            existingAddress.setIdWard(adressRequest.getIdWard());
            existingAddress.setUpdateBy("Admin");
            existingAddress.setUpdateTime(Timestamp.from(Instant.now()));
            adressRepository.save(existingAddress);
            System.out.println("000000000000");
        } else if (hasDefaultAddress && isDefaultNewAddress) {
            // Nếu đã có địa chỉ mặc định cho customer_id, và người dùng muốn cập nhật địa chỉ mới thành mặc định,
            // thì ta cần thực hiện thêm các bước xử lý:
            // 1. Update địa chỉ cũ (đã là mặc định) thành không mặc định (isDefault = false)
            // 2. Update địa chỉ mới thành mặc định (isDefault = true)
            Address defaultAddress = adressRepository.findByCustomerIdAndIsDefaultTrue(existingAddress.getCustomer().getId());
            defaultAddress.setIsDefault(false);
            existingAddress.setIsDefault(true);
            existingAddress.setStatus(0);
            existingAddress.setAddress(adressRequest.getAddress());
            existingAddress.setCityName(adressRequest.getCityName());
            existingAddress.setDistrictName(adressRequest.getDistrictName());
            existingAddress.setWardName(adressRequest.getWardName());
            existingAddress.setOther(adressRequest.getOther());
            existingAddress.setIdCity(adressRequest.getIdCity());
            existingAddress.setIdDistrict(adressRequest.getIdDistrict());
            existingAddress.setIdWard(adressRequest.getIdWard());
            existingAddress.setUpdateBy("Admin");
            existingAddress.setUpdateTime(Timestamp.from(Instant.now()));
            adressRepository.saveAll(Arrays.asList(defaultAddress, existingAddress));
            System.out.println("222222222222");
        } else {
            // Nếu người dùng không muốn cập nhật địa chỉ mới thành mặc định, thì ta chỉ cập nhật thông tin địa chỉ
            existingAddress.setIsDefault(false);
            existingAddress.setStatus(0);
            existingAddress.setAddress(adressRequest.getAddress());
            existingAddress.setCityName(adressRequest.getCityName());
            existingAddress.setDistrictName(adressRequest.getDistrictName());
            existingAddress.setWardName(adressRequest.getWardName());
            existingAddress.setOther(adressRequest.getOther());
            existingAddress.setIdCity(adressRequest.getIdCity());
            existingAddress.setIdDistrict(adressRequest.getIdDistrict());
            existingAddress.setIdWard(adressRequest.getIdWard());
            existingAddress.setUpdateBy("Admin");
            existingAddress.setUpdateTime(Timestamp.from(Instant.now()));
            adressRepository.save(existingAddress);
            System.out.println("1111111111");
        }

        // Bước 5: Tạo và trả về đối tượng AdressResponse với thông tin địa chỉ đã cập nhật
        AdressResponse updatedAddressResponse = ObjectMapperUtils.map(existingAddress, AdressResponse.class);
        updatedAddressResponse.setCustomerResponse(ObjectMapperUtils.map(existingAddress.getCustomer(), CustomerResponse.class));
        return updatedAddressResponse;
    }

    @Override
    public List<AdressResponse> getAllAdressByCustomerIdAndStatus(Integer customerId, Integer status) {
        List<Address> addresses;
        if (status != null) {
            addresses = adressRepository.findAllByCustomerIdAndStatus(customerId, status);
        } else {
            addresses = adressRepository.findAllByCustomerIdAndStatus(customerId, null);
        }
        List<AdressResponse> adressResponses = new ArrayList<>();
        for (Address address : addresses) {
            AdressResponse adressResponse = ObjectMapperUtils.map(address, AdressResponse.class);
            adressResponse.setCustomerResponse(ObjectMapperUtils.map(address.getCustomer(), CustomerResponse.class));
            adressResponses.add(adressResponse);
        }
        return adressResponses;
    }


    @Override
    public AdressResponse deleteAddress(Integer addressId) {
        Address existingAddress = adressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Address ID"));

        existingAddress.setStatus(0);

        Address deletedAddress = adressRepository.save(existingAddress);
        AdressResponse deletedAddressResponse = ObjectMapperUtils.map(deletedAddress, AdressResponse.class);
        deletedAddressResponse.setCustomerResponse(ObjectMapperUtils.map(deletedAddress.getCustomer(), CustomerResponse.class));

        return deletedAddressResponse;
    }



}

package com.ManShirtShop.Authentication.sercurity.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ManShirtShop.Authentication.custom.TokenUtilities;
import com.ManShirtShop.Authentication.dto.response.ResponseUser;
import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.entities.Address;
import com.ManShirtShop.entities.Customer;
import com.ManShirtShop.entities.Employee;
import com.ManShirtShop.entities.Role;
import com.ManShirtShop.repository.CustomerRepository;
import com.ManShirtShop.repository.EmployeeRepository;
import com.ManShirtShop.repository.RoleRepository;

@Service
public class AuthIm implements AuthService {
    @Autowired
    TokenUtilities tokenUtilities;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<ResponseUser> getInfomation(HttpServletRequest request) {
        String userName = "";
        userName = tokenUtilities.getClaimsProperty(request).get("sub").toString();
        int tmp = 0;
        Optional<Customer> customer = customerRepository.findByEmail(userName);
        List<String> roleNames = new ArrayList<>();
        Optional<Employee> employee = employeeRepository.findByEmail(userName);
        List<ResponseUser> users = new ArrayList<>();

        if (customer.isPresent()&& !employee.isPresent()) {
            tmp++;
            ResponseUser user = new ResponseUser();
            user.setBirthDate(customer.get().getBirthDate());
            user.setEmail(userName);
            user.setFullname(customer.get().getFullname());
            user.setPhone(customer.get().getPhone());
            Role role = customer.get().getRole();

            roleNames.add( "ROLE_" + role.getName());

           user.setRoles(roleNames);
            user.setStatus(customer.get().getStatus());
            users.add(user);
            return users;
        }
        if (employee.isPresent() && !customer.isPresent()) {
            tmp++;

            ResponseUser user = new ResponseUser();
            user.setAddress(employee.get().getAddress().toString());
            user.setBirthDate(employee.get().getBirthDate());
            user.setEmail(userName);
            user.setFullname(employee.get().getFullname());
            user.setPhoto(employee.get().getPhoto());
            user.setPhone(employee.get().getPhone());

            Role role = employee.get().getRole();
            roleNames.add( "ROLE_" + role.getName());

            user.setRoles(roleNames);

            user.setStatus(employee.get().getStatus());
            users.add(user);
            return users;
        }

        if (customer.isPresent() && employee.isPresent()) {
            tmp++;
            Role role = customer.get().getRole();
            roleNames.add(role.getName());
            ResponseUser employeeUser = new ResponseUser();
            employeeUser.setBirthDate(employee.get().getBirthDate());
            employeeUser.setEmail(userName);
            employeeUser.setFullname(employee.get().getFullname());
            employeeUser.setPhoto(employee.get().getPhoto());
            employeeUser.setPhone(employee.get().getPhone());
            Role role1 = employee.get().getRole();
            roleNames.add( "ROLE_" + role1.getName());
            employeeUser.setRoles(roleNames);
            employeeUser.setStatus(employee.get().getStatus());
            employeeUser.setAddress(employee.get().getAddress().toString());
            users.add(employeeUser);
            return users;

        }
        if (tmp == 0) {
            throw new IllegalAccessError("Invalid user");
        }

        return null;

    }

}

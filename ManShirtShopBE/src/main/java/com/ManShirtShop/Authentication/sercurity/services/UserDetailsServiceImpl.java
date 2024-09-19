package com.ManShirtShop.Authentication.sercurity.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ManShirtShop.entities.Customer;
import com.ManShirtShop.entities.Employee;
import com.ManShirtShop.repository.CustomerRepository;
import com.ManShirtShop.repository.EmployeeRepository;
import com.ManShirtShop.repository.RoleRepository;
import com.ManShirtShop.service.customer.CustomerService;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private RoleRepository roleRepo;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		int tmp = 0;
		Optional<Employee> opaccount = employeeRepository.findByEmail(email);
		if (opaccount.isPresent()) {
			tmp++;
			Employee account = opaccount.get();
			if (!account.getPassword().startsWith("$2a$")) {
				account.setPassword(encoder.encode(account.getPassword()));
				account.setId(account.getId());
				account.setEmail(account.getEmail());
				employeeRepository.save(account);
			}
		}
	
	
		Optional<Customer> opcustomer = customerRepository.findByEmail(email);
		if (opcustomer.isPresent()) {
			tmp++;
			Customer customer = opcustomer.get();
			if (!customer.getPassword().startsWith("$2a$")) {
				customer.setPassword(encoder.encode(customer.getPassword()));
				customer.setId(customer.getId());
				customer.setEmail(customer.getEmail());
				customerRepository.save(customer);
			}
		}

		if(tmp == 0){
			throw new IllegalAccessError("Invalid user");
		}
	
		ArrayList<String> roles = new ArrayList<>();
		Optional<Employee> roleIds = opaccount;
		Optional<Customer> roleId1 = opcustomer;
	
		if (roleIds != null ) {
			try {
				roles.add(roleRepo.findById(opaccount.get().getRole().getId()).get().getName());
			} catch (Exception e) {
			}
		}
	
		if (roleId1 != null) {
			try {
				roles.add(roleRepo.findById(opcustomer.get().getRole().getId()).get().getName());
			} catch (Exception e) {
			}
		}
		if (opaccount.isPresent() && opcustomer.isPresent()) {
			return UserDetailsImpl.build(opaccount.get(), opcustomer.get(), roles);
		} else {
			if (opaccount.isPresent()) {
				return UserDetailsImpl.build(opaccount.get(), null, roles);
			} else {
				return UserDetailsImpl.build(null, opcustomer.get(), roles);
			}
		}
	}
	
}
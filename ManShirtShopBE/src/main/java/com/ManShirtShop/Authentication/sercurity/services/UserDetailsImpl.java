package com.ManShirtShop.Authentication.sercurity.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ManShirtShop.entities.Customer;
import com.ManShirtShop.entities.Employee;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Timestamp;


public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	private Integer id;

	private String username;

	private Integer status;

	private Timestamp createTime;

	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(Integer id, String username, String password, Integer status,
	Timestamp createTime,  Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
		this.status = status;
		this.createTime = createTime;
	}

	public static UserDetailsImpl build(Employee account, Customer customer,ArrayList<String> roles) {
		
		List<GrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
				.collect(Collectors.toList());
		if (account != null) {
			return new UserDetailsImpl(account.getId(), account.getEmail(), account.getPassword(),
					account.getStatus(), account.getCreateTime(), authorities);

		}
		if (customer != null) {
			return new UserDetailsImpl(customer.getId(), customer.getEmail(), customer.getPassword(),
			customer.getStatus(), customer.getCreateTime(), authorities);
				}
		return null;

	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Integer getId() {
		return id;
	}


	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}

	public Integer getStatus() {
		return status;
	}


	public Timestamp getCreateTime() {
		return createTime;
	}


}

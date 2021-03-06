package com.chunkymonkey.itb.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Document(collection = "users")
public class ItbUser implements UserDetails {

	private static final long serialVersionUID = 3268483053777667114L;

	@Id 
	@NotNull
	private String username;
	
	@NotNull
	private String password;
	
	@Indexed
	@Email(message = "Email is not valid")
	private String email;
	
	@NotNull
	private Collection<? extends GrantedAuthority> authorities;

	public ItbUser() {}
	
	public ItbUser(String username, String password, String email, List<String> authorities) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.authorities = authorities == null ? 
				new ArrayList<>() : authorities.stream()
					.map(s -> new SimpleGrantedAuthority(s))
					.collect(Collectors.toList());
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ItbUser))
			return false;
		ItbUser other = (ItbUser) obj;
		if (getUsername() == null) {
			if (other.getUsername() != null)
				return false;
		} else if (!getUsername().equals(other.getUsername()))
			return false;
		return true;
	}
}

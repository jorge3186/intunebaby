package com.chunkymonkey.itb.graphql.domain;

import java.time.LocalDateTime;

import com.chunkymonkey.itb.domain.BabyEntity;
import com.chunkymonkey.itb.domain.Gender;

public class BabyInput {

	private String accountName;
	
	private String birthday;
	
	private String firstName;
	
	private String middleName;
	
	private String lastName;
	
	private String gender;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public BabyEntity toBaby() {
		BabyEntity b = new BabyEntity();
		b.setAccountName(getAccountName());
		b.setFirstName(getFirstName());
		b.setMiddleName(getMiddleName());
		b.setBirthday(getBirthday() == null ? null : LocalDateTime.parse(getBirthday()));
		b.setLastName(getLastName());
		b.setGender(Gender.forValue(getGender()));
		return b;
	}
	
}

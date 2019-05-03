package com.chunkymonkey.itb.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {

	MALE("M"),
	
	FEMALE("F");
	
	private String gender;
	
	private Gender(String gender) {
		this.gender = gender;
	}
	
	@JsonValue
	@Override
	public String toString() {
		return this.gender;
	}
	
	@JsonCreator
	public static Gender forValue(String value) {
		if (value == null)
			return null;
		if ("m".equals(value.toLowerCase()))
			return MALE;
		else
			return FEMALE;
	}
	
}

package com.chunkymonkey.itb.domain;

import java.math.BigInteger;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "db_sequences")
public class Sequences {
	
	@Id
	private String sequenceName;
	
	private BigInteger nextval;

	public String getSequenceName() {
		return sequenceName;
	}

	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}

	public BigInteger getNextval() {
		return nextval;
	}

	public void setNextval(BigInteger nextval) {
		this.nextval = nextval;
	}

}

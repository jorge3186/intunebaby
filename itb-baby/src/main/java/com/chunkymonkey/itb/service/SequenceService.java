package com.chunkymonkey.itb.service;

import java.math.BigInteger;

public interface SequenceService {

	BigInteger nextSequenceValue(String sequenceName);
	
}

package com.chunkymonkey.itb.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

import com.chunkymonkey.itb.domain.BabyEntity;
import com.chunkymonkey.itb.service.SequenceService;

@Configuration
public class BabyEventListener extends AbstractMongoEventListener<BabyEntity> {

	private final SequenceService sequences;
	
	@Autowired
	public BabyEventListener(SequenceService seqs) {
		super();
		this.sequences = seqs;
	}
	
	@Override
	public void onBeforeConvert(BeforeConvertEvent<BabyEntity> event) {
		if (event.getSource().getId() == null)
			event.getSource().setId(sequences.nextSequenceValue("babies_seq"));
	}
	
}

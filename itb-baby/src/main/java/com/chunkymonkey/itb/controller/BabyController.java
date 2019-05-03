package com.chunkymonkey.itb.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chunkymonkey.itb.dto.Baby;
import com.chunkymonkey.itb.service.BabyService;
import com.chunkymonkey.itb.service.MapperService;

@RestController
@RequestMapping("/v1")
public class BabyController {
	
	private final BabyService babies;
	
	private final MapperService mapper;
	
	@Autowired
	public BabyController(BabyService babies, MapperService mapper) {
		this.babies = babies;
		this.mapper = mapper;
	}

	@RequestMapping("/{username}")
	public ResponseEntity<List<Baby>> babiesForUser(@PathVariable("username") String username) {
		return ResponseEntity.ok(
				babies.getBabiesForUser(username)
					.stream()
					.map(b -> mapper.map(b, Baby.class))
					.collect(Collectors.toList())
		);
	}
}

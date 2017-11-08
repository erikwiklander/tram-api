package io.wiklandia.tramapi.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeadersController {

	@GetMapping(value = "headers", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> headers(@RequestHeader HttpHeaders headers) {
		return ResponseEntity.ok(headers);
	}

}

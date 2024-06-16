package ru.daremor.integration.vm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EntityNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private final String message;
}

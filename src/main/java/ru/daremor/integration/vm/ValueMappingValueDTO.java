package ru.daremor.integration.vm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ValueMappingValueDTO {
	
    private String sourceAgency;
    private String sourceIdentifier;
    private String sourceValue;
    private String targetAgency;
    private String targetIdentifier;
    private String targetValue;
    
}
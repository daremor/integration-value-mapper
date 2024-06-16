package ru.daremor.integration.vm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class ValueMappingValue implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
    private String agency;
    private String identifier;
    private String vmValue;
    
    @ManyToOne
    @JoinColumn(name = "value_mapping_group_id")
    @JsonBackReference
    private ValueMappingGroup valueMappingGroup;
    
}
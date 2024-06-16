package ru.daremor.integration.vm;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValueMappingService {

    private final ValueMappingGroupRepository valueMappingGroupRepository;
    private final ValueMappingValueRepository valueMappingValueRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "valueMappingGroupCache", key = "#sourceAgency + #sourceIdentifier + #value")
    public ValueMappingGroup getGroupByValueMapping(String sourceAgency, String sourceIdentifier, String value) {
    	
        List<ValueMappingGroup> groupList = valueMappingGroupRepository.findByValueMapping(sourceAgency, sourceIdentifier, value);
        
        if (groupList.size() == 0) {
        	throw new EntityNotFoundException("Group not found");
        }
        
        if (groupList.size() == 1) {
            Hibernate.initialize(groupList.getFirst().getValueMappingValueList());
            return groupList.getFirst();
        }
        
        if (groupList.size() > 1) {
        	ValueMappingGroup vmGroup = groupList.stream()
        			.filter(ValueMappingGroup::getIsDefault)
        			.findFirst()
        			.orElseThrow(() -> new RuntimeException("Default ValueMappingGroup not found. Found groups: "));
        	Hibernate.initialize(vmGroup.getValueMappingValueList());
            return vmGroup;
        }
        
        throw new RuntimeException("Возникло недопустимое событие!");
        
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "valueMappingValueCache", key = "#sourceAgency + #sourceIdentifier + #value + #targetAgency + #targetIdentifier")
    public ValueMappingValue getValueMappingValue(String sourceAgency, String sourceIdentifier, String value, String targetAgency, String targetIdentifier) {
    	
    	List<ValueMappingValue> valueList = valueMappingValueRepository.findByAgencyIdentifierValueAndGroup(sourceAgency, sourceIdentifier, value, targetAgency, targetIdentifier);
    	
        if (valueList.size() == 0) {
        	throw new EntityNotFoundException("Value not found");
        }
        
        if (valueList.size() == 1) {
        	return valueList.getFirst();
        }
        
        if (valueList.size() > 1) {
        	ValueMappingValue vmValue = valueList.stream()
        			.filter(v -> v.getValueMappingGroup() != null && Boolean.TRUE.equals(v.getValueMappingGroup().getIsDefault()))
        			.findFirst()
        			.orElseThrow(() -> new RuntimeException("Default ValueMappingValue not found. Found groups: "));
            return vmValue;
        }
        
        throw new RuntimeException("Возникло недопустимое событие!");
    }
    
    @Transactional
    public void addValueMappingGroup(ValueMappingValueDTO valueMappingValueDTOs) {
        
    	ValueMappingGroup vmGroup = new ValueMappingGroup();
    	
    	// Проверю есть ли какое-либо значение уже в базе
    	boolean isDuplicateValue = valueMappingGroupRepository
        		.existsBySourceOrTarget(
        				valueMappingValueDTOs.getSourceAgency(), 
        				valueMappingValueDTOs.getSourceIdentifier(),
        				valueMappingValueDTOs.getSourceValue(), 
        				valueMappingValueDTOs.getTargetAgency(),
        				valueMappingValueDTOs.getTargetIdentifier(),
        				valueMappingValueDTOs.getTargetValue()
        		);
    	
    	if (isDuplicateValue) {
    		
    		log.info("Дубликат какой-то из записей найден! Делаю дополнительную выборку false!");
    		vmGroup.setIsDefault(false);

    		// Проверю есть ли именно такая группа уже в базе
            boolean isDuplicateGroup = valueMappingGroupRepository
            		.existsBySourceAndTarget(
            				valueMappingValueDTOs.getSourceAgency(), 
            				valueMappingValueDTOs.getSourceIdentifier(),
            				valueMappingValueDTOs.getSourceValue(), 
            				valueMappingValueDTOs.getTargetAgency(),
            				valueMappingValueDTOs.getTargetIdentifier(),
            				valueMappingValueDTOs.getTargetValue()
            		);
            
            if (isDuplicateGroup) {
            	log.info("Запись уже есть в БД! Терминирую...");
            	return;
            }
            	
    	} else {
    		log.info("Дубликаты записей не найдены! Ставлю isDefault на true!");
    		vmGroup.setIsDefault(true);
    	}
    	
        valueMappingGroupRepository.save(vmGroup);
        
        ValueMappingValue sourceValue = new ValueMappingValue();
        sourceValue.setAgency(valueMappingValueDTOs.getSourceAgency());
        sourceValue.setIdentifier(valueMappingValueDTOs.getSourceIdentifier());
        sourceValue.setVmValue(valueMappingValueDTOs.getSourceValue());
        sourceValue.setValueMappingGroup(vmGroup);
        
        valueMappingValueRepository.save(sourceValue);
        
        ValueMappingValue targetValue = new ValueMappingValue();
        targetValue.setAgency(valueMappingValueDTOs.getTargetAgency());
        targetValue.setIdentifier(valueMappingValueDTOs.getTargetIdentifier());
        targetValue.setVmValue(valueMappingValueDTOs.getTargetValue());
        targetValue.setValueMappingGroup(vmGroup);
        
        valueMappingValueRepository.save(targetValue);
        
    }
}

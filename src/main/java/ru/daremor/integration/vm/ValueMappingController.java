package ru.daremor.integration.vm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/valuemapping")
public class ValueMappingController {

    @Autowired
    private ValueMappingService valueMappingService;

    @GetMapping("/group")
    public ValueMappingGroup getGroupByValueMapping(@RequestParam String agency, @RequestParam String identifier, @RequestParam String value) {
        return valueMappingService.getGroupByValueMapping(agency, identifier, value);
    }

    @GetMapping("/value")
    public ValueMappingValue getValueMappingValue(@RequestParam String sourceAgency, @RequestParam String sourceIdentifier, @RequestParam String sourceValue, @RequestParam String targetAgency, @RequestParam String targetIdentifier) {
        return valueMappingService.getValueMappingValue(sourceAgency, sourceIdentifier, sourceValue, targetAgency, targetIdentifier);
    }
    
    @PostMapping("/value")
    public void addValueMappingValues(@RequestBody ValueMappingValueDTO valueMappingValueDTOs) {
        valueMappingService.addValueMappingGroup(valueMappingValueDTOs);
    }
}
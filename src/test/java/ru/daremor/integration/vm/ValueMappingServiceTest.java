package ru.daremor.integration.vm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

@ExtendWith(MockitoExtension.class)
public class ValueMappingServiceTest {

    @Mock
    private ValueMappingGroupRepository valueMappingGroupRepository;

    @Mock
    private ValueMappingValueRepository valueMappingValueRepository;

    @InjectMocks
    private ValueMappingService valueMappingService;

    private CacheManager cacheManager;

    @BeforeEach
    public void setup() {
        cacheManager = new ConcurrentMapCacheManager("valueMappingGroupCache", "valueMappingValueCache");
    }

    @Test
    public void testGetGroupByValueMappingGroupNotFound() {
        when(valueMappingGroupRepository.findByValueMapping(any(), any(), any())).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () ->
            valueMappingService.getGroupByValueMapping("agency1", "identifier1", "value1")
        );
    }

    @Test
    public void testGetGroupByValueMappingGroupFound() {
        ValueMappingGroup group = new ValueMappingGroup();
        when(valueMappingGroupRepository.findByValueMapping(any(), any(), any())).thenReturn(List.of(group));

        ValueMappingGroup result = valueMappingService.getGroupByValueMapping("agency1", "identifier1", "value1");

        assertThat(result).isNotNull();
        verify(valueMappingGroupRepository).findByValueMapping(any(), any(), any());
        verifyNoMoreInteractions(valueMappingGroupRepository);
    }

    @Test
    public void testGetGroupByValueMappingMultipleGroupsFound() {
        ValueMappingGroup defaultGroup = new ValueMappingGroup();
        defaultGroup.setIsDefault(true);

        ValueMappingGroup nonDefaultGroup = new ValueMappingGroup();
        nonDefaultGroup.setIsDefault(false);

        when(valueMappingGroupRepository.findByValueMapping(any(), any(), any())).thenReturn(List.of(nonDefaultGroup, defaultGroup));

        ValueMappingGroup result = valueMappingService.getGroupByValueMapping("agency1", "identifier1", "value1");

        assertThat(result).isNotNull();
        verify(valueMappingGroupRepository).findByValueMapping(any(), any(), any());
        verifyNoMoreInteractions(valueMappingGroupRepository);
    }

    @Test
    public void testGetValueMappingValueValueNotFound() {
        when(valueMappingValueRepository.findByAgencyIdentifierValueAndGroup(any(), any(), any(), any(), any())).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () ->
            valueMappingService.getValueMappingValue("agency1", "identifier1", "value1", "targetAgency1", "targetIdentifier1")
        );
    }

    @Test
    public void testGetValueMappingValueValueFound() {
        ValueMappingValue value = new ValueMappingValue();
        value.setVmValue("Test Value");

        when(valueMappingValueRepository.findByAgencyIdentifierValueAndGroup(any(), any(), any(), any(), any())).thenReturn(List.of(value));
        
        ValueMappingValue result = valueMappingService.getValueMappingValue("agency1", "identifier1", "value1", "targetAgency1", "targetIdentifier1");

        assertThat(result).isNotNull();
        assertThat(result.getVmValue()).isEqualTo("Test Value");
        verify(valueMappingValueRepository).findByAgencyIdentifierValueAndGroup(any(), any(), any(), any(), any());
        verifyNoMoreInteractions(valueMappingValueRepository);
    }

    @Test
    public void testGetValueMappingValueMultipleValuesFound() {
        ValueMappingGroup defaultGroup = new ValueMappingGroup();
        defaultGroup.setIsDefault(true);

        ValueMappingValue defaultValue = new ValueMappingValue();
        defaultValue.setVmValue("Default Value");
        defaultValue.setValueMappingGroup(defaultGroup);

        ValueMappingValue nonDefaultValue = new ValueMappingValue();
        nonDefaultValue.setVmValue("Non-default Value");

        when(valueMappingValueRepository.findByAgencyIdentifierValueAndGroup(any(), any(), any(), any(), any())).thenReturn(List.of(nonDefaultValue, defaultValue));

        ValueMappingValue result = valueMappingService.getValueMappingValue("agency1", "identifier1", "value1", "targetAgency1", "targetIdentifier1");

        assertThat(result).isNotNull();
        assertThat(result.getVmValue()).isEqualTo("Default Value");
        verify(valueMappingValueRepository).findByAgencyIdentifierValueAndGroup(any(), any(), any(), any(), any());
        verifyNoMoreInteractions(valueMappingValueRepository);
    }
}
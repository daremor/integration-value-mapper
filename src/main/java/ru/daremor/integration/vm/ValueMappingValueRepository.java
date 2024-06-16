package ru.daremor.integration.vm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ValueMappingValueRepository extends JpaRepository<ValueMappingValue, Long> {

    @Query("SELECT v2 FROM ValueMappingValue v1 " +
           "JOIN v1.valueMappingGroup g " +
           "JOIN g.valueMappingValueList v2 " +
           "WHERE v1.agency = :sourceAgency AND v1.identifier = :sourceIdentifier AND v1.vmValue = :value " +
           "AND v2.agency = :targetAgency AND v2.identifier = :targetIdentifier")
    List<ValueMappingValue> findByAgencyIdentifierValueAndGroup(
    		@Param("sourceAgency") String sourceAgency, 
    		@Param("sourceIdentifier") String sourceIdentifier, 
    		@Param("value") String value,
    		@Param("targetAgency") String targetAgency, 
    		@Param("targetIdentifier") String targetIdentifier
    );
    
}
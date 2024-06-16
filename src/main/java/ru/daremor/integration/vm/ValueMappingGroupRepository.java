package ru.daremor.integration.vm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ValueMappingGroupRepository extends JpaRepository<ValueMappingGroup, Long> {

    @Query("SELECT g FROM ValueMappingGroup g JOIN g.valueMappingValueList v " +
           "WHERE v.agency = :agency AND v.identifier = :identifier AND v.vmValue = :value")
    List<ValueMappingGroup> findByValueMapping(@Param("agency") String agency, @Param("identifier") String identifier, @Param("value") String value);
    
    @Query("SELECT CASE WHEN COUNT(v1) > 0 THEN TRUE ELSE FALSE END FROM ValueMappingValue v1 " +
            "JOIN v1.valueMappingGroup g " +
            "JOIN g.valueMappingValueList v2 " +
            "WHERE (v1.agency = :sourceAgency AND v1.identifier = :sourceIdentifier AND v1.vmValue = :sourceValue) " +
            "AND (v2.agency = :targetAgency AND v2.identifier = :targetIdentifier AND v2.vmValue = :targetValue)")
    boolean existsBySourceAndTarget(
    		@Param("sourceAgency") String sourceAgency, 
    		@Param("sourceIdentifier") String sourceIdentifier, 
    		@Param("sourceValue") String sourceValue,                         
    		@Param("targetAgency") String targetAgency, 
    		@Param("targetIdentifier") String targetIdentifier,
    		@Param("targetValue") String targetValue
    );
    
    @Query("SELECT CASE WHEN COUNT(v1) > 0 THEN TRUE ELSE FALSE END FROM ValueMappingValue v1 " +
            "JOIN v1.valueMappingGroup g " +
            "JOIN g.valueMappingValueList v2 " +
            "WHERE (v1.agency = :sourceAgency AND v1.identifier = :sourceIdentifier AND v1.vmValue = :sourceValue) " +
            "OR (v2.agency = :targetAgency AND v2.identifier = :targetIdentifier AND v2.vmValue = :targetValue)" +
            "AND g.isDefault = true")
    boolean existsBySourceOrTarget(
    		@Param("sourceAgency") String sourceAgency, 
    		@Param("sourceIdentifier") String sourceIdentifier, 
    		@Param("sourceValue") String sourceValue,                         
    		@Param("targetAgency") String targetAgency, 
    		@Param("targetIdentifier") String targetIdentifier,
    		@Param("targetValue") String targetValue
    );
    
}

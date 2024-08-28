/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2020
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.edca.catalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ericsson.oss.edca.catalog.domain.DataCategory;
import com.ericsson.oss.edca.catalog.domain.DataProviderType;

@Repository
public interface DataProviderTypeRepository extends CrudRepository<DataProviderType, Integer>, JpaSpecificationExecutor<DataProviderType> {

    @Query(value = "SELECT dProvType from DataProviderType dProvType where dProvType.dataSpace.name = :dataSpaceName")
    public List<DataProviderType> findByDataspaceName(@Param(value = "dataSpaceName") final String dataspaceName);

    @Query(value = "SELECT dProvType from DataProviderType dProvType where dProvType.dataCategory = :dataCategory")
    public List<DataProviderType> findByDataCategory(@Param(value = "dataCategory") final DataCategory dataCategory);

    @Query(value = "SELECT dProvType from DataProviderType dProvType where dProvType.dataCategory = :dataCategory AND dProvType.dataSpace.name = :dataSpaceName")
    public List<DataProviderType> findByDataSpaceNameAndDataCategory(@Param(value = "dataCategory") final DataCategory dataCategory, @Param(value = "dataSpaceName") final String dataSpaceName);

    @Query(value = "SELECT dProvType from DataProviderType dProvType where dProvType.providerTypeId = :providerTypeId")
    public List<DataProviderType> findByProviderTypeId(@Param(value = "providerTypeId") final String providerTypeId);

    @Query(value = "SELECT dProvType from DataProviderType dProvType where dProvType.providerVersion = :providerVersion")
    public List<DataProviderType> findByProviderVersion(@Param(value = "providerVersion") final String providerVersion);

    @Query(value = "SELECT dProvType from DataProviderType dProvType where dProvType.providerTypeId = :providerTypeId AND dProvType.providerVersion = :providerVersion")
    public List<DataProviderType> findByProviderTypeIdAndProviderVersion(@Param(value = "providerTypeId") final String providerTypeId, @Param(value = "providerVersion") final String providerVersion);

    @Query(value = "SELECT dProvType from DataProviderType dProvType where dProvType.providerTypeId = :providerTypeId AND dProvType.dataCategory = :dataCategory")
    public List<DataProviderType> findByProviderTypeIdAndDataCategory(@Param(value = "providerTypeId") final String providerTypeId, @Param(value = "dataCategory") final DataCategory dataCategory);

    @Query(value = "SELECT dProvType from DataProviderType dProvType where dProvType.providerVersion = :providerVersion AND dProvType.dataCategory = :dataCategory")
    public List<DataProviderType> findByProviderVersionAndDataCategory(@Param(value = "providerVersion") final String providerVersion, @Param(value = "dataCategory") final DataCategory dataCategory);

    @Query(value = "SELECT dProvType from DataProviderType dProvType where dProvType.providerTypeId = :providerTypeId AND dProvType.providerVersion = :providerVersion AND dProvType.dataCategory = :dataCategory")
    public List<DataProviderType> findByProviderTypeIdAndProviderVersionAndDataCategory(@Param(value = "providerTypeId") final String providerTypeId,
            @Param(value = "providerVersion") final String providerVersion, @Param(value = "dataCategory") final DataCategory dataCategory);

}

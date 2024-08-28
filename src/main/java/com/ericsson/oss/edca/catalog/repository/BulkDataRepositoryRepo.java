/*******************************************************************************

* COPYRIGHT Ericsson 2020
*
* The copyright to the computer program(s) herein is the property of
*
* Ericsson Inc. The programs may be used and/or copied only with written
*
* permission from Ericsson Inc. or in accordance with the terms and
*
* conditions stipulated in the agreement/contract under which the
*
* program(s) have been supplied.
* 
******************************************************************************/
package com.ericsson.oss.edca.catalog.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ericsson.oss.edca.catalog.domain.BulkDataRepository;

@Repository
public interface BulkDataRepositoryRepo extends CrudRepository<BulkDataRepository, Integer> {

    List<BulkDataRepository> findByNameSpaceAndName(String nameSpace, String name);

}

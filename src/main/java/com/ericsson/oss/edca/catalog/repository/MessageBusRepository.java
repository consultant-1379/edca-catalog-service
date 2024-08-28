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

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.oss.edca.catalog.domain.MessageBus;

@Repository
public interface MessageBusRepository extends CrudRepository<MessageBus, Integer> {

    @Query("SELECT messagebus FROM MessageBus messagebus where messagebus.name=:name AND messagebus.nameSpace = :namespace")
    @Transactional(readOnly = true)
    List<MessageBus> findMessageBusByNameAndNamespace(@Param("name") String name, @Param("namespace") String nameSpace);

}

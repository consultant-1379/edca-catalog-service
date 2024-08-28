/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2021
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.edca.catalog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ericsson.oss.edca.catalog.domain.MessageStatusTopic;

@Repository
public interface MessageStatusTopicRepository extends CrudRepository<MessageStatusTopic, Integer> {

    @Query(value = "SELECT * FROM message_status_topic WHERE name=?1 AND message_bus_id=?2", nativeQuery = true)
    public Optional<MessageStatusTopic> findByNameAndMessageBusId(String name, int messageBusId);

}

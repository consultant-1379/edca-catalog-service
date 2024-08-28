package com.ericsson.oss.edca.catalog.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ericsson.oss.edca.catalog.domain.NotificationTopic;


@Repository
public interface NotificationTopicRepository extends CrudRepository<NotificationTopic, Integer> {

    @Query(value="SELECT * FROM notification_topic WHERE name=?1 AND message_bus_id=?2", nativeQuery=true)
    public Optional<NotificationTopic> findByNameAndMessageBusId(String name, int messageBusId );

}
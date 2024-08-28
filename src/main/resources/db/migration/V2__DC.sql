-- -----------------------------------------------------
-- Table `message_status_topic`
-- -----------------------------------------------------
CREATE TABLE message_status_topic (
    id SERIAL PRIMARY KEY,
    version INT,
    name VARCHAR NOT NULL,
    specification_reference VARCHAR,
    encoding INT,
    message_bus_id INT,
    CONSTRAINT status_message_bus_fk FOREIGN KEY (message_bus_id) REFERENCES message_bus(id),
    CONSTRAINT uniqueMessageStatusTopicPerService UNIQUE (message_bus_id , name)
);

-- -----------------------------------------------------
-- Table `message_data_topic`
-- -----------------------------------------------------
CREATE TABLE message_data_topic (
    id SERIAL PRIMARY KEY,
    version INT,
    name VARCHAR NOT NULL,
    encoding INT,
    message_bus_id INT,
    message_status_topic_id INT,
    CONSTRAINT data_message_bus_fk FOREIGN KEY (message_bus_id) REFERENCES message_bus(id),
    CONSTRAINT status_message_topic_fk FOREIGN KEY (message_status_topic_id) REFERENCES message_status_topic(id),
    CONSTRAINT uniqueMessageDataTopicPerService UNIQUE (message_bus_id , name)
);

-- -----------------------------------------------------
-- Table `message_schema`
-- -----------------------------------------------------
CREATE TABLE message_schema (
    id SERIAL PRIMARY KEY,
    version INT,
    data_collector_id INT,
    data_provider_type_id INT,
    data_topic_id INT,
    specification_reference VARCHAR,
    CONSTRAINT unique_message_schema_per_message_provider UNIQUE (data_provider_type_id),
    CONSTRAINT message_schema_data_provider_type_fk FOREIGN KEY (data_provider_type_id) REFERENCES data_provider_type(id),
    CONSTRAINT message_schema_data_collector_fk FOREIGN KEY (data_collector_id) REFERENCES data_collector(id),
    CONSTRAINT data_topic_fk FOREIGN KEY (data_topic_id) REFERENCES message_data_topic(id)
);


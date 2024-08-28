-- ----------------------------
-- Enum `data_category`
-- ----------------------------

CREATE TYPE data_category_type as ENUM('CM_EXPORTS' , 'CM_NOTIFICATIONS' , 'FM_NOTIFICATIONS' , 'PM_EVENTS' , 'PM_STATS');

-- ----------------------------
-- Enum `data_encoding`
-- ----------------------------

CREATE TYPE data_encoding_type as ENUM('CSV' , 'JSON' , 'PARQUET' , 'XML');

-- -----------------------------------------------------
-- Enum `notification_encoding_type`
-- -----------------------------------------------------
CREATE TYPE notification_encoding_type as ENUM('JSON');

-- -----------------------------------------------------
-- Table `data_space`
-- -----------------------------------------------------
CREATE TABLE data_space (
    id SERIAL PRIMARY KEY,
    name VARCHAR UNIQUE,
    version INT
);

-- -----------------------------------------------------
-- Table `data_provider_type`
-- -----------------------------------------------------

CREATE TABLE data_provider_type (
    id SERIAL,
    version INT,
    provider_type_id VARCHAR NOT NULL,
    provider_version VARCHAR,
    data_category INT NOT NULL,
    PRIMARY KEY(id),
    data_space_id INT,
    CONSTRAINT data_space_fk FOREIGN KEY (data_space_id) REFERENCES data_space(id),
    CONSTRAINT unique_data_provider_type UNIQUE (data_space_id , provider_type_id , provider_version)
);

-- -----------------------------------------------------
-- Table `bulk_data_repository`
-- TBD : access_endpoints set of string field inclusion
-- -----------------------------------------------------

CREATE TABLE bulk_data_repository (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    cluster_name VARCHAR NOT NULL,
    namespace VARCHAR,
    CONSTRAINT BDR_UNIQUE_IDENTITY UNIQUE (name , cluster_name , namespace)
);

-- ---------------------------------------------------
-- CollectionTable `bulk_data_repository_endpoint_ids` for set of access_endpoints of bulk_data_repository
-- ---------------------------------------------------
CREATE TABLE bulk_data_repository_endpoint_ids (
    id INT NOT NULL, 
    access_endpoints VARCHAR,
    CONSTRAINT bdr_endpoint_ids_fk FOREIGN KEY(id) REFERENCES bulk_data_repository(id)
);


-- -----------------------------------------------------
-- Table `message_bus`
-- -----------------------------------------------------

CREATE TABLE message_bus (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    cluster_name VARCHAR NOT NULL,
    namespace VARCHAR,
    CONSTRAINT MESSAGE_BUS_UNIQUE_IDENTITY UNIQUE (name , cluster_name , namespace)
);

-- ---------------------------------------------------
-- CollectionTable `message_bus_endpoint_ids` for set of access_endpoints of message_bus
-- ---------------------------------------------------
CREATE TABLE message_bus_endpoint_ids (
    id INT NOT NULL, 
    access_endpoints VARCHAR,
    CONSTRAINT message_bus_endpoint_ids_fk FOREIGN KEY(id) REFERENCES message_bus(id)
);


-- -----------------------------------------------------
-- Table `notification_topic`
-- -----------------------------------------------------

CREATE TABLE notification_topic (
    id SERIAL PRIMARY KEY,
    version INT,
    name VARCHAR NOT NULL,
    specification_reference VARCHAR,
    encoding INT,
    message_bus_id INT,
    CONSTRAINT message_bus_fk FOREIGN KEY (message_bus_id) REFERENCES message_bus(id),
    CONSTRAINT UNIQUE_TOPIC_PER_SERVICE UNIQUE (message_bus_id , name)
);

-- -----------------------------------------------------
-- Table `data_collector`
-- -----------------------------------------------------

CREATE TABLE data_collector (
    id SERIAL,
    name VARCHAR NOT NULL,
    version INT,
    collector_id VARCHAR UNIQUE NOT NULL,
    PRIMARY KEY (id),
    control_endpoint VARCHAR NOT NULL
);

-- -----------------------------------------------------
-- Table `file_format`
-- -----------------------------------------------------

CREATE TABLE file_format (
    id SERIAL PRIMARY KEY,
    version INT,
    specification_reference VARCHAR,
    data_encoding INT,
    data_provider_type_id INT,
    data_collector_id INT,
    notification_topic_id INT,
    bulk_data_repository_id INT,
    CONSTRAINT UNIQUE_FILE_FORMAT_PER_PROVIDER UNIQUE (data_encoding  , data_provider_type_id),
    CONSTRAINT data_provider_type_fk FOREIGN KEY (data_provider_type_id) REFERENCES data_provider_type(id),
    CONSTRAINT data_collector_fk FOREIGN KEY (data_collector_id) REFERENCES data_collector(id),
    CONSTRAINT notification_topic_fk FOREIGN KEY (notification_topic_id) REFERENCES notification_topic(id),
    CONSTRAINT bulk_data_repository_fk FOREIGN KEY (bulk_data_repository_id) REFERENCES bulk_data_repository(id)
);

-- ---------------------------------------------------
-- CollectionTable `message_bus_endpoint_ids` for set of report_output_periods of file_format
-- ---------------------------------------------------
CREATE TABLE report_output_periods (
    id INT NOT NULL,
    report_output_period_set INT,
    CONSTRAINT report_output_periods_fk FOREIGN KEY (id) REFERENCES file_format(id)
);
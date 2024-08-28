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
package com.ericsson.oss.edca.catalog.common;

public class CatalogConstants {
    private CatalogConstants() {

    }

    public static final String POST_CREATED_RESPONSE_MESSAGE = "Metadata created successfully";
    public static final String POST_CONFLICT_RESPONSE_MESSAGE = "Metadata exists already";
    public static final String PATCH_UPDATED_RESPONSE_MESSAGE = "Metadata updated Successfully";
    public static final String OPERATION_BAD_REQUEST_MESSAGE = "Only add & remove operations are supported";
    public static final String UPDATE_FIELD_BAD_REQUEST_MESSAGE = "Only dataSubCategory & sourceType values can be updated";
    public static final String PATCH_UPDATIONVAL_BAD_REQUEST_MESSAGE = "Invalid values found in the update request body";
    public static final String FILE_NOT_FOUND_MESSAGE = "Metadata not found for the given topicName ";
    public static final String FILE_UPLOAD_DIR = "catalog_upload";
    public static final String FILE_FORMAT = ".json";
    public static final String COMMON_PATTERN_MATCHER_EXPRESSION = "__(.*)";
    public static final String FILENAME_SEPARATOR = "__";
    public static final String INVALID_TOPICNAME = "Invalid topicName";
    public static final String MINIO_ESCAPE_SEQUENCE_CHAR = "[\\+]";
    public static final String DEL_SUCCESS_MESSAGE = "Metadata deleted successfully for the topicName ";
    public static final String BAD_REQUEST_HTTP_MESSAGE = "Invalid request values";
    public static final String COMMON_NOT_FOUND_MSG = "Requested Resource not found";
    public static final String COMMON_CONFLICT_MSG = " Resource exists already";
    public static final String BDR_QPARAMS_ERROR_MSG = "Only parameters(name & nameSpace) can be used for filtering bulk-data-repository details";
    public static final String MSGBUS_QPARAMS_ERROR_MSG = "Only parameters(name & nameSpace) can be used for filtering Message Bus details";
    public static final String NT_SUCCESS_MESSSAGE = "Notification Topic has been created successfully";
    public static final String NT_QPARAMS_ERROR_MSG = "Only name and messageBusId can be used for filtering";
    public static final String DATASPACE_QPARAMS_ERROR_MSG = "Parameter values(name) missing in request";
    public static final String DATAPROVIDERTYPE_QPARAMS_ERROR_MSG = "Parameter values(dataSpace) missing in request";
    public static final String DATAPROVIDERTYPE_DATASPACE_NOT_FOUND_MSG = "dataSpace not found for dataProviderType";
    public static final String FILEFORMAT_DATAPROVIDERTYPE_NOT_FOUND_MSG = "dataProviderType not found for fileFormat";
    public static final String FILEFORMAT_BULK_DATA_REPO_NOT_FOUND_MSG = "bulkDataRepository not found for fileFormat";
    public static final String FILEFORMAT_NOTIFICATION_TOPIC_NOT_FOUND_MSG = "notificationTopic not found for fileFormat";
    public static final String FILEFORMAT_DATACOLLECTOR_NOT_FOUND_MSG = "dataCollector not found for fileFormat";
    public static final String FILEFORMAT_QPARAMS_ERROR_MSG = "Parameters(dataProviderType, dataSpace, dataCategory) missing in request or invalid query params";
    public static final String MESSAGESTATUSTOPIC_MESSAGEBUS_NOT_FOUND_MSG = "messageBus not found for messageStatusTopic";
    public static final String MESSAGEDATATOPIC_MESSAGEBUS_NOT_FOUND_MSG = "messageBus not found for messageDataTopic";
    public static final String MESSAGEDATATOPIC_MESSAGESTATUSTOPIC_NOT_FOUND_MSG = "messageStatusTopic not found for messageDataTopic";
    public static final String MESSAGESCHEMA_DATAPROVIDERTYPE_NOT_FOUND_MSG = "dataProviderType not found for messageSchema";
    public static final String MESSAGESCHEMA_DATACOLLECTOR_NOT_FOUND_MSG = "dataCollector not found for messageSchema";
    public static final String MESSAGESCHEMA_MESSAGEDATATOPIC_NOT_FOUND = "messageDataTopic not found for messageSchema";
    public static final String MESSAGESCHEMA_QPARAMS_ERROR_MSG = "Parameters(dataProviderType, dataSpace, dataCategory, topic) missing in request or invalid query params";

}

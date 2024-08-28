export CATALOG_URL=http://eric-edca-catalog.sunshine-harii:9590
export BDR_ENDPOINT="/catalog/v1/bulk-data-repository"
export DATAPROVIDER_ENDPOINT="/catalog/v1/data-provider-type"
export DATASPACE_ENDPOINT="/catalog/v1/data-space"
export FILEFORMAT_ENDPOINT="/catalog/v1/file-format"
export MESSAGEBUS_ENDPOINT="/catalog/v1/message-bus"
export NOTIFICATIONTOPIC_ENDPOINT="/catalog/v1/notification-topic"

export MINIO_SECRETKEY="drguser123"
export MINIO_ACCESSKEY="drguser"

export SFTP_USER="drg"
export SFTP_PASS="drg"

java -Dspring.application.json='{"server.port":8081}' -jar drgSimulatorApp.jar  --a

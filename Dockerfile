#
# COPYRIGHT Ericsson 2020
#
#
#
# The copyright to the computer program(s) herein is the property of
#
# Ericsson Inc. The programs may be used and/or copied only with written
#
# permission from Ericsson Inc. or in accordance with the terms and
#
# conditions stipulated in the agreement/contract under which the
#
# program(s) have been supplied.
#

ARG CBO_IMAGE_URL=armdocker.rnd.ericsson.se/proj-ldc/common_base_os_release/sles

ARG CBO_VERSION=3.30.0-7
FROM ${CBO_IMAGE_URL}:${CBO_VERSION}
ARG CBO_VERSION
ARG CBO_REPO=arm.rnd.ki.sw.ericsson.se/artifactory/proj-ldc-repo-rpm-local/common_base_os/sles/

ARG COMMIT=""
ARG APP_VERSION=""
ARG CURRENT_DATE=""
ARG PRODUCT_REVISION=""
# ARG SERVER_PORT=9590

LABEL \
    com.ericsson.product-number="CXU 101 0395/1"\
    com.ericsson.product-revision=$PRODUCT_REVISION\
    org.opencontainers.image.title="eric-edca-catalog"\
    org.opencontainers.image.created=$CURRENT_DATE\
    org.opencontainers.image.revision=$COMMIT\
    org.opencontainers.image.vendor="Ericsson"\
    org.opencontainers.image.version=$APP_VERSION

ENV APP_DIR /app

WORKDIR ${APP_DIR}

COPY target/catalog-service.jar app.jar
COPY entryPoint.sh entryPoint.sh

RUN zypper addrepo --gpgcheck-strict -f https://${CBO_REPO}${CBO_VERSION} \
    COMMON_BASE_OS_SLES_REPO \
    && zypper --gpg-auto-import-keys refresh \
    && zypper install -l -y shadow file java-11-openjdk-headless \
    && zypper clean --all \
    && groupadd -g 1100 catalog \
    && useradd -u 1100 -G catalog catalog \
    && chmod 700 /app/entryPoint.sh \
    && chown -R catalog:catalog /app/ \
    && zypper rr COMMON_BASE_OS_SLES_REPO

EXPOSE 9590
ENTRYPOINT [ "sh", "entryPoint.sh" ]

USER catalog

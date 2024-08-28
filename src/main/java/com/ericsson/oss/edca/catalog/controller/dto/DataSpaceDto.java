package com.ericsson.oss.edca.catalog.controller.dto;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DataSpaceDto extends NamedDto {
    Set<Integer> DataProviderTypeIds = new HashSet<>();

    /**
     * @return the dataProviderTypeIds
     */
    public Set<Integer> getDataProviderTypeIds() {
        return Collections.unmodifiableSet(DataProviderTypeIds);
    }

    /**
     * @param dataProviderTypeIds
     *            the dataProviderTypeIds to set
     */
    public void setDataProviderTypeIds(final Set<Integer> dataProviderTypeIds) {
        DataProviderTypeIds = new HashSet<>(dataProviderTypeIds);
    }

}
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
package com.ericsson.oss.edca.catalog.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Umbrella for a set of related data sources (e.g. Radio 4G/5G domain) roughly
 * matching the application managing them. For the it only has a name, when we
 * will handshake with model service we can add more constraints.
 */
@Entity
@Table(name = "data_space", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name", name = "uniqueDataSpaceName") })
public class DataSpace extends NamedEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dataSpace", orphanRemoval = true)
    private Set<DataProviderType> dataProviderTypes = Collections.emptySet();

    public void addDataProviderType(DataProviderType dataProviderType) {
        if (dataProviderType.isNew()) {
            getDataProviderTypesInternal().add(dataProviderType);
        }
        dataProviderType.setDataSpace(this);
    }

    public Set<DataProviderType> getDataProviderTypes() {
        return Collections.unmodifiableSet(dataProviderTypes);
    }

    public void setDataProviderTypes(Set<DataProviderType> dataProviderTypes) {
        this.dataProviderTypes = new HashSet<>(dataProviderTypes);
    }

    private Collection<DataProviderType> getDataProviderTypesInternal() {
        if (dataProviderTypes == null) {
            this.dataProviderTypes = new HashSet<>();
        }
        return Collections.unmodifiableSet(dataProviderTypes);
    }

    public Set<Integer> getDataProviderTypeIds() {
        if (dataProviderTypes == null) {
            return new HashSet<>();
        }
        return getDataProviderTypesInternal().stream().map(DataProviderType::getId).collect(Collectors.toSet());
    }

}

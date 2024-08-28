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

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "bulk_data_repository", uniqueConstraints = { @UniqueConstraint(columnNames = { "cluster_name", "namespace", "name" }, name = "uniqueBulkDataRepository") })
public class BulkDataRepository implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @Column(name = "name")
    private String name;

    @NotEmpty
    @Column(name = "cluster_name")
    private String clusterName;

    @Column(name = "namespace")
    private String nameSpace;

    @NotEmpty
    @ElementCollection
    @CollectionTable(name = "bulk_data_repository_endpoint_ids", joinColumns = @JoinColumn(name = "id", referencedColumnName = "id"))
    @Column(name = "access_endpoints")
    private Set<String> accessEndpoints = Collections.emptySet();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bulkDataRepository", orphanRemoval = true)
    private Set<FileFormat> fileFormats = Collections.emptySet();

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(final String clusterName) {
        this.clusterName = clusterName;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(final String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public Set<String> getAccessEndpoints() {
        return Collections.unmodifiableSet(accessEndpoints);
    }

    public void setAccessEndpoints(final Set<String> accessEndpoints) {
        this.accessEndpoints = new HashSet<>(accessEndpoints);
    }

    public Set<FileFormat> getFileFormat() {
        return Collections.unmodifiableSet(fileFormats);
    }

    public void setFileFormat(final Set<FileFormat> fileFormat) {
        this.fileFormats = new HashSet<>(fileFormat);
    }

    public Set<Integer> getFileFormatIds() {
        if (fileFormats == null) {
            return new HashSet<>();
        }
        return this.fileFormats.stream().map(FileFormat::getId).collect(Collectors.toSet());
    }

    public void addFileFormat(final FileFormat fileFormatForBdr) {
        if (fileFormatForBdr.isNew()) {
            getFileFormatInternal().add(fileFormatForBdr);
        }
        fileFormatForBdr.setBulkDataRepository(this);
    }

    private Collection<FileFormat> getFileFormatInternal() {
        if (fileFormats == null) {
            this.fileFormats = new HashSet<>();
        }
        return Collections.unmodifiableSet(fileFormats);
    }

}

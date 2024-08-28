package com.ericsson.oss.edca.catalog.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.oss.edca.catalog.domain.DataSpace;

@Repository
public interface DataSpaceRepository extends CrudRepository<DataSpace, Integer> {

    @Transactional(readOnly = true)
    List<DataSpace> findDistinctDataSpaceByName(String name);

}

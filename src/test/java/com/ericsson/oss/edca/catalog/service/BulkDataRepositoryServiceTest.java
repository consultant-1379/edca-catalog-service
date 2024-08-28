package com.ericsson.oss.edca.catalog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import com.ericsson.oss.edca.catalog.controller.dto.BulkDataRepositoryDTO;
import com.ericsson.oss.edca.catalog.domain.BulkDataRepository;
import com.ericsson.oss.edca.catalog.repository.BulkDataRepositoryRepo;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(BulkDataRepositoryServiceTest.class)
public class BulkDataRepositoryServiceTest {

    @InjectMocks
    private BulkDataRepositoryService service;

    @Mock
    private BulkDataRepositoryRepo repo;

    private BulkDataRepository bdrModel;

    private BulkDataRepositoryDTO bdrDTO;

    @BeforeEach
    void setupDTO() {
        bdrDTO = new BulkDataRepositoryDTO();
        bdrDTO.setId(101);
        bdrDTO.setName("test");
        bdrDTO.setNameSpace("testSpace");
        bdrDTO.setClusterName("testCluster");
        bdrDTO.setFileFormatIds(Collections.emptySet());
        bdrDTO.setAccessEndpoints(Stream.of("test1", "test2").collect(Collectors.toSet()));
        bdrModel = new BulkDataRepository();
        bdrModel.setId(101);
        bdrModel.setName("test");
        bdrModel.setNameSpace("testSpace");
        bdrModel.setClusterName("testCluster");
        bdrModel.setAccessEndpoints(Stream.of("test1", "test2").collect(Collectors.toSet()));
    }

    @Test
    void saveBDREntryTest() {
        assertThat(bdrDTO).isEqualToComparingFieldByField(service.saveBDR(bdrDTO));
    }

    @Test
    void getBDREntryTest() {
        when(repo.findById(Mockito.any(Integer.class))).thenReturn(Optional.of(bdrModel));
        assertThat(bdrDTO).isEqualToComparingFieldByField(service.getBDRById(bdrDTO.getId()));
    }

    @Test
    void getBDREntryByNamsepaceAndNameTest() {
        when(repo.findByNameSpaceAndName(Mockito.any(String.class), Mockito.any(String.class)))
                .thenReturn(List.of(bdrModel));
        service.getBDRByNameSpaceandName(bdrDTO.getNameSpace(), bdrDTO.getName()).stream()
                .forEach(s -> assertThat(s).isEqualToIgnoringGivenFields(bdrDTO, "id"));

    }

    @Test
    void getAllBDREntriesTest() {
        when(repo.findAll()).thenReturn(Arrays.asList(bdrModel));
        assertThat(service.getAllBDR()).anyMatch(s -> s.getClusterName() == bdrDTO.getClusterName());
    }

}

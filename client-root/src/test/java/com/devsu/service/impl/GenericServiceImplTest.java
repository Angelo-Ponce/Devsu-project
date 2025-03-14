package com.devsu.service.impl;

import com.devsu.exception.ModelNotFoundException;
import com.devsu.repository.IGenericRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenericServiceImplTest {

    private static class TestEntity {
        private Long id;
        private String name;

        public TestEntity(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Mock
    private IGenericRepository<TestEntity, Long> mockRepository;

    private GenericServiceImpl<TestEntity, Long> service;

    private final TestEntity mockEntity = new TestEntity(1L, "Angelo");

    @BeforeEach
    void setUp() {
        service = new GenericServiceImpl<TestEntity, Long>() {
            @Override
            protected IGenericRepository<TestEntity, Long> getRepository() {
                return mockRepository;
            }
        };
    }

    @Test
    void givenFindAll_ThenReturnAllEntities() {
        when(mockRepository.findAll()).thenReturn(java.util.List.of(mockEntity));
        java.util.List<TestEntity> result = service.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockEntity.id, result.getFirst().id);
        assertEquals(mockEntity.name, result.getFirst().name);
        verify(mockRepository, times(1)).findAll();
    }

    @Test
    void givenFindById_WhenEntityExists_ThenReturnEntity() {
        when(mockRepository.findById(mockEntity.id)).thenReturn(Optional.of(mockEntity));
        TestEntity result = service.findById(mockEntity.id);
        assertNotNull(result);
        assertEquals(mockEntity.id, result.id);
        verify(mockRepository, times(1)).findById(anyLong());
    }

    @Test
    void givenFindById_WhenEntityDoesNotExist_ThenThrowModelNotFoundException (){
        Long id = 5L;
        when(mockRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(
                ModelNotFoundException.class,
                () -> service.findById(id),
                "ID not found: " + id
        );
        verify(mockRepository, times(1)).findById(id);
    }

}
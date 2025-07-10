package com.example.test.service;

import com.example.test.dto.DoctorDto;
import com.example.test.model.Doctor;
import com.example.test.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorService doctorService;

    private Doctor testDoctor;
    private DoctorDto testDoctorDto;

    @BeforeEach
    void setUp() {
        testDoctor = new Doctor();
        testDoctor.setId(1);
        testDoctor.setName("Ivan");
        testDoctor.setSurname("Petrov");

        testDoctorDto = new DoctorDto();
        testDoctorDto.setId(1);
        testDoctorDto.setName("Ivan");
        testDoctorDto.setSurname("Petrov");
    }

    @Test
    void save_ShouldSaveDoctor_WhenValidDoctorDtoProvided() {
        when(doctorRepository.save(any(Doctor.class))).thenReturn(testDoctor);

        doctorService.save(testDoctorDto);

        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void save_ShouldConvertDtoToModel_WhenCalled() {
        when(doctorRepository.save(any(Doctor.class))).thenReturn(testDoctor);

        doctorService.save(testDoctorDto);

        verify(doctorRepository).save(argThat(doctor -> 
            doctor.getName().equals(testDoctorDto.getName()) && 
            doctor.getSurname().equals(testDoctorDto.getSurname())
        ));
    }

    @Test
    void get_ShouldReturnDoctorDto_WhenDoctorExists() {
        when(doctorRepository.findById(1)).thenReturn(Optional.of(testDoctor));

        DoctorDto result = doctorService.get(1);

        assertNotNull(result);
        assertEquals(testDoctor.getName(), result.getName());
        assertEquals(testDoctor.getSurname(), result.getSurname());
        verify(doctorRepository, times(1)).findById(1);
    }

    @Test
    void get_ShouldThrowException_WhenDoctorNotFound() {
        when(doctorRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> doctorService.get(1));
        verify(doctorRepository, times(1)).findById(1);
    }

    @Test
    void update_ShouldUpdateExistingDoctor_WhenDoctorExists() {
        DoctorDto updatedDto = new DoctorDto();
        updatedDto.setName("Petr");
        updatedDto.setSurname("Ivanov");

        when(doctorRepository.findById(1)).thenReturn(Optional.of(testDoctor));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(testDoctor);

        doctorService.update(updatedDto, 1);

        verify(doctorRepository, times(1)).findById(1);
        verify(doctorRepository, times(1)).save(testDoctor);
        assertEquals("Petr", testDoctor.getName());
        assertEquals("Ivanov", testDoctor.getSurname());
    }

    @Test
    void update_ShouldThrowException_WhenDoctorNotFound() {
        when(doctorRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> doctorService.update(testDoctorDto, 1));
        verify(doctorRepository, times(1)).findById(1);
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    void delete_ShouldCallRepositoryDelete_WhenCalled() {
        doctorService.delete(1);

        verify(doctorRepository, times(1)).deleteById(1);
    }

    @Test
    void delete_ShouldHandleNonExistentId_WhenCalled() {
        doNothing().when(doctorRepository).deleteById(anyInt());

        assertDoesNotThrow(() -> doctorService.delete(999));
        verify(doctorRepository, times(1)).deleteById(999);
    }

    @Test
    void save_ShouldHandleNullValues_WhenDoctorDtoHasNullFields() {
        DoctorDto nullDto = new DoctorDto();
        nullDto.setName(null);
        nullDto.setSurname(null);

        when(doctorRepository.save(any(Doctor.class))).thenReturn(new Doctor());

        assertDoesNotThrow(() -> doctorService.save(nullDto));
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void get_ShouldReturnCorrectMapping_WhenDoctorHasAllFields() {
        Doctor fullDoctor = new Doctor();
        fullDoctor.setId(5);
        fullDoctor.setName("Anna");
        fullDoctor.setSurname("Smirnova");

        when(doctorRepository.findById(5)).thenReturn(Optional.of(fullDoctor));

        DoctorDto result = doctorService.get(5);

        assertNotNull(result);
        assertEquals("Anna", result.getName());
        assertEquals("Smirnova", result.getSurname());
    }
}
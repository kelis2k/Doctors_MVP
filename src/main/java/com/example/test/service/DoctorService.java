package com.example.test.service;

import com.example.test.dto.DoctorDto;
import com.example.test.model.Doctor;
import com.example.test.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;


    public void save(DoctorDto doctorDto) {
        doctorRepository.save(toModel(doctorDto));
        System.out.println(doctorDto.toString());
    }

    public DoctorDto get(Integer id ) {
        Doctor doctor = doctorRepository.findById(id).get();
        System.out.println(doctor);
        return toDto(doctor);
    }

    public void update(DoctorDto doctorDto, Integer id) {
        Doctor doctor = doctorRepository.findById(id).get();
        doctor.setName(doctorDto.getName());
        doctor.setSurname(doctorDto.getSurname());
        doctorRepository.save(doctor);
    }

    public void delete(Integer id) {
        doctorRepository.deleteById(id);
    }



    private Doctor toModel(DoctorDto doctorDto) {
        Doctor doctor = new Doctor();
        doctor.setName(doctorDto.getName());
        doctor.setSurname(doctorDto.getSurname());
        return doctor;
    }
    private DoctorDto toDto(Doctor doctor) {
        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setName(doctor.getName());
        doctorDto.setSurname(doctor.getSurname());
        return doctorDto;
    }
}

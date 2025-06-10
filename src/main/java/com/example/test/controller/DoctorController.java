package com.example.test.controller;

import com.example.test.dto.DoctorDto;
import com.example.test.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/doctors")
@RestController
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @PostMapping
    public void save(@RequestBody DoctorDto doctorDto) {
        doctorService.save(doctorDto);

    }

    @GetMapping
    public DoctorDto get(@RequestParam Integer id) {
        return doctorService.get(id);
    }

    @PutMapping
    public void update(@RequestBody DoctorDto doctorDto, @RequestParam Integer id) {
        doctorService.update(doctorDto, id);
    }

    @DeleteMapping
    public void delete(@RequestParam Integer id) {
        doctorService.delete(id);
    }

}




package com.medibook.appointment.service;

import com.medibook.appointment.dto.DoctorRequest;
import com.medibook.appointment.dto.DoctorResponse;
import com.medibook.appointment.entity.Doctor;
import com.medibook.appointment.repository.AppointmentRepository;
import com.medibook.appointment.repository.DoctorRepository;
import com.medibook.appointment.repository.SlotRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository doctors;
    private final SlotRepository slots;
    private final AppointmentRepository appointments;

    public DoctorService(DoctorRepository doctors, SlotRepository slots, AppointmentRepository appointments) {
        this.doctors = doctors;
        this.slots = slots;
        this.appointments = appointments;
    }

    @Transactional(readOnly = true)
    public List<DoctorResponse> search(String name, String specialisation) {
        return doctors.findByNameContainingIgnoreCaseAndSpecialisationContainingIgnoreCase(name, specialisation)
                .stream().map(DoctorResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public DoctorResponse get(Long id) { return DoctorResponse.from(find(id)); }

    @Transactional
    public DoctorResponse create(DoctorRequest request) {
        return DoctorResponse.from(doctors.save(new Doctor(request.name(), request.specialisation(), request.qualification(), request.clinic())));
    }

    @Transactional
    public DoctorResponse update(Long id, DoctorRequest request) {
        Doctor doctor = find(id);
        doctor.setName(request.name());
        doctor.setSpecialisation(request.specialisation());
        doctor.setQualification(request.qualification());
        doctor.setClinic(request.clinic());
        return DoctorResponse.from(doctor);
    }

    @Transactional
    public void delete(Long id) {
        Doctor doctor = find(id);
        if (slots.existsByDoctorId(id) || appointments.existsByDoctorId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A doctor with slots or appointments cannot be deleted");
        }
        doctors.delete(doctor);
    }

    private Doctor find(Long id) {
        return doctors.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found"));
    }
}

package com.echanneling.e_channeling_system.service;

import com.echanneling.e_channeling_system.entity.LabOrder;
import com.echanneling.e_channeling_system.repository.LabOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LabOrderService {

    private final LabOrderRepository labOrderRepository;

    public LabOrderService(LabOrderRepository labOrderRepository) {
        this.labOrderRepository = labOrderRepository;
    }

    public void saveLabOrder(LabOrder labOrder) {
        labOrderRepository.save(labOrder);
    }

    public List<LabOrder> getAllLabOrders() {
        return labOrderRepository.findAll();
    }

    public Optional<LabOrder> getLabOrderById(Long id) {
        return labOrderRepository.findById(id);
    }

    public void deleteLabOrder(Long id) {
        labOrderRepository.deleteById(id);
    }
}
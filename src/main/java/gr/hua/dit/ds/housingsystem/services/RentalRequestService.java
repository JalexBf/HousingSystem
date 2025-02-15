package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.enums.RequestStatus;
import gr.hua.dit.ds.housingsystem.entities.model.RentalRequest;
import gr.hua.dit.ds.housingsystem.repositories.RentalRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class RentalRequestService {

    @Autowired
    private RentalRequestRepository rentalRequestRepository;

    @Transactional
    public List<RentalRequest> getAllRentalRequests() {
        return rentalRequestRepository.findAll();
    }

    @Transactional
    public Optional<RentalRequest> getRentalRequestById(Long id) {
        return rentalRequestRepository.findById(id);
    }

    @Transactional
    public RentalRequest createRentalRequest(RentalRequest rentalRequest) {
        rentalRequest.setStatus(RequestStatus.PENDING);
        return rentalRequestRepository.save(rentalRequest);
    }

    @Transactional
    public RentalRequest updateRentalRequest(Long id, RequestStatus status) {
        RentalRequest rentalRequest = rentalRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental Request not found"));
        rentalRequest.setStatus(status);
        return rentalRequestRepository.save(rentalRequest);
    }

    @Transactional
    public void deleteRentalRequest(Long id) {
        rentalRequestRepository.deleteById(id);
    }
}
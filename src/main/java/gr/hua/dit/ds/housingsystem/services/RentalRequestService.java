package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.enums.RequestStatus;
import gr.hua.dit.ds.housingsystem.entities.model.RentalRequest;
import gr.hua.dit.ds.housingsystem.repositories.RentalRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class RentalRequestService {
    private final RentalRequestRepository rentalRequestRepository;

    public RentalRequestService(RentalRequestRepository rentalRequestRepository) {
        this.rentalRequestRepository = rentalRequestRepository;
    }

    public Page<RentalRequest> getRentalRequestsByStatus(RequestStatus status, int page, int size) {
        return rentalRequestRepository.findByStatus(status, PageRequest.of(page, size));
    }
}

package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.enums.RequestStatus;
import gr.hua.dit.ds.housingsystem.entities.model.ViewingRequest;
import gr.hua.dit.ds.housingsystem.repositories.ViewingRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ViewingRequestService {

    @Autowired
    private ViewingRequestRepository viewingRequestRepository;

    @Transactional
    public List<ViewingRequest> getAllViewingRequests() {
        return viewingRequestRepository.findAll();
    }

    @Transactional
    public Optional<ViewingRequest> getViewingRequestById(Long id) {
        return viewingRequestRepository.findById(id);
    }


    @Transactional
    public ViewingRequest createViewingRequest(ViewingRequest viewingRequest) {
        viewingRequest.setStatus(RequestStatus.PENDING);
        return viewingRequestRepository.save(viewingRequest);
    }


    @Transactional
    public ViewingRequest updateViewingRequest(Long id, RequestStatus status) {
        ViewingRequest viewingRequest = viewingRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Viewing Request not found"));
        viewingRequest.setStatus(status);
        return viewingRequestRepository.save(viewingRequest);
    }


    @Transactional
    public void deleteViewingRequest(Long id) {
        viewingRequestRepository.deleteById(id);
    }


    @Transactional
    public ViewingRequest manageViewingRequest(Long requestId, RequestStatus status, Long ownerId) {
        ViewingRequest viewingRequest = viewingRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Viewing Request not found"));

        if (!viewingRequest.getProperty().getOwner().getId().equals(ownerId)) {
            throw new RuntimeException("Unauthorized: You are not the owner of this property");
        }

        viewingRequest.setStatus(status);
        return viewingRequestRepository.save(viewingRequest);
    }
}

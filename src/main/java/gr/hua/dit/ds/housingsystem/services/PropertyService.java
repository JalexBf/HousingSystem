package gr.hua.dit.ds.housingsystem.services;

import gr.hua.dit.ds.housingsystem.entities.enums.PropertyCategory;
import gr.hua.dit.ds.housingsystem.entities.model.Photo;
import gr.hua.dit.ds.housingsystem.entities.model.Property;
import gr.hua.dit.ds.housingsystem.repositories.PhotoRepository;
import gr.hua.dit.ds.housingsystem.repositories.PropertyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PhotoService photoService;
    private final PhotoRepository photoRepository;


    public PropertyService(PropertyRepository propertyRepository, PhotoService photoService, PhotoRepository photoRepository) {
        this.propertyRepository = propertyRepository;
        this.photoService = photoService;
        this.photoRepository = photoRepository;
    }


    public Property saveProperty(Property property) {
        return propertyRepository.save(property);
    }


    public Property getPropertyById(Long propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new EntityNotFoundException("Property not found with ID: " + propertyId));
    }


    public void addPhotoToProperty(Long propertyId, MultipartFile file, String uploadDirectory, String prefix) throws IOException {
        Property property = getPropertyById(propertyId);
        String filePath = photoService.uploadPhoto(file, uploadDirectory, prefix);

        Photo photo = new Photo();
        photo.setFilePath(filePath);
        photo.setProperty(property);

        photoRepository.save(photo);
    }


    public List<Property> getPropertiesByCategory(PropertyCategory category) {
        return propertyRepository.findByCategory(category);
    }


    public List<Property> getPropertiesByArea(String area) {
        return propertyRepository.findByAreaContaining(area);
    }


    public List<Property> getPropertiesByOwner(Long ownerId) {
        return propertyRepository.findByOwnerId(ownerId);
    }


    public void approveProperty(Long propertyId) {
        Property property = getPropertyById(propertyId);
        property.setApproved(true);
        propertyRepository.save(property);
    }


    public void deleteProperty(Long propertyId) {
        if (!propertyRepository.existsById(propertyId)) {
            throw new EntityNotFoundException("Property not found with ID: " + propertyId);
        }
        propertyRepository.deleteById(propertyId);
    }


    public List<Property> searchProperties(String category, Double minPrice, Double maxPrice, String location, Integer minRooms) {
        return propertyRepository.searchProperties(category, minPrice, maxPrice, location, minRooms);
    }
}
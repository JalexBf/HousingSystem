package gr.hua.dit.ds.housingsystem.services;

import java.util.List;

public class PropertyDTO {
    private Long id;
    private String category;
    private String area;
    private String firstPhotoUrl;

    public PropertyDTO(Long id, String category, String area, String firstPhotoUrl) {
        this.id = id;
        this.category = category;
        this.area = area;
        this.firstPhotoUrl = firstPhotoUrl;
    }

    // Getters
    public Long getId() { return id; }
    public String getCategory() { return category; }
    public String getArea() { return area; }
    public String getFirstPhotoUrl() { return firstPhotoUrl; }
}


package gr.hua.dit.ds.housingsystem.DTO;

public class AvailabilitySlotDTO {
    private Long id;
    private String dayOfWeek;
    private Integer startHour;
    private Integer endHour;

    public AvailabilitySlotDTO(Long id, String dayOfWeek, Integer startHour, Integer endHour) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public Long getId() {
        return id;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public Integer getEndHour() {
        return endHour;
    }
}

package gr.hua.dit.ds.housingsystem.services;

import java.time.DayOfWeek;

public class AvailabilitySlotDTO {
    private Long id;
    private DayOfWeek dayOfWeek;
    private Integer startHour;
    private Integer endHour;

    public AvailabilitySlotDTO(Long id, DayOfWeek dayOfWeek, Integer startHour, Integer endHour) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public Long getId() { return id; }
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public Integer getStartHour() { return startHour; }
    public Integer getEndHour() { return endHour; }
}

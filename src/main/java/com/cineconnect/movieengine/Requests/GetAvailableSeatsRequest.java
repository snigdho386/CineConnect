package com.cineconnect.movieengine.Requests;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class GetAvailableSeatsRequest {
    private String movieName;
    private LocalDate showDate;
    private LocalTime showTime;
    private Integer theaterId;
}

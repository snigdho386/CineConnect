package com.cineconnect.movieengine.Requests;

import lombok.Data;
import java.util.List;

@Data
public class SeatSelectionRequest {
    private Integer showId;
    private List<String> selectedSeats;
    private String userMobNo;
}

package com.cineconnect.movieengine.Repository;

import com.cineconnect.movieengine.Models.Movie;
import com.cineconnect.movieengine.Models.Show;
import com.cineconnect.movieengine.Models.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalTime;

public interface ShowRepository extends JpaRepository<Show,Integer> {

    public Show findShowByShowDateAndShowTimeAndMovieAndTheater(LocalDate showDate,
                                                                LocalTime showTime,
                                                                Movie movie,
                                                                Theater theater);
}

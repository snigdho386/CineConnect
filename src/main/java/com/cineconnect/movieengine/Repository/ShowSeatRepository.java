package com.cineconnect.movieengine.Repository;

import com.cineconnect.movieengine.Models.Show;
import com.cineconnect.movieengine.Models.ShowSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ShowSeatRepository extends JpaRepository<ShowSeat,Integer> {

    public List<ShowSeat> findAllByShow(Show show); //Inbuilt method invoking

    @Query(nativeQuery = true,value = "select * from show_seats where show_show_id = :showId")
    public List<ShowSeat> findShowSeats(Integer showId);
}

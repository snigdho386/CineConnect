package com.cineconnect.movieengine.Repository;

import com.cineconnect.movieengine.Models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket,String> {

}

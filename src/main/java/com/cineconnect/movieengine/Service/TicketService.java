package com.cineconnect.movieengine.Service;

import com.cineconnect.movieengine.Exceptions.SeatUnavailableException;
import com.cineconnect.movieengine.Models.*;
import com.cineconnect.movieengine.Repository.*;
import com.cineconnect.movieengine.Requests.BookTicketRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    // SeatSelectionRepository for managing temporary seat selections
    @Autowired
    private SeatSelectionRepository seatSelectionRepository;

    @Transactional
    public Ticket bookTicket(BookTicketRequest bookTicketRequest) throws Exception{

        // Get the show and check temporary seat selections
        Movie movie = movieRepository.findMovieByMovieName(bookTicketRequest.getMovieName());
        Theater theater = theaterRepository.findById(bookTicketRequest.getTheaterId()).get();

        //1.1 Find the ShowEntity with this date and Time
        Show show = showRepository.findShowByShowDateAndShowTimeAndMovieAndTheater(
                bookTicketRequest.getShowDate(),
                bookTicketRequest.getShowTime(),
                movie,
                theater
        );

        // Check if seats are temporarily selected by the user
        Date cutoffTime = new Date(System.currentTimeMillis() - 10 * 60 * 1000); // 10 minutes ago
        List<SeatSelection> userTempSelections = seatSelectionRepository.findByShowAndStatusAndCreatedAtAfter(
                show, "TEMP", cutoffTime
        );

        List<String> userSelectedSeats = userTempSelections.stream()
                .filter(s -> s.getUserMobNo().equals(bookTicketRequest.getMobNo()))
                .map(SeatSelection::getSeatNo)
                .collect(Collectors.toList());

        // Verify requested seats match temp selections
        if (!userSelectedSeats.containsAll(bookTicketRequest.getRequestedSeats())) {
            throw new SeatUnavailableException("Please select seats first through the seat selection interface. Some requested seats are not in your temporary selection.");
        }

        // Calculate the total cost of the tickets
        Integer showId = show.getShowId();
        List<ShowSeat> showSeatList = showSeatRepository.findShowSeats(showId);

        //Calculate the total Amt and if all seats mentioned are available or not
        int totalAmount = 0;
        Boolean areAllSeatsAvailable = Boolean.TRUE;

        for(String seatNo:bookTicketRequest.getRequestedSeats()) {
            for(ShowSeat showSeat:showSeatList) {
                if(showSeat.getSeatNo().equals(seatNo))
                {
                    if(showSeat.getIsAvailable()==Boolean.FALSE){
                        areAllSeatsAvailable = Boolean.FALSE;
                        break;
                    }
                    totalAmount = totalAmount+showSeat.getPrice();
                }
            }
        }

        if(areAllSeatsAvailable==Boolean.FALSE){
            throw new SeatUnavailableException("The requested Seats are unavailable");
        }

        // Make the seats booked (only if seats are available, otherwise throw exception)
        for(String seatNo:bookTicketRequest.getRequestedSeats()) {
            for(ShowSeat showSeat:showSeatList) {
                if(showSeat.getSeatNo().equals(seatNo))
                {
                    showSeat.setIsAvailable(Boolean.FALSE);
                }
            }
        }

        // Save updated show seats
        showSeatRepository.saveAll(showSeatList);

        User user = userRepository.findUserByMobNo(bookTicketRequest.getMobNo());

        // Create and save the ticket
        Ticket ticket = Ticket.builder()
                .user(user)
                .movieName(bookTicketRequest.getMovieName())
                .showDate(bookTicketRequest.getShowDate())
                .theaterNameAndAddress(theater.getName()+" "+theater.getAddress())
                .showTime(bookTicketRequest.getShowTime())
                .totalAmtPaid(totalAmount)
                .build();

        ticket = ticketRepository.save(ticket);

        // Update temp selections to CONFIRMED after successful booking
        for (SeatSelection selection : userTempSelections) {
            if (bookTicketRequest.getRequestedSeats().contains(selection.getSeatNo())
                    && selection.getUserMobNo().equals(bookTicketRequest.getMobNo())) {
                selection.setStatus("CONFIRMED");
                seatSelectionRepository.save(selection);
            }
        }

        // Clean up any other temporary selections for these seats from other users
        List<SeatSelection> allTempSelections = seatSelectionRepository.findByShowAndStatusAndCreatedAtAfter(
                show, "TEMP", cutoffTime
        );

        for (SeatSelection selection : allTempSelections) {
            if (bookTicketRequest.getRequestedSeats().contains(selection.getSeatNo())
                    && !selection.getUserMobNo().equals(bookTicketRequest.getMobNo())) {
                seatSelectionRepository.delete(selection);
            }
        }

        return ticket;
    }
}
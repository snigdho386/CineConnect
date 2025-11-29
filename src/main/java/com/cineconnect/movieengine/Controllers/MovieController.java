package com.cineconnect.movieengine.Controllers;

import com.cineconnect.movieengine.Models.Movie;
import com.cineconnect.movieengine.Requests.UpdateMovieRequest;
import com.cineconnect.movieengine.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("movie")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @PostMapping("/addMovie")
    public String addMovie(@RequestBody Movie movie){
        String response = movieService.addMovie(movie);
        return response;
    }

    @PutMapping("/updateMovieAttributes")
    public String updateMovieAttributes(@RequestBody UpdateMovieRequest movieRequest) {
        // FIXED: Removed the unnecessary unused variable declaration
        String result = movieService.updateMovieAttributes(movieRequest);
        return result;
    }
}
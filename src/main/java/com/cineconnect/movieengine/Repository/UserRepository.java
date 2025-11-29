package com.cineconnect.movieengine.Repository;

import com.cineconnect.movieengine.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    public User findUserByMobNo(String mobNo);
}
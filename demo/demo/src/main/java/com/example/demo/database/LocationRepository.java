package com.example.demo.database;

import com.example.demo.data.Location;
import com.example.demo.data.Section;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, String> {

    @Cacheable("campusCache")
    @Query(value = "SELECT l.campus FROM Location l ORDER BY l.campus")
    public List<String> getCampuses();
}

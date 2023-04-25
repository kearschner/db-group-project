package com.example.demo.database.repositories;

import com.example.demo.data.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long>, MeetingRepositoryCustom{

}

package com.example.jobscrapper.repository;

import com.example.jobscrapper.model.JobAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobAdRepository extends JpaRepository<JobAd, Long> {
}


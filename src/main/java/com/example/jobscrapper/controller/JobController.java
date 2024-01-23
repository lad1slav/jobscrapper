package com.example.jobscrapper.controller;

import com.example.jobscrapper.model.JobAd;
import com.example.jobscrapper.service.ScrappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    ScrappingService scrappingService;

    @GetMapping("/search")
    List<JobAd> searchJobs(@RequestParam String function) {
        return scrappingService.getJobsResult(function);
    }

    @GetMapping
    List<JobAd> getJobs() {
        return scrappingService.getJobs();
    }

    @DeleteMapping
    void deleteJobs() {
        scrappingService.deleteJobs();
    }
}

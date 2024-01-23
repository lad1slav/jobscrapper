package com.example.jobscrapper.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JobAd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(columnDefinition = "varchar(526)")
    String jobPageUrl;
    String positionName;
    @Column(columnDefinition = "varchar(526)")
    String organisationUrl;
    @Column(columnDefinition = "varchar(526)")
    String logo;
    String organisationTitle;
    String laborFunction;
    String location;
    LocalDate postedDate;
    @Column(columnDefinition = "varchar(20000)")
    String description;
    @Column(columnDefinition = "varchar(526)")
    String tags;
}

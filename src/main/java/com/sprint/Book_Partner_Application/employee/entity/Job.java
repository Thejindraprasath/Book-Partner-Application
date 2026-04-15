package com.sprint.Book_Partner_Application.employee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Short jobId;

    @Column(name = "job_desc", nullable = false, length = 50)
    @NotBlank(message = "Job description is required")
    @Size(max = 50, message = "Job description must not exceed 50 characters")
    @Builder.Default
    private String jobDesc = "New Position - title not formalized yet";

    @Column(name = "min_lvl", nullable = false)
    @NotNull(message = "Minimum level is required")
    @Min(value = 10, message = "Minimum level must be at least 10")
    private Integer minLvl;

    @Column(name = "max_lvl", nullable = false)
    @NotNull(message = "Maximum level is required")
    @Max(value = 250, message = "Maximum level must not exceed 250")
    private Integer maxLvl;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Employee> employees = new ArrayList<>();
}

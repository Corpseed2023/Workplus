package model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
    public class ProcessData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "memory_usage")
    private double memoryUsage;

    @Column(name = "cpu_usage")
    private Double cpuUsage;

    @Column(name = "process_name")
    private String processName;

    @Column(name = "process_id")
    private int processId;



}



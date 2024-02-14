package model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Screenshot {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pageName")
    private String pageName;



}

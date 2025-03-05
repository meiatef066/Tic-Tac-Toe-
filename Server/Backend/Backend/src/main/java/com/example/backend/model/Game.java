package com.example.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Game {
    @Id
    private Long id;
    @ManyToOne
    private User player1;
    @ManyToOne
    private User player2;
    @ManyToOne
    private User Winner;
    @Enumerated(EnumType.STRING)
    private GameSttatus gameSttatus;
    private LocalDate gameDate;
}

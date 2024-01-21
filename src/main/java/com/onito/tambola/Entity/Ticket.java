package com.onito.tambola.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @JsonIgnore
    private UUID groupId;
    @JsonIgnore
    private LocalDateTime createdAt;
    @ElementCollection
    @Column(name = "number")
    private int[][] ticket;
    @JsonIgnore
    @Column(unique = true)
    private String arrayHash;
}

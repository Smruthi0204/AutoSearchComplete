package com.AutoSearchComplete.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "search_terms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String term;

    private int frequency;
}

package com.verycoolapp.ideahub.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "IDEA")
public class Idea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "NAME")
    private String name;

    @Column(name = "IMAGE_PATH")
    private String imagePath;

}

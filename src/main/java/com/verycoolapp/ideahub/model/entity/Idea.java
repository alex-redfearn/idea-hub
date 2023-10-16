package com.verycoolapp.ideahub.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "IDEA")
public class Idea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "NAME")
    private String name;

    // The image for an idea could be uploaded to a CDN and the path stored in the DB.
    @Column(name = "IMAGE_PATH")
    private String imagePath;

    @OneToMany(mappedBy = "idea")
    private List<IdeaComment> comments;

    @OneToMany(mappedBy = "idea")
    private List<IdeaLike> likes;

}

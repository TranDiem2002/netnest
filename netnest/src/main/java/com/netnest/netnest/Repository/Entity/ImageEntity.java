package com.netnest.netnest.Repository.Entity;

import com.netnest.netnest.Model.UserModel;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Blob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "images")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "image")
    @Lob
    private Blob image;

    @Column(name = "createDate")
    private String createdate;

//    @ManyToOne
//    private UserModel user;

    public ImageEntity(Blob image) {
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date dateobj = new Date();
        this.image = image;
        this.createdate = df.format(dateobj);
    }

}

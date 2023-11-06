package com.netnest.netnest.Model;

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
public class ImageModel {

    private Long id;
    private Blob image;
    private Date createDate;

}

package com.webchat.netnest.util;

import com.webchat.netnest.Model.ImageModel;
import com.webchat.netnest.entity.imageEntity;
import org.modelmapper.ModelMapper;

import java.sql.Blob;
import java.sql.SQLException;

public class ImageMapper {

    ModelMapper modelMapper = new ModelMapper();

    public ImageModel convertToModel(imageEntity image){
        return modelMapper.map(image, ImageModel.class);
    }

    public imageEntity convertToEntity(ImageModel image){
        return modelMapper.map(image, imageEntity.class);
    }

    public String convertBase64( imageEntity image){
        String base64Image = null;
        Blob blob = image.getImage();
        try {
            base64Image = java.util.Base64.getEncoder().encodeToString(blob.getBytes(1,(int) blob.length()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return base64Image;
    }

}

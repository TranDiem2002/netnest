package com.netnest.netnest.until;

import com.netnest.netnest.Model.ImageModel;
import com.netnest.netnest.Repository.Entity.ImageEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class ImageMapper {

    @Autowired
   private ModelMapper modelMapper;

    public ImageEntity covertToEntity (ImageModel imageModel){
        ImageEntity image = modelMapper.map(imageModel, ImageEntity.class);
        return image;
    }
    public ImageModel covertToModel (ImageEntity imageEntity){
       return  modelMapper.map(imageEntity, ImageModel.class);
    }
}

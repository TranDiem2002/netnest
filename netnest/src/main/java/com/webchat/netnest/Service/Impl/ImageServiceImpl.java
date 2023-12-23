package com.webchat.netnest.Service.Impl;

import com.webchat.netnest.Model.ImageModel;
import com.webchat.netnest.Repository.ImageRepository;
import com.webchat.netnest.Service.ImageService;
import com.webchat.netnest.entity.imageEntity;
import com.webchat.netnest.util.ImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    private ImageMapper imageMapper;

    public ImageServiceImpl() {
        this.imageMapper = new ImageMapper();
    }

    @Override
    public ImageModel createImage(ImageModel image) {
        imageEntity imageEntity = imageMapper.convertToEntity(image);
        imageEntity saveImage = imageRepository.save(imageEntity);
        return imageMapper.convertToModel(saveImage);
    }

    @Override
    public Optional<imageEntity> findImageById(Long id) {
        return imageRepository.findById(id);
    }
}

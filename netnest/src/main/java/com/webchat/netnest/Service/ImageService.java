package com.webchat.netnest.Service;

import com.webchat.netnest.Model.ImageModel;
import com.webchat.netnest.entity.imageEntity;

import java.util.Optional;

public interface ImageService {
    ImageModel createImage(ImageModel image);

    Optional<imageEntity> findImageById(Long id);
}

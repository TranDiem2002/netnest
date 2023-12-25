package com.webchat.netnest.util;

import com.webchat.netnest.Model.VideoModel;
import com.webchat.netnest.entity.videoEntity;
import org.modelmapper.ModelMapper;

import java.sql.Blob;
import java.sql.SQLException;

public class VideoMapper {

    ModelMapper modelMapper = new ModelMapper();

    public VideoModel convertToModel(videoEntity video){
        return modelMapper.map(video, VideoModel.class);
    }

    public videoEntity convertToEntity(VideoModel video){
        return modelMapper.map(video, videoEntity.class);
    }

    public String convertBase64( videoEntity video){
        String base64Video = null;
        Blob blob = video.getVideo();
        try {
            base64Video = java.util.Base64.getEncoder().encodeToString(blob.getBytes(1,(int) blob.length()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return base64Video;
    }
}

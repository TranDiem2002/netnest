package com.webchat.netnest.Controller;

import com.webchat.netnest.Model.ImageModel;
import com.webchat.netnest.Service.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

@Controller
@AllArgsConstructor
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/images")
    public ResponseEntity<ImageModel> saveImage(HttpServletRequest request, @RequestParam("image") MultipartFile file)
            throws IOException, SerialException, SQLException
    {
        byte[] bytes = file.getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

        ImageModel imageModel = new ImageModel();
        imageModel.setImage(blob);
        imageService.createImage(imageModel);
        return new ResponseEntity<>(imageModel, HttpStatus.CREATED);
    }


}

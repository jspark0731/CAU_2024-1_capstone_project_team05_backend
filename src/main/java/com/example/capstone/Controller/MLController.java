package com.example.capstone.Controller;

import com.example.capstone.Dto.MLDto;
import com.example.capstone.Dto.ResponseDto;
import com.example.capstone.Service.MLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ML")
public class MLController {

        @Autowired
        private MLService mlService;

        @PostMapping("/separate")
        public ResponseEntity<ResponseDto<?>> separateAudio(
                @RequestParam("file") MultipartFile file,
                @RequestParam("model") String model,
                @RequestHeader("Authorization") String token) {

                String email = mlService.getCurrentUserEmail(token);
                if (email == null) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.setFailed("Invalid Token"));
                }

                MLDto mlDto = new MLDto(token, file, model, email);
                ResponseDto<?> result = mlService.fileUploadToMLContainer(mlDto, file);

                if (result.getResult()) {
                        String filePath = result.getData().toString(); // Assuming data contains filePath
                        try {
                                mlService.sendFilePathToFastApi(filePath);
                        } catch (Exception e) {
                                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.setFailed("Failed to send file path to FastAPI server"));
                        }
                }

                return ResponseEntity.ok(result);
        }
}


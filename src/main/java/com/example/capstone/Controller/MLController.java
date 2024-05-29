package com.example.capstone.Controller;

import com.example.capstone.Dto.MLDto;
import com.example.capstone.Dto.ResponseDto;
import com.example.capstone.Service.MLService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ml")
public class MLController
{
        private static final Logger logger = LoggerFactory.getLogger(MLController.class);

        @Autowired
        private MLService mlService;

        @PostMapping("/separate")
        public ResponseEntity<ResponseDto<?>> separateAudio(
                @RequestPart(value = "file", required = true) MultipartFile file,
                @RequestParam(value = "model", required = true) String model,
                @RequestParam(value = "instrumentType", required = true) String instrumentType,
                @RequestHeader(value = "Authorization", required = true) String token)
        {
                logger.info("Received request - file: {}, model: {}, instrumentType: {}, token: {}",
                        file.getOriginalFilename(), model, instrumentType, token);

                // Bearer 부분을 제거하고 실제 토큰 값만 사용
                if (token.startsWith("Bearer "))
                {
                        token = token.substring(7);
                }

                String email = mlService.getCurrentUserEmail(token);
                if (email == null) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.setFailed("Invalid Token"));
                }

                String fileName = file.getOriginalFilename();
                String filePath = mlService.defineFilePath(email, fileName);

                MLDto mlDto = new MLDto(token, model, instrumentType, fileName, filePath);
                ResponseDto<?> result = mlService.fileUploadToMLContainer(mlDto, file);

                if (result.getResult()) {
                        String definedFilePath = result.getData().toString(); // Assuming data contains definedFilePath
                        logger.info("definedFilePath : {}", definedFilePath);

                        try {
                                mlService.sendFilePathToFastApi(definedFilePath);
                        } catch (Exception e) {
                                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.setFailed("Failed to send file path to FastAPI server"));
                        }
                }
                return ResponseEntity.ok(result);
        }
}

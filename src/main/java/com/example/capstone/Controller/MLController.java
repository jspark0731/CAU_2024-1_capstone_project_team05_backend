package com.example.capstone.Controller;

import com.example.capstone.Dto.MLDto;
import com.example.capstone.Dto.ResponseDto;
import com.example.capstone.Service.MLService;
import com.example.capstone.Service.PathCache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/api/ml")
public class MLController
{
        private static final Logger logger = LoggerFactory.getLogger(MLController.class);
        private final PathCache musescorePathCache;

        @Autowired
        public MLController(PathCache musescorePathCache) {
                this.musescorePathCache = musescorePathCache;
        }

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

                MLDto mlDto = new MLDto(token, 0, model, instrumentType, fileName, filePath, email, "", "", "");
                ResponseDto<?> result = mlService.saveFileAndData(mlDto, file);

                if (result.getResult())
                {
                        try
                        {
                                mlService.sendMLDtoToFastApi(mlDto);
                                return ResponseEntity.ok(ResponseDto.setSuccess("File processed successfully"));
                        } catch (Exception e)
                        {
                                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.setFailed("Failed to send file path to FastAPI server"));
                        }
                }
                return ResponseEntity.ok(result);
        }

        @PostMapping("/receive_path")
        public ResponseEntity<?> receiveMusescoreOutputPath(@RequestBody Map<String, String> payload) {
                String musescoreOutputPath = payload.get("musescoreOutputPath");
                String token = payload.get("token");

                if (musescoreOutputPath == null || musescoreOutputPath.isEmpty()) {
                        logger.error("musescoreOutputPath is null or empty");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("musescoreOutputPath cannot be null or empty");
                }

                logger.info("Received musescoreOutputPath: " + musescoreOutputPath);
                musescorePathCache.storePath(token, musescoreOutputPath);

                return ResponseEntity.ok().build();
        }

        @GetMapping("/get_xml")
        public ResponseEntity<Resource> getXml(@RequestHeader(value = "Authorization", required = true) String token) {
                // Bearer 부분을 제거하고 실제 토큰 값만 사용
                if (token.startsWith("Bearer ")) {
                        token = token.substring(7);
                }

                String musescoreOutputPath = musescorePathCache.getPath(token);
                logger.info("musescorePath:{}", musescoreOutputPath);

                if (musescoreOutputPath != null) {
                        try {
                                Path path = Paths.get(musescoreOutputPath);
                                Resource resource = new UrlResource(path.toUri());
                                if (resource.exists() && resource.isReadable()) {
                                        return ResponseEntity.ok()
                                                .contentType(MediaType.parseMediaType("application/xml"))
                                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                                                .body(resource);
                                } else {
                                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                                }
                        } catch (Exception e) {
                                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                        } finally {
                                musescorePathCache.removePath(token); // Remove the path after serving the file
                        }
                } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
        }
}

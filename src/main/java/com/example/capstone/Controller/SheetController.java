package com.example.capstone.Controller;

import com.example.capstone.Dto.ResponseDto;
import com.example.capstone.Dto.SheetDto;
import com.example.capstone.Service.SheetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sheet")
public class SheetController
{
        private static final Logger logger = LoggerFactory.getLogger(SheetController.class);

        @Autowired
        SheetService sheetService;

        @PostMapping("/generate")
        public ResponseEntity<ResponseDto<?>> makeSheet(
                @RequestParam(value = "model", required = true) String model,
                @RequestParam(value = "instrumentType", required = true) String instrumentType,
                @RequestParam(value = "videoId", required = true) String videoId,
                @RequestHeader(value = "Authorization", required = true) String token
                )
        {
                logger.info("Received request - model:{}, instrumentType:{}, videoId:{}", model, instrumentType, videoId);

                // Auth 과정
                if (token.startsWith("Bearer "))
                {
                        token = token.substring(7);
                }

                String email = sheetService.getCurrentUserEmail(token);
                if (email == null) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.setFailed("Invalid Token"));
                }

                // Dto 생성
                SheetDto sheetDto = new SheetDto(0, token, email, videoId, instrumentType, model, "", "", "" ,"", "");
                ResponseDto<?> result = sheetService.downloadMp3FromApi(sheetDto);

                if (result.getResult())
                {
                        try
                        {
                                sheetService.sendSheetDtoToApi(sheetDto);
                        } catch (Exception e)
                        {
                                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.setFailed("Failed to send file path to FastAPI server"));
                        }
                }

                return ResponseEntity.ok(result);
        }
}

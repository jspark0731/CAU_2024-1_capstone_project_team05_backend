package com.example.capstone.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "set")
public class ResponseDto<D>
{
        private boolean result;
        private String message;
        private D data;

        // 성공 Return
        public static <D> ResponseDto<D> setSuccess(String message)
        {
                return ResponseDto.set(true, message, null);
        }

        // 실패 Return
        public static <D> ResponseDto<D> setFailed(String message)
        {
                return ResponseDto.set(false, message, null);
        }

        // 성공 Return + Data
        public static <D> ResponseDto<D> setSuccessData(String message, D data)
        {
                return ResponseDto.set(true, message, data);
        }

        // 실패 Return + Data
        public static <D> ResponseDto<D> setFailedData(String message, D data)
        {
                return ResponseDto.set(false, message, data);
        }

        // 요청 성공 여부 확인
        public boolean getResult()
        {
                return result;
        }
}
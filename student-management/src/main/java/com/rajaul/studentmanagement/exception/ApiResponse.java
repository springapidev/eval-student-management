package com.rajaul.studentmanagement.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;
/**
 * @author Mohammad Rajaul Islam
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private HttpStatus status;
    private int code;
    private List<ApiError> errors;

    public ApiResponse(HttpStatus status, int code) {
        this.status = status;
        this.code = code;
    }
}

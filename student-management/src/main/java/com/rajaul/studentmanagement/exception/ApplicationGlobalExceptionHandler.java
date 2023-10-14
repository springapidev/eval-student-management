package com.rajaul.studentmanagement.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Mohammad Rajaul Islam
 * @version 1.0.0
 * @since 1.0.0
 */
@RestControllerAdvice
public class ApplicationGlobalExceptionHandler  {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleMethodArgumentException(MethodArgumentNotValidException exception) {
        ApiResponse<?> serviceResponse = new ApiResponse<>();
        List<ApiError> errorDTOList = new ArrayList<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> {
                    ApiError errorDTO = new ApiError(error.getField()+" : "+error.getDefaultMessage());
                    errorDTOList.add(errorDTO);
                });
        serviceResponse.setStatus(HttpStatus.BAD_REQUEST);
        serviceResponse.setCode(400);
        serviceResponse.setErrors(errorDTOList);
        return serviceResponse;
    }
    @ExceptionHandler(UniqueConstraintViolationException.class)
    public ApiResponse<?> handleUniqueConstraintViolationException(UniqueConstraintViolationException ex) {
        // Customize the error response as per your needs
        String errorMessage = "Unique constraint violation: " + ex.getMessage();
        ApiResponse<?> serviceResponse = new ApiResponse<>();
        List<ApiError> errorDTOList = new ArrayList<>();
        errorDTOList.add(new ApiError(errorMessage));
        serviceResponse.setStatus(HttpStatus.CONFLICT);
        serviceResponse.setCode(409);
        serviceResponse.setErrors(errorDTOList);
        return serviceResponse;
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiResponse<?> dataIntegrityViolationException(DataIntegrityViolationException ex) {
        // Customize the error response as per your needs
        String errorMessage = "Used As Foreign Key: So you can not delete it ";
        ApiResponse<?> serviceResponse = new ApiResponse<>();
        List<ApiError> errorDTOList = new ArrayList<>();
        errorDTOList.add(new ApiError(errorMessage));
        serviceResponse.setStatus(HttpStatus.CONFLICT);
        serviceResponse.setCode(409);
        serviceResponse.setErrors(errorDTOList);
        return serviceResponse;
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponse<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiResponse<?> response = new ApiResponse<>();
        String errorMessage = "Resource Not Found Exception: " + ex.getMessage();
        List<ApiError> errorDTOList = new ArrayList<>();
        errorDTOList.add(new ApiError(errorMessage));
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setCode(404);
        response.setErrors(errorDTOList);
        return response;
    }
    @ExceptionHandler(DeleteSuccessMessage.class)
    public ApiResponse<?> handleDeleteSuccessMessage(DeleteSuccessMessage ex) {
        ApiResponse<?> response = new ApiResponse<>();
        String errorMessage = "Delete: " + ex.getMessage();
        List<ApiError> errorDTOList = new ArrayList<>();
        errorDTOList.add(new ApiError(errorMessage));
        response.setStatus(HttpStatus.NO_CONTENT);
        response.setCode(226);
        response.setErrors(errorDTOList);
        return response;
    }
    @ExceptionHandler(AssignCoursesSuccessMessage.class)
    public ApiResponse<?> assignCoursesSuccessMessage(AssignCoursesSuccessMessage ex) {
        ApiResponse<?> response = new ApiResponse<>();
        String errorMessage = "Assign: " + ex.getMessage();
        List<ApiError> errorDTOList = new ArrayList<>();
        errorDTOList.add(new ApiError(errorMessage));
        response.setStatus(HttpStatus.ACCEPTED);
        response.setCode(202);
        response.setErrors(errorDTOList);
        return response;
    }
    @ExceptionHandler(UpdateNotSuccessMessage.class)
    public ApiResponse<?> updateNotSuccessMessage(UpdateNotSuccessMessage ex) {
        ApiResponse<?> response = new ApiResponse<>();
        String errorMessage = "Update/Assign: " + ex.getMessage();
        List<ApiError> errorDTOList = new ArrayList<>();
        errorDTOList.add(new ApiError(errorMessage));
        response.setStatus(HttpStatus.NOT_MODIFIED);
        response.setCode(304);
        response.setErrors(errorDTOList);
        return response;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResponse<?> handleMethodNotAllowedException(HttpServletRequest request, Exception ex) {
        ApiResponse<?> response = new ApiResponse<>();
        String errorMessage = "Method Not Allowed: " + ex.getMessage();
        List<ApiError> errorDTOList = new ArrayList<>();
        errorDTOList.add(new ApiError(errorMessage));
        response.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
        response.setCode(405);
        response.setErrors(errorDTOList);
        return response;
    }

//    @ExceptionHandler(Exception.class)
//    public ApiResponse<?> handle(Exception ex, HttpServletRequest request, HttpServletResponse response) {
//        ApiResponse<?> responses = new ApiResponse<>();
//        List<ApiError> errorDTOList = new ArrayList<>();
//        if (ex instanceof HttpServerErrorException.InternalServerError) {
//            errorDTOList.add(new ApiError("Internal Server Error"));
//            responses.setCode(500);
//            responses.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//        }else if(ex instanceof NullPointerException){
//            errorDTOList.add(new ApiError("Bad Request"));
//            responses.setCode(500);
//            responses.setStatus(HttpStatus.BAD_REQUEST);
//        }
//        responses.setErrors(errorDTOList);
//        return responses;
//    }


}

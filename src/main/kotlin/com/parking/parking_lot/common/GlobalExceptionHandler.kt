package com.parking.parking_lot.common

import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class GlobalExceptionHandler {


    /**
     * DTO 검증 실패 시 400 Bad Request 와 메시지를 반환합니다.
     * - @Validation 검증시 사용합니다.
     *
     * @param ex [MethodArgumentNotValidException] - DTO 검증 실패 시 발생하는 예외 객체
     * @return Map<String, String> - 검증 실패한 필드명과 해당 오류 메시지를 포함한 맵
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handelValidationExceptions(ex: MethodArgumentNotValidException) : Map<String,String>{

        val errors = mutableMapOf<String, String>()//변경가능한 Map 자료형, JSON 형태로 전달하기 위함


        // 필드별 에러 메시지 저장
        ex.bindingResult.fieldErrors.forEach { error ->
            //디폴트 메시지가 없을경우, 단순하게 잘못된 입력값임을 명시, error.field는 검증실패한 필드 이름
            errors[error.field] = error.defaultMessage ?: "잘못된 입력값입니다."
        }
        return errors//에러반환
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalArgumentExceptions(ex:IllegalArgumentException) : Map<String,String>{



        return mapOf("error" to (ex.message?:"잘못된 요청입니다"))

    }

}
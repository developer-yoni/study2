package com.example.api.global.response;

import com.example.api.global.response.validation.ValidationFail;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 생성자를 통한 객체생성을 막음
@Builder(access = AccessLevel.PRIVATE) // builder를 내부적으로 쓰되, 외부에서는 막음
@JsonPropertyOrder({"isSuccess", "httpStatus", "apiCode", "result", "invalidInput"}) // 응답으로 나갈 Json 필드 순서 정렬
@JsonInclude(JsonInclude.Include.NON_NULL) // Json으로 응답이 나갈 때 - null인 필드는(CLASS LEVEL에 붙었으니) 응답으로 포함시키지 않는 어노테이션
public class ApiResponse<T> {
	
	private Boolean isSuccess;
	
	private HttpStatus httpStatus;

	private ApiCode apiCode;
	
	private T result;
	
	private T invalidInput;

	private Exception exception;
	
	/**
	 * [정적 메서드 팩토리 패턴]
	 * :생성자를 private으로 정의하므로 써 , 생성자를 통한 객체 생성을 막고 , 대신 static 생성메서드를 통한 객체 생성을 유도함
	 * */

	/**
	 * [API 성공시 나가는 응답]
	 * : 응답 결과가 없는 경우
	 * */
	
	public static ApiResponse success(ApiCode apiCode){
		
		return ApiResponse.builder()
						  .isSuccess(apiCode.getIsSuccess())
						  .httpStatus(apiCode.getHttpStatus())
						  .apiCode(apiCode)
						  .build();
	}
	
	/**
	 * [API 성공시 나가는 응답]
	 * : 응답 결과가 있는 경우
	 * */
	
	public static <T> ApiResponse<T> success(ApiCode apiCode, T result){

		return ApiResponse.<T>builder()
						  .isSuccess(apiCode.getIsSuccess())
						  .httpStatus(apiCode.getHttpStatus())
						  .apiCode(apiCode)
						  .result(result)
						  .build();
	}
	
	/**
	 *  [API 실패시 나가는 응답]
	 *  : 응답 결과가 없는 경우
	 * */
	public static ApiResponse fail(ApiCode apiCode){

		return ApiResponse.builder()
						  .isSuccess(apiCode.getIsSuccess())
						  .httpStatus(apiCode.getHttpStatus())
						  .apiCode(apiCode)
						  .build();
	}
	
	/**
	 *  [API 실패시 나가는 응답]
	 *  : 어떤 입력이 들어와서 실패 했는지 , 그 응답을 함께 보내줌
	 *  */
	public static <T> ApiResponse<T> failWithInvalidInput(ApiCode apiCode, T invalidInput){

		return ApiResponse.<T>builder()
						  .isSuccess(apiCode.getIsSuccess())
						  .httpStatus(apiCode.getHttpStatus())
						  .apiCode(apiCode)
						  .invalidInput(invalidInput)
						  .build();
	}
	
	/**
	 *  [API 실패시 나가는 응답]
	 *  :  Bean Validation 에 의한 검증 오류 시 , 그 결과를 넣을 수 있도록 함
	 *  */
	public static  ApiResponse failForBeanValidation(BindingResult bindResult){

		return ApiResponse.<ValidationFail>builder()
						  .isSuccess(ApiCode.CODE_000_0012.getIsSuccess())
						  .httpStatus(ApiCode.CODE_000_0012.getHttpStatus())
						  .apiCode(ApiCode.CODE_000_0012)
						  .invalidInput(ValidationFail.convert(bindResult))
						  .build();
	}

	/**
	 *  [API 실패시 나가는 응답]
	 *  :  기본적인 400에러 관련 예외 발생시
	 *  */
	public static ApiResponse failForBasicException(Exception exception) {

		return ApiResponse.builder()
						  .isSuccess(false)
						  .httpStatus(HttpStatus.BAD_REQUEST)
						  .exception(exception)
						  .build();
	}

	/**
	 *  [API 실패시 나가는 응답]
	 *  :  500에러 관련 예외 발생시
	 *  */
	public static ApiResponse failForInternalServerError(Exception exception) {

		return ApiResponse.builder()
						  .isSuccess(false)
						  .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
						  .exception(exception)
						  .build();
	}
}


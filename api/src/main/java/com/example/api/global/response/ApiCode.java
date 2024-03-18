package com.example.api.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 1000~1999 : 성공 내부 코드
 * 2000~2999 : 요청 실패 내부 코드
 * 3000~3999 : 응답 실패 내부 코드
 * 4000~4999 : DB  오류 내부 코드
 * 5000~5999 : Server 오류 내부 코드
 * */

@Getter
@RequiredArgsConstructor
public enum ApiCode {

	/**
	 * CODE_000_0000
	 * - 000 : 000~999 까지 개발자 별로 부여된 번호
	 * - 0000 : 0000 ~ 9999 까지, 각 개발자가 부여한 코드번호 (순차적으로 증가하도록)
	 *
	 * - 만약 enum값 자체를 NOT_FOUND_ENTITY 이런식으로 정의하면, 겹칠 가능성이 너무 많다고 판단함.
	 * */

	CODE_000_0000(true, HttpStatus.OK, "OK"),
	CODE_000_0001(true, HttpStatus.CREATED, "CREATED"),

	CODE_000_0010(false, HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"),
	CODE_000_0011(false, HttpStatus.BAD_REQUEST, "NOT_FOUND_ENTITY"),
	CODE_000_0012(false, HttpStatus.BAD_REQUEST, "BEAN_VALIDATION_FAIL");
	
	private final Boolean    isSuccess;
	private final HttpStatus httpStatus;
	private final String     message;
}

package com.example.api.global.response.validation;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class ValidationFail {
	
	private List<ValidationFailForField> fieldErrorList;
	private List<ValidationFailForObject> objectErrorList;

	public static  ValidationFail convert(BindingResult bindingResult) {

		List<ValidationFailForField> fieldErrorList = bindingResult.getFieldErrors()
															.stream()
															.map(fieldError -> ValidationFailForField.convert(fieldError))
															.collect(Collectors.toList());

		List<ValidationFailForObject> objectErrorList = bindingResult.getGlobalErrors()
															 .stream()
															 .map(objectError -> ValidationFailForObject.convert(objectError))
															 .collect(Collectors.toList());

		return ValidationFail.builder()
							 .fieldErrorList(fieldErrorList)
							 .objectErrorList(objectErrorList)
							 .build();
	}
}

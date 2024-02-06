package com.craft.demo.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class GenericResponse<T> {

    private HttpStatus status;

    private T body;

    private String message;
}

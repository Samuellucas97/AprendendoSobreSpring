package br.ufrn.imd.learningspringboot2.exception;

import lombok.experimental.SuperBuilder;
import lombok.Getter;

@SuperBuilder
@Getter
public class ValidationExceptionDetails extends ExceptionalDetails {
    private String fields;
    private String fieldsMessage;
}

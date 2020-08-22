package br.ufrn.imd.learningspringboot2.exception;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class ExceptionalDetails {
    protected String title;
    protected Integer status;
    protected String detail;
    protected LocalDateTime timestamp;
    protected String developerMessage;
}
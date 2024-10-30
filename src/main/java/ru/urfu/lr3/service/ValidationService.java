package ru.urfu.lr3.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.urfu.lr3.exception.UnsupportedCodeException;
import ru.urfu.lr3.exception.ValidationFailedException;
import ru.urfu.lr3.model.Request;

@Service
public interface ValidationService {
    void isValid(BindingResult bindingResult) throws ValidationFailedException;
    void isCodeValid(Request request) throws UnsupportedCodeException;
}

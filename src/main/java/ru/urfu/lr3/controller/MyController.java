package ru.urfu.lr3.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.urfu.lr3.exception.UnsupportedCodeException;
import ru.urfu.lr3.exception.ValidationFailedException;
import ru.urfu.lr3.model.*;
//import ru.urfu.lr3.service.ModifyRequestService;
import ru.urfu.lr3.service.ModifyResponseService;
import ru.urfu.lr3.service.ValidationService;
import ru.urfu.lr3.util.DateTimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;
//    private final ModifyRequestService modifyRequestService;


    @Autowired
    public MyController(ValidationService validationService,
                        @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifyResponseService){
//                        ModifyRequestService modifyRequestService) {
        this.validationService = validationService;
        this.modifyResponseService = modifyResponseService;
//        this.modifyRequestService = modifyRequestService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult) throws ParseException {

        log.info("45 request: {}", request);

        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();

        log.info("57 response: {}", response);

        var status = HttpStatus.OK;
        try {
            validationService.isCodeValid(request);
            validationService.isValid(bindingResult);
        } catch (ValidationFailedException e) {
            log.error("error: {}", e.toString());
            response.setCode(Codes.FAILURE);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            status = HttpStatus.BAD_REQUEST;
        } catch (UnsupportedCodeException e) {
            log.error("error: {}", e.toString());
            response.setCode(Codes.FAILURE);
            response.setErrorCode(ErrorCodes.UNSUPPORTED_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNSUPPORTED);
            status = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            log.error("error: {}", e.toString());
            response.setCode(Codes.FAILURE);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        modifyResponseService.modify(response);
//        modifyRequestService.modify(request);

        log.info("86 request: {}", request);
        SimpleDateFormat deltaTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date deltaTime = deltaTimeFormat.parse(request.getSystemTime());


        String resultTime = String.valueOf(new Date().getTime() - deltaTime.getTime());
        log.info("delta time, ms: {}", (resultTime));

        return new ResponseEntity<>(modifyResponseService.modify(response), status);
    }
}

package ru.urfu.lr3.service;

import org.springframework.stereotype.Service;
import ru.urfu.lr3.model.Response;

@Service
public interface ModifyResponseService {
    Response modify(Response response);
}

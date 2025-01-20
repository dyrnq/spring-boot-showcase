package com.dyrnq.sbs.common.controller;

import com.dyrnq.sbs.common.dto.RequestDTO;
import com.dyrnq.sbs.common.dto.RequestData;
import com.dyrnq.sbs.common.dto.ResponseDTO;
import com.dyrnq.sbs.common.service.BasicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class BasicController {

    @Autowired
    private BasicService basicService;

    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDTO getRequest() {
        log.info("Basic controller : getRequest method");
        return basicService.getMethod();
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDTO postRequest(@RequestBody RequestDTO<RequestData> requestDTO) {
        log.info("Basic controller : postRequest method");
        return basicService.postMethod(requestDTO);
    }
}

package com.dyrnq.sbs.common.service;

import com.dyrnq.sbs.common.dto.RequestDTO;
import com.dyrnq.sbs.common.dto.RequestData;
import com.dyrnq.sbs.common.dto.ResponseDTO;
import com.dyrnq.sbs.common.dto.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BasicService {

    public ResponseDTO getMethod(){
        ResponseDTO<ResponseData> response = new ResponseDTO<>();
        log.info("Basic service : getMethod method");
        ResponseData res = new ResponseData("get", 1);
        response.setMetaData("success");
        response.setData(res);
        return response;
    }

    public ResponseDTO postMethod(RequestDTO<RequestData> requestDTO){
        ResponseDTO<ResponseData> response = new ResponseDTO<>();
        log.info("Basic service : postMethod method");
        ResponseData res = new ResponseData(requestDTO.getData().getField1(), requestDTO.getData().getField2());
        response.setMetaData("success");
        response.setData(res);
        return response;
    }
}

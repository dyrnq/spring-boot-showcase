package com.dyrnq.sbs.common.controller;

import com.dyrnq.sbs.common.dto.ResponseDTO;
import com.dyrnq.sbs.common.model.UserModel;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseDTO<UserModel> add(@ModelAttribute UserModel userModel, HttpServletRequest request) {
        ResponseDTO<UserModel> responseDTO = new ResponseDTO<UserModel>();
        responseDTO.setMetaData(request.getHeader("Content-Type"));
        responseDTO.setData(userModel);
        return responseDTO;
    }


    @RequestMapping(value = "/add-body", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseDTO<UserModel> addBody(@RequestBody UserModel userModel, HttpServletRequest request) {
        ResponseDTO<UserModel> responseDTO = new ResponseDTO<UserModel>();
        responseDTO.setMetaData(request.getHeader("Content-Type"));
        responseDTO.setData(userModel);
        return responseDTO;
    }
}

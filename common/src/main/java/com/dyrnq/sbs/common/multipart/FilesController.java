package com.dyrnq.sbs.common.multipart;

import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.digest.DigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class FilesController {

    @Autowired
    FilesStorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            storageService.save(file);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @DeleteMapping("/files/{filename:.+}")
    public ResponseEntity<ResponseMessage> deleteFile(@PathVariable String filename) {
        String message = "";

        try {
            boolean existed = storageService.delete(filename);

            if (existed) {
                message = "Delete the file successfully: " + filename;
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            }

            message = "The file does not exist!";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not delete the file: " + filename + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }
    }

    @RequestMapping(value = "/upload-part", method = RequestMethod.POST, consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<String> uploadPart(
            @RequestPart(value = "token", required = false) String token,
            @RequestPart(value = "sign", required = false) String sign,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        // 处理上传的文件
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("文件未上传");
        }

        // 这里可以保存文件或进行其他处理
        String fileName = file.getOriginalFilename();
        // 示例：保存文件到某个路径
        File saveFile = new File("uploads/" + fileName);
        IoUtil.copy(file.getInputStream(),new FileOutputStream(saveFile));
        String sha256Hex = DigestUtil.sha256Hex(saveFile);

        return ResponseEntity.ok().body(String.format("上传成功: %s, Token: %s, Sign: %s, sha256Hex: %s\n", fileName, token, sign,sha256Hex));
    }

    @RequestMapping(value = "/upload-part-json", method = RequestMethod.POST, consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<String> uploadMixed(
            @RequestPart(value = "json", required = false) PartJson json,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        String token = json!=null?json.getToken():null;
        String sign = json!=null?json.getSign():null;

        // 处理上传的文件
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("文件未上传");
        }

        // 这里可以保存文件或进行其他处理
        String fileName = file.getOriginalFilename();
        // 示例：保存文件到某个路径
        File saveFile = new File("uploads/" + fileName);
        IoUtil.copy(file.getInputStream(),new FileOutputStream(saveFile));
        String sha256Hex = DigestUtil.sha256Hex(saveFile);

        return ResponseEntity.ok().body(String.format("上传成功: %s, Token: %s, Sign: %s, sha256Hex: %s\n", fileName, token, sign,sha256Hex));
    }

}

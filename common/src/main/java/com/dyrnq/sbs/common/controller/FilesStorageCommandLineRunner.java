package com.dyrnq.sbs.common.controller;

import com.dyrnq.sbs.common.multipart.FilesStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilesStorageCommandLineRunner implements CommandLineRunner {

    private final FilesStorageService filesStorageService;

    @Override
    public void run(String... args) throws Exception {
        filesStorageService.init();
    }
}

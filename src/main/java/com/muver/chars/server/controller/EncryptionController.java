package com.muver.chars.server.controller;

import com.muver.chars.server.model.EncryptionPackage;
import com.muver.chars.server.util.InvalidChecksumException;
import com.muver.chars.server.util.TooSmallContainerException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.muver.chars.server.util.CharEncoder;

@RestController
public class EncryptionController {

    @PostMapping("/encrypt")
    EncryptionPackage encrypt(@RequestBody EncryptionPackage data) {
        return CharEncoder.execute(data);
    }
}

package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
@RequestMapping("/admin/common")
@RestController
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;
    @PostMapping("/upload")
    public Result<String> uploadfile(MultipartFile file)  {
        log.info("上传文件:{}",file);
        //获取原文件名
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
         String objectname = UUID.randomUUID().toString() + substring;

        try {
            String url = aliOssUtil.upload(file.getBytes(), objectname);
            return Result.success(url);
        } catch (IOException e) {
            log.error("错误:{}",e);
        }
return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}

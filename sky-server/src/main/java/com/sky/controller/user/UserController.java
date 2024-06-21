package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    public Result<UserLoginVO> wxlogin(@RequestBody UserLoginDTO userLoginDTO){
        log.info("微信登录传过来的校验码：{}",userLoginDTO.getCode());
        User user= userService.wxlogin(userLoginDTO);
        //生成jwt令牌
        Map<String,Object> map=new HashMap<>();
        map.put(JwtClaimsConstant.USER_ID,user.getId());
        String jwt = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(),map);
        //将数据传进userloginvo返回给前端
        UserLoginVO userLoginVO=new UserLoginVO();
        userLoginVO.setId(user.getId());
        userLoginVO.setOpenid(user.getOpenid());
        userLoginVO.setToken(jwt);
        return Result.success(userLoginVO);


    }
}

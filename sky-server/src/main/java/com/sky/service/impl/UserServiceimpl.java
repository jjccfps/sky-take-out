package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceimpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
   private WeChatProperties weChatProperties;
    @Override
    public User wxlogin(UserLoginDTO userLoginDTO) {
    /*调用 auth.code2Session 接口  url:GET https://api.weixin.qq.com/sns/jscode2session
       换取 用户唯一标识 OpenID
       用户在微信开放平台账号下的唯一标识UnionID（若当前小程序已绑定到微信开放平台账号）
       和 会话密钥 session_key。*/
        final String URL="https://api.weixin.qq.com/sns/jscode2session";
        Map<String,String> map=new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",userLoginDTO.getCode());
        map.put("grant_type","authorization_code");
        //返回的json格式的字符串
        String s = HttpClientUtil.doGet(URL, map);
        //解析传过来的json字符串，拿到openid
        JSONObject jsonObject = JSON.parseObject(s);
        String openid = jsonObject.getString("openid");
        //获取到openid,判断是否为空，空就登陆失败
        if (openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //非空,登录成功判断用户是否已经存在，不存在就添加
        User user = userMapper.checkopenid(openid);

        //返回的user对象为空就不存在
        if (user==null){
            //添加用户
            user=User.builder().createTime(LocalDateTime.now()).openid(openid).build();
            userMapper.insertuser(user);
        }

        return user;
    }
}

package com.sky.aspet;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoAspect {
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autofillpointcut(){}


    @Before("autofillpointcut()")
    public void autofill(JoinPoint joinPoint){
      //获取是insert还是update方法
        MethodSignature signature =(MethodSignature) joinPoint.getSignature();//方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获得方法上的注解对象
        OperationType value = autoFill.value();//获得数据库上的操作类型

        //获得当前被拦截方法的参数-实体对象
        Object[] args = joinPoint.getArgs();
        if (args==null || args.length==0){
            return;
        }
        //参数默认在第一位
        Object arg = args[0];
        //准备赋值对象
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        //根据不同的操作类型来赋值，运用反射
        if (value==OperationType.INSERT){
            Class<?> aClass = arg.getClass();
            try {
                Method setcreattime = aClass.getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setupdatetime = aClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setcreatuser = aClass.getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setupdateuser = aClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setcreatuser.invoke(arg,currentId);
                setupdateuser.invoke(arg,currentId);
                setcreattime.invoke(arg,now);
                setupdatetime.invoke(arg,now);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (value==OperationType.UPDATE){
            Class<?> aClass = arg.getClass();
            try {
                Method setupdateuser = aClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                Method setupdatetime = aClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                setupdateuser.invoke(arg,currentId);
                setupdatetime.invoke(arg,now);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

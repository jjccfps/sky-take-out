package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Slf4j
public class AddressController {
    @Autowired
    private AddressService addressService;
    @PostMapping
    public Result insertaddress(@RequestBody AddressBook addressBook){
        log.info("增加地址:{}",addressBook);
        addressService.insertaddress(addressBook);
        return Result.success();
    }
    @GetMapping("/list")
    public Result<List<AddressBook>> listaddress(){
        List<AddressBook> list= addressService.listaddress();
        return Result.success(list);

    }
    @GetMapping("{id}")
    public Result<AddressBook> getbyid(@PathVariable Long id){
        log.info("根据id查询地址：{}",id);
        AddressBook addressBook= addressService.getbyid(id);
        return Result.success(addressBook);
    }
    @PutMapping("/default")
    public Result updatedefault(@RequestBody AddressBook addressBook){
        log.info("设置默认地址:{}",addressBook);
        addressService.updatedefault(addressBook);
        return Result.success();
    }
    @GetMapping("default")
    public Result<AddressBook> getdefault(){
      AddressBook addressBook=  addressService.getdefault();
      return Result.success(addressBook);
    }
    @PutMapping()
    public Result updateaddress(@RequestBody AddressBook addressBook){
        log.info("修改地址：{}",addressBook);
        addressService.updateaddress(addressBook);
        return Result.success();
    }
    @DeleteMapping
    public Result delete(Long id){
        log.info("删除:{}",id);
        addressService.delete(id);
        return Result.success();
    }


}

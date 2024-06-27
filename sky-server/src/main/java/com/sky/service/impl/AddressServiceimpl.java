package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressMapper;
import com.sky.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceimpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;
    @Override
    public void insertaddress(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(StatusConstant.DISABLE);
        addressMapper.insertaddress(addressBook);
    }

    @Override
    public List<AddressBook> listaddress() {
        return addressMapper.listaddress();
    }

    @Override
    public AddressBook getbyid(Long id) {
        return addressMapper.getbyid(id);
    }

    @Override
    public void updatedefault(AddressBook addressBook) {
        //先判断原来有没有默认地址，有就修改为不是默认，没有直接设置默认
        Integer enable = StatusConstant.ENABLE;
        AddressBook addressBook1= addressMapper.selectdefault(enable);
        //判断
        if (addressBook !=null){
            //修改为不是默认
            addressBook1.setIsDefault(StatusConstant.DISABLE);
            addressMapper.updatedefault(addressBook1);
        }
        addressBook.setIsDefault(enable);
        addressMapper.setdefault(addressBook);
    }

    @Override
    public AddressBook getdefault() {
        Integer enable = StatusConstant.ENABLE;
        return  addressMapper.getdefault(enable);
    }

    @Override
    public void updateaddress(AddressBook addressBook) {
        addressMapper.updateaddress(addressBook);

    }

    @Override
    public void delete(Long id) {
        addressMapper.delete(id);
    }
}

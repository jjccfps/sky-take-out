package com.sky.service;

import com.sky.entity.AddressBook;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressService {
    void insertaddress(AddressBook addressBook);

    List<AddressBook> listaddress();

    AddressBook getbyid(Long id);

    void updatedefault(AddressBook addressBook);

    AddressBook getdefault();

    void updateaddress(AddressBook addressBook);

    void delete(Long id);
}

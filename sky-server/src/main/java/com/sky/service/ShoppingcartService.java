package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShoppingcartService {
    List<ShoppingCart> getcart();

    void addcart(ShoppingCartDTO shoppingCartDTO);

    void cleanall();

    void delete(ShoppingCartDTO shoppingCartDTO);
}

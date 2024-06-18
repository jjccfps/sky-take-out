package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DishService {
    PageResult selectpage(DishPageQueryDTO dishPageQueryDTO);

    void insertdish(DishDTO dishDTO);
}

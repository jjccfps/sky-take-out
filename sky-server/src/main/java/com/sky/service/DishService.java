package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DishService {
    PageResult selectpage(DishPageQueryDTO dishPageQueryDTO);

    void insertdish(DishDTO dishDTO);

    DishVO insertbyid(Long id);

    List<Dish> selectbycategoryid(Long categoryId);

    void deletedish(Long[] ids);

    void updatadish(DishDTO dishDTO);

    void updatestatus(Integer status, Long id);

    List<DishVO> userselectbycategoryid(Long categoryId);

}

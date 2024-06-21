package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SetmealService {
    PageResult selectpage(SetmealPageQueryDTO setmealPageQueryDTO);


    SetmealVO selectbyid(Long id);

    void insertsetmeal(SetmealDTO setmealDTO);

    void updatestemal(SetmealVO setmealVO);

    void updatestatus(Integer status, Long id);

    void delete(String[] ids);

    List<Setmeal> selectsetmeal(Setmeal setmeal);

    List<DishItemVO> userselectsermealdish(Long id);
}

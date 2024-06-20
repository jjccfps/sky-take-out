package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;
import org.springframework.stereotype.Service;

@Service
public interface SetmealService {
    PageResult selectpage(SetmealPageQueryDTO setmealPageQueryDTO);


    SetmealVO selectbyid(Long id);

    void insertsetmeal(SetmealDTO setmealDTO);

    void updatestemal(SetmealVO setmealVO);

    void updatestatus(Integer status, Long id);

    void delete(String[] ids);
}

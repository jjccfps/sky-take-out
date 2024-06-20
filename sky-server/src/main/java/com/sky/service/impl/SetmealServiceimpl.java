package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.StemaldishMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceimpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private StemaldishMapper stemaldishMapper;
    @Override
    public PageResult selectpage(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page=(Page<SetmealVO>) setmealMapper.selectpage(setmealPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public SetmealVO selectbyid(Long id) {
        SetmealVO setmealVO = new SetmealVO();
        Setmeal setmeal = setmealMapper.setmealbyid(id);
        List<SetmealDish> setmealDishes= stemaldishMapper.setmealbyid(id);
         // !!!!!!!!!!!!!!!!!!!!!!!!!!
        setmealVO.setSetmealDishes(setmealDishes);

        BeanUtils.copyProperties(setmeal,setmealVO);
        return setmealVO;
    }
    @Transactional
    @Override
    public void insertsetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.insertsetmeal(setmeal);
        Long id = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(id);
            stemaldishMapper.insertsetmeal(setmealDish);
        }


    }

    @Override
    public void updatestemal(SetmealVO setmealVO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealVO,setmeal);
        setmealMapper.updatesetmeal(setmeal);
        List<SetmealDish> setmealDishes = setmealVO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            stemaldishMapper.updatesetmeal(setmealDish);
        }


    }

    @Override
    public void updatestatus(Integer status, Long id) {
        Setmeal setmeal = new Setmeal();
        setmeal.setId(id);
        setmeal.setStatus(status);
        setmealMapper.updatestatus(setmeal);
    }
@Transactional
    @Override
    public void delete(String[] ids) {
        //判断套餐是否起售，起售不能删除
        for (String id : ids) {
            Integer status= setmealMapper.selectbyid(id);
            if (status.equals(StatusConstant.ENABLE)){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }

        }
        //没在起售状态，删除套餐表，套餐菜品表
    setmealMapper.delete(ids);
        stemaldishMapper.delete(ids);

    }
}

package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.StemaldishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceimpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private StemaldishMapper stemaldishMapper;
    @Override
    public PageResult selectpage(DishPageQueryDTO dishPageQueryDTO) {
      PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
      List<DishVO> list=dishMapper.selectpage(dishPageQueryDTO);
      Page<DishVO> page=(Page<DishVO>) list;
      return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void insertdish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.insertdish(dish);
        Long id = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors !=null && flavors.size()>0){
            flavors.forEach(dishFlavor ->
                    dishFlavor.setDishId(id));
            dishFlavorMapper.insertflavors(flavors);
        }


    }

    @Override
    public DishVO insertbyid(Long id) {
        DishVO dishVO = new DishVO();
        Dish dish=  dishMapper.selectbyid(id);
       List<DishFlavor> flavors= dishFlavorMapper.selectbyid(id);
       BeanUtils.copyProperties(dish,dishVO);
       dishVO.setFlavors(flavors);
       return dishVO;
    }

    @Override
    public List<Dish> selectbycategoryid(Long categoryId) {
        List<Dish> dishes=dishMapper.selectbycategoryid(categoryId);
        return dishes;
    }
    @Transactional
    @Override
    public void deletedish(Long[] ids) {
       //判断要删除的菜品是不是停售中的
        for (Long id : ids) {
          int status=  dishMapper.getbyid(id);
          if (status>0){
              throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
          }
            //判断当前菜品有没有关联套餐，关联了就不能删除
            int setmealdishid=stemaldishMapper.getsetmealid(id);
            if (setmealdishid>0){
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }
        }
        //删除菜品，以及菜品对应口味表
        dishMapper.deletedish(ids);
        dishFlavorMapper.deletedishflavor(ids);



    }

    @Override
    public void updatadish(DishDTO dishDTO) {
        //根据id修改菜品信息，也要改对应菜品id的口味表信息

        //先修改菜品表信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.updatadish(dish);
        //修改对应菜品id的口味表信息
        DishFlavor dishFlavor = new DishFlavor();
        BeanUtils.copyProperties(dishDTO,dishFlavor);
        //设置dish_id
        Long id = dish.getId();
        dishFlavor.setDishId(id);
        dishFlavorMapper.updata(dishFlavor);
    }

    @Override
    public void updatestatus(Integer status, Long id) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        dishMapper.updatestatus(dish);

    }
}

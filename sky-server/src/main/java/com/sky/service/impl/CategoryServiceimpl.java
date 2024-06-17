package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryServicce;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceimpl implements CategoryServicce {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Override
    public PageResult selectclassify(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        Page<Category> page=categoryMapper.selectclassify(categoryPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void instert(CategoryDTO categoryDTO) {
        Category category=new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setStatus(StatusConstant.DISABLE);
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());
       categoryMapper.instert(category);

    }

    @Override
    public void delete(Long id) {
        //查询当前分类是否关联了菜品，如果关联了就抛出业务异常
        Integer count = dishMapper.countByCategoryId(id);
        if(count > 0){
            //当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        //查询当前分类是否关联了套餐，如果关联了就抛出业务异常
        count = setmealMapper.countByCategoryId(id);
        if(count > 0){
            //当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
         categoryMapper.delete(id);

    }

    @Override
    public List<Category> selecttype(Integer type) {
        List<Category> list=categoryMapper.selecttype(type);
        return   list;
    }

    @Override
    public void updatastatus(Integer status,Long id) {
        Category category = new Category();
        category.setId(id);
        category.setStatus(status);
        category.setUpdateUser(BaseContext.getCurrentId());
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.updatastatus(category);
    }

    @Override
    public void updatatype(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setUpdateUser(BaseContext.getCurrentId());
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.updatatype(category);
    }
}

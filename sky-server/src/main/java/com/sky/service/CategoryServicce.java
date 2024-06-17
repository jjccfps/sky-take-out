package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryServicce {
    PageResult selectclassify(CategoryPageQueryDTO categoryPageQueryDTO);

    void instert(CategoryDTO categoryDTO);

    void delete(Long id);

    List<Category> selecttype(Integer type);
}

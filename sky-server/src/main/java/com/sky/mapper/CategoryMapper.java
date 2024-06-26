package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    Page<Category> selectclassify(CategoryPageQueryDTO categoryPageQueryDTO);
@Insert("insert into category (type, name, sort, status, create_time, update_time, create_user, update_user)" +
        "values (#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser}) ")
    void instert(Category category);
@Delete("delete from category where id=#{id}")
    void delete(Long id);

    List<Category> selecttype(Integer type);

    void updatastatus(Category category);

    void updatatype(Category category);
}

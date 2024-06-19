package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

   List<DishVO> selectpage(DishPageQueryDTO dishPageQueryDTO);

   @AutoFill(value = OperationType.INSERT)

    void insertdish(Dish dish);
@Select("select *from dish where id=#{id}")
    Dish selectbyid(Long id);
@Select("select *from dish where category_id=#{categoryId}")
    List<Dish> selectbycategoryid(Long categoryId);

@Select("select count(*) from dish where id=#{id} and status=1 ")
    int getbyid(Long id);

    void deletedish(Long[] ids);
@AutoFill(value = OperationType.UPDATE)
    void updatadish(Dish dish);
@AutoFill(value = OperationType.UPDATE)
@Update("update dish set status=#{status} where id=#{id}")
    void updatestatus(Dish dish);
}

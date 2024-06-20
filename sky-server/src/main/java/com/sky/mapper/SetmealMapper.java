package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    Page<SetmealVO> selectpage(SetmealPageQueryDTO setmealPageQueryDTO);
    @Select("select * from setmeal where id=#{id}")
    Setmeal setmealbyid(Long id);
    @AutoFill(value = OperationType.INSERT)
    void insertsetmeal(Setmeal setmeal);
@AutoFill(value = OperationType.UPDATE)
    void updatesetmeal(Setmeal setmeal);
@AutoFill(value = OperationType.UPDATE)
@Update("update setmeal set status=#{status} where id=#{id}")
    void updatestatus(Setmeal setmeal);
@Select("select status from setmeal where id=#{id}")
    Integer selectbyid(String id);

    void delete(String[] ids);
}

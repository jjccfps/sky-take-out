package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressMapper {
    @Insert("insert into address_book ( user_id, consignee, sex, phone, province_code, province_name, city_code, city_name, district_code, district_name, detail, label, is_default)" +
            "values (#{userId},#{consignee},#{sex},#{phone},#{provinceCode},#{provinceName},#{cityCode},#{cityName},#{districtCode},#{districtName},#{detail},#{label},#{isDefault})")
    void insertaddress(AddressBook addressBook);
@Select("select *from address_book")
    List<AddressBook> listaddress();
@Select("select *from address_book where id=#{id}")
    AddressBook getbyid(Long id);
@Select("select * from address_book where is_default=#{isDefault}")
    AddressBook selectdefault(Integer isDefault);
@Update("update address_book set is_default=#{isDefault} where id=#{id} ")
    void updatedefault(AddressBook addressBook);
@Update("update address_book set is_default=#{isDefault} where id=#{id}")
    void setdefault(AddressBook addressBook);
@Select("select * from address_book where is_default=#{isDefault}")
    AddressBook getdefault(Integer isDefault);
@Update("update address_book set consignee=#{consignee}, sex=#{sex}, phone=#{phone}, province_code=#{provinceCode},province_name=#{provinceName},city_code=#{cityCode},city_name=#{cityName},district_code=#{districtCode},district_name=#{districtName},detail=#{detail},label=#{label} where id=#{id} ")
    void updateaddress(AddressBook addressBook);
@Delete("delete from address_book where id=#{id}")
    void delete(Long id);
}

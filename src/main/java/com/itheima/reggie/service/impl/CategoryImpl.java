package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除分类，查询分类是否关联菜品、套餐
     * @param ids
     */
    @Override
    public void remove(Long ids) {
        LambdaQueryWrapper<Dish> queryWrapper1 = new LambdaQueryWrapper<>();

        queryWrapper1.eq(Dish::getCategoryId,ids);

        int count1 = (int) dishService.count(queryWrapper1);

        if(count1 > 0){
            //抛出异常
            throw new CustomException("关联菜品，不能删除");
        }

        LambdaQueryWrapper<Setmeal> queryWrapper2 = new LambdaQueryWrapper<>();

        queryWrapper2.eq(Setmeal::getCategoryId,ids);

        int count2 = (int) setmealService.count(queryWrapper2);

        if(count2 > 0){
            //抛出异常
            throw new CustomException("关联套餐，不能删除");
        }

        super.removeById(ids);

    }
}

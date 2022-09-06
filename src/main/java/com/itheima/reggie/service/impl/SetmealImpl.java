package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.ws.Action;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    @Transactional //事务管理注解
    public void saveWithDish(SetmealDto setmealDto) {
        //setmeal表格操作
        this.save(setmealDto);

        //lambda表达式,利用dto获取setmeal_dish的id
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map(items->{
            items.setDishId(setmealDto.getId());
            return items;
        }).collect(Collectors.toList());

        //setmeal_dish表格操作
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //selete count(*) from setmeal where id in (1,2,3) and status = 1;
        //停售状态才能删除，查询套餐状态
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        long count = this.count(queryWrapper);

        if(count > 0){
            throw new CustomException("套餐正在售卖，不能删除");
        }

        //删除套餐表setmeal数据
        this.removeBatchByIds(ids);

        //删除关系表setmeal_dish的数据
        //delete from setmeal_dish where setmeal_id in (1,2,3)
//        setmealDishService.removeBatchByIds() ids是setmeal的主键，不是setmeal_dish
        LambdaQueryWrapper<SetmealDish> queryWrapper2 = new LambdaQueryWrapper();
        queryWrapper2.in(SetmealDish::getSetmealId,ids);
        setmealDishService.removeById(queryWrapper);

    }
}

package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.*;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class ReportServiceimpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        //将要找的天数放到集合中
        List<LocalDate> localDates=new ArrayList<>();
        localDates.add(begin);
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            localDates.add(begin);
        }
        String listdate = StringUtils.join(localDates, ",");
        //将要找的开始结束时间换成具体的localdatetime
        List<Double> amounts=new ArrayList<>();
        Map  map=new HashMap();
        for (LocalDate date : localDates) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            map.clear();
            map.put("beginTime",beginTime);
            map.put("endTime",endTime);
            map.put("status",Orders.COMPLETED);
           Double amount = reportMapper.turnoverStatistics(map);
           amounts.add(amount);
        }
        String listamount = StringUtils.join(amounts, ",");
        TurnoverReportVO turnoverReportVO = new TurnoverReportVO();
        turnoverReportVO.setDateList(listdate);
        turnoverReportVO.setTurnoverList(listamount);
        return turnoverReportVO;

    }

    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        //将要找的天数放到集合中
        List<LocalDate> localDates=new ArrayList<>();
        localDates.add(begin);
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            localDates.add(begin);
        }
        String listdate = StringUtils.join(localDates, ",");
        //查询用户总量
        //将要找的开始结束时间换成具体的localdatetime
        List<Integer> newUserList=new ArrayList<>();
        List<Integer> amounts=new ArrayList<>();
        Map  map=new HashMap();
        for (LocalDate date : localDates) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            map.clear();
            map.put("beginTime",beginTime);
            map.put("endTime",endTime);

            Integer usercount=reportMapper.userStatistics(map);
            Integer newuser = reportMapper.newuser(map);
            amounts.add(usercount);
            newUserList.add(newuser);
        }
        String useramounts = StringUtils.join(amounts, ",");
        String newuserlist = StringUtils.join(newUserList, ",");
        UserReportVO userReportVO = new UserReportVO();
        userReportVO.setNewUserList(newuserlist);
        userReportVO.setTotalUserList(useramounts);
        userReportVO.setDateList(listdate);
    return userReportVO;
    }

    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        OrderReportVO orderReportVO = new OrderReportVO();
        //将要找的天数放到集合中
        List<LocalDate> localDates=new ArrayList<>();
        localDates.add(begin);
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            localDates.add(begin);
        }
        String listdate = StringUtils.join(localDates, ",");
        //查询每日订单总数，有效数
        //将要找的开始结束时间换成具体的localdatetime
        List<Integer> newUserList=new ArrayList<>();
        List<Integer> amounts=new ArrayList<>();
        Map  map=new HashMap();

        for (LocalDate date : localDates) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            map.clear();

            map.put("beginTime",beginTime);
            map.put("endTime",endTime);
            map.put("status",Orders.COMPLETED);

            Integer  dateordertoatl=reportMapper.dateordertoatl(map);
            Integer dateordercomplete = reportMapper.dateordercomplete(map);
            amounts.add(dateordertoatl);
            newUserList.add(dateordercomplete);
        }
        String useramounts = StringUtils.join(amounts, ",");
        String newuserlist = StringUtils.join(newUserList, ",");
        orderReportVO.setValidOrderCountList(newuserlist);
        orderReportVO.setOrderCountList(useramounts);
        orderReportVO.setDateList(listdate);
        //总订单
        Integer totals=reportMapper.tatalorders();
            orderReportVO.setTotalOrderCount(totals);
        int total = totals.intValue();
        Integer totalcompletes=reportMapper.totalordercomplete(Orders.COMPLETED);
            orderReportVO.setValidOrderCount(totalcompletes);
        int totalcomplete = totalcompletes.intValue();


        double ordercoplete=totalcomplete / total;
        Double aDouble = new Double(ordercoplete);
        orderReportVO.setOrderCompletionRate(aDouble);
        return orderReportVO;
    }

    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {
        SalesTop10ReportVO salesTop10ReportVO = new SalesTop10ReportVO();

        List<LocalDate> localDates = new ArrayList<>();

        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> salesDTOList=reportMapper.top10(beginTime,endTime);
        List<String> keylist=new ArrayList<>();
        List<Integer> valuelist=new ArrayList<>();
        for (GoodsSalesDTO goodsSalesDTO : salesDTOList) {
            keylist.add(goodsSalesDTO.getName());
            valuelist.add(goodsSalesDTO.getNumber());
        }
        String name = StringUtils.join(keylist, ",");
        String value = StringUtils.join(valuelist, ",");
        salesTop10ReportVO.setNumberList(value);
        salesTop10ReportVO.setNameList(name);
        return salesTop10ReportVO;
      /*  Map<String, Integer> map = reportMapper.top10(beginTime, endTime);
        Set<Map.Entry<String, Integer>> entrySet = map.entrySet();
        List<Map.Entry<String, Integer>> list = new ArrayList<>(entrySet);
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }
        });
        List<String> keylist=new ArrayList<>();
        List<Integer> valuelist=new ArrayList<>();
        for (Map.Entry<String, Integer> entry : entrySet) {
            Integer value = entry.getValue();
            String key = entry.getKey();
            valuelist.add(value);
            keylist.add(key);
        }
        if (keylist.size()>10){
            List<String> stringList = keylist.subList(0, 10);
            String string = stringList.toString();
            salesTop10ReportVO.setNameList(string);
            String string1 = valuelist.subList(0, 10).toString();
            salesTop10ReportVO.setNumberList(string1);
            return salesTop10ReportVO;
        }
        String name = StringUtils.join(keylist, ",");
        String value = StringUtils.join(valuelist, ",");
        salesTop10ReportVO.setNumberList(value);
        salesTop10ReportVO.setNameList(name);
        return salesTop10ReportVO;
*/

    }
}

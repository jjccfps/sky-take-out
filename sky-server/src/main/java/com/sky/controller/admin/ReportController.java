package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/report")
@Slf4j
public class ReportController {
    @Autowired
    private ReportService reportService;
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO>  turnoverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("营业额统计接口:{},{}",begin,end);
        TurnoverReportVO turnoverReportVO=  reportService.turnoverStatistics(begin,end);
        return  Result.success(turnoverReportVO);
    }
    @GetMapping("/userStatistics")
    public Result<UserReportVO> userStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("用户统计接口:{},{}",begin,end);
        UserReportVO userReportVO = reportService.userStatistics(begin,end);
        return  Result.success(userReportVO);
    }
    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> ordersStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("订单统计接口:{},{}",begin,end);
        OrderReportVO orderStatisticsVO =  reportService.ordersStatistics(begin,end);
        return  Result.success(orderStatisticsVO);
    }
    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> top10(
                                              @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                              @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("top10:{},{}",begin,end);
        SalesTop10ReportVO salesTop10ReportVO=reportService.top10(begin,end);
        return Result.success(salesTop10ReportVO);
    }
}

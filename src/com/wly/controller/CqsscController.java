package com.wly.controller;

import com.wly.service.CqsscService;
import com.wly.utils.FileOperate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
@Scope(value = "prototype")
public class CqsscController extends BaseController{

    @Resource
    private CqsscService cqsscService;

    public String synchronize() {
        String day = request.getParameter("day");
        return null;
    }

    public String haoma() {
        System.out.println(FileOperate.getRootPath("saiy"));
//        cqsscService.reqHaoMa(5);
        return null;
    }

    public static void main(String[] args) {
    }
}

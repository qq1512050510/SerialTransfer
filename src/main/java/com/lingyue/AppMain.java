package com.lingyue;

import com.lingyue.serial.SerialComUtils;
import com.lingyue.util.StringComUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author PF14EBBQ
 */
@SpringBootApplication
@RestController
@Api("星芒灯管")
public class AppMain {

    @Autowired
    private SerialComUtils comUtils;

    @PostConstruct
    private void init() {
    }

    private static void startExplorer() {
        try {
            /*指定自己项目的路径*/
            Runtime.getRuntime().exec("cmd /c \"C:\\Program Files\\Internet Explorer\\iexplore.exe\" -k http://localhost:8097/swagger-ui.html");
            //Runtime.getRuntime().exec("cmd /c \"C:\\Program Files\\Mozilla Firefox\\firefox.exe\" -k http://localhost:8099/index.html");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) {
        SpringApplication.run(AppMain.class, args);
        startExplorer();
    }

    @ApiOperation("发送数据16进制数据")
    @RequestMapping(value = {"/sendContentHexToCom/{content}"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET, org.springframework.web.bind.annotation.RequestMethod.POST})
    public Map sendContentHexToCom(@PathVariable("content") String content) {
        Map resultMap = new HashMap();
        byte[] hexString = StringComUtils.hexStringToBinaryStr(content);
        //comUtils.sendHexMsg(hexString);
        comUtils.sendHexMsg(content);
        resultMap.put("content", content);
        resultMap.put("msg", "发送成功");
        return resultMap;
    }

    @ApiOperation("接收数据")
    @RequestMapping(value = {"/receiveContentToCom"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Map receiveContentFromCom() {
        Map resultMap = new HashMap();
        resultMap.put("msg", this.comUtils.getMsg());
        return resultMap;
    }
}
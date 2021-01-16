package com.lingyue;

import com.lingyue.serial.SerialComUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author PF14EBBQ
 */
@SpringBootApplication
@RestController
public class AppMain {

    @Autowired
    private SerialComUtils comUtils;

    @PostConstruct
    private void init() {
    }

    private static void startExplorer() {
        try {
            /*指定自己项目的路径*/
            //Runtime.getRuntime().exec("cmd /c \"C:\\Program Files\\Internet Explorer\\iexplore.exe\" -k http://localhost:8099/index.html");
            Runtime.getRuntime().exec("cmd /c \"C:\\Program Files\\Mozilla Firefox\\firefox.exe\" -k http://localhost:8099/index.html");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) {
        SpringApplication.run(AppMain.class, args);
        startExplorer();
    }

    @RequestMapping(value = "/sendContentToCom/{content}/{com}", method = {RequestMethod.GET, RequestMethod.POST})
    public Map sendContentToCom(@PathVariable(value = "content") String content, @PathVariable(value = "com") String comID) {
        Map<String, Object> resultMap = new HashMap<>();
        comUtils.sendMsg(content);
        return resultMap;
    }

    @RequestMapping(value = "/receiveContentToCom/{content}/{com}", method = {RequestMethod.GET, RequestMethod.POST})
    public Map receiveContentFromCom(@PathVariable(value = "content") String content, @PathVariable(value = "com") String comID) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("msg", comUtils.getMsg());
        return resultMap;
    }

}

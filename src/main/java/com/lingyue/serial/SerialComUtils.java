package com.lingyue.serial;

import gnu.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author PF14EBBQ
 * @description SerialPortEventListener
 */
@Component
public class SerialComUtils extends Thread implements SerialPortEventListener {

    //@Value("${serial.portId:COM1}")
    @Value("${serial.portId}")
    private String portIdString = "COM6";

    private final static Logger log = LoggerFactory.getLogger(SerialComUtils.class);
    /**
     * 监听器,我的理解是独立开辟一个线程监听串口数据 串口通信管理类
     */
    static CommPortIdentifier portId;
    /**
     * 有效连接上的端口的枚举
     */
    static Enumeration<?> portList;
    /**
     * 从串口来的输入流
     */
    InputStream inputStream;
    /**
     * 向串口输出的流
     */
    static OutputStream outputStream;
    /**
     * 串口的引用
     */
    static SerialPort serialPort;
    /**
     * 堵塞队列用来存放读到的数据
     */
    private BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>();


    public void setPortIdString(String portIdString) {
        this.portIdString = portIdString;
    }

    @Override
    /**
     * SerialPort EventListene 的方法,持续监听端口上是否有数据流
     */
    public void serialEvent(SerialPortEvent event) {

        switch (event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            // 当有可用数据时读取数据
            case SerialPortEvent.DATA_AVAILABLE:
                byte[] readBuffer = new byte[1023];
                try {
                    int numBytes = -1;
                    while (inputStream.available() > 0) {
                        numBytes = inputStream.read(readBuffer);

                        if (numBytes > 0) {
                            msgQueue.add(new Date() + "真实收到的数据为：-----"
                                    + new String(readBuffer, StandardCharsets.UTF_8).trim());
                            // 重新构造缓冲对象，否则有可能会影响接下来接收的数据
                            readBuffer = new byte[1023];
                        } else {
                            msgQueue.add("额------没有读到数据");
                        }
                    }
                } catch (IOException e) {
                }
                break;
        }
    }

    /**
     * 通过程序打开COM1串口，设置监听器以及相关的参数
     *
     * @return 返回1 表示端口打开成功，返回 0表示端口打开失败
     */
    @PostConstruct
    public int startComPort() {
        log.info("端口{}初始化", portIdString);

        // 通过串口通信管理类获得当前连接上的串口列表
        portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {

            // 获取相应串口对象
            portId = (CommPortIdentifier) portList.nextElement();

            System.out.println("设备类型：--->" + portId.getPortType());
            System.out.println("设备名称：---->" + portId.getName());
            System.out.println("当前端口的对象或应用程序：---->" + portId.getCurrentOwner());
            // 判断端口类型是否为串口
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                // 判断如果COM1串口存在，就打开该串口
                if (portId.getName().equals(portIdString)) {
                    try {

                        //TODO:判断接口是否占用
                        System.out.println(portId.isCurrentlyOwned());
                        System.out.println(portId);
                        // 打开串口名字为COM_1(名字任意),延迟为2毫秒
                        serialPort = (SerialPort) portId.open(portIdString, 2000);
                        // 设置当前串口的输入输出流
                        inputStream = serialPort.getInputStream();
                        outputStream = serialPort.getOutputStream();
                        // 给当前串口添加一个监听器
                        serialPort.addEventListener(this);
                        // 设置监听器生效，即：当有数据时通知
                        serialPort.notifyOnDataAvailable(true);

                        // 设置串口的一些读写参数
                        //波特率,数据位,停止位,奇偶检验
                        /*serialPort.setSerialPortParams(9600,
                                SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);*/
                        serialPort.setSerialPortParams(115200,
                                SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);
                    } catch (PortInUseException e) {
                        e.printStackTrace();
                        return 0;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return 0;
                    } catch (TooManyListenersException e) {
                        e.printStackTrace();
                        return 0;
                    } catch (UnsupportedCommOperationException e) {
                        e.printStackTrace();
                        return 0;
                    } finally {
                        System.out.println("finally");
                    }

                    return 1;
                }
            }
        }
        return 0;
    }

    private SerialPortBean createSerialPort(SerialPort serialPort, InputStream inputStream, OutputStream outputStream) {
        return null;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            System.out.println("--------------任务处理线程运行了--------------");
            while (true) {
                // 如果堵塞队列中存在数据就将其输出
                if (msgQueue.size() > 0) {
                    System.out.println(msgQueue.take());
                }
                sleep(1000);
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getMsg() {
        String returnS = "";
        // 如果堵塞队列中存在数据就将其输出
        if (msgQueue.size() > 0) {
            try {
                returnS = msgQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info(returnS);

        }
        return returnS;
    }

    public String sendMsg(String msg) {
        try {
            System.out.println("发出字节数：" + msg.getBytes("UTF-8").length);
            outputStream.write(msg.getBytes("UTF-8"), 0,
                    msg.getBytes("UTF-8").length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public static void main(String[] args) {
        for (int j = 3; j < 5; j++) {
            SerialComUtils cRead = new SerialComUtils();
            cRead.setPortIdString("COM" + j);
            int i = cRead.startComPort();
            if (i == 1) {
                // 启动线程来处理收到的数据
                cRead.start();
                try {
                    String st = "哈哈----你好";
                    System.out.println("发出字节数：" + st.getBytes("UTF-8").length);
                    outputStream.write(st.getBytes("UTF-8"), 0,
                            st.getBytes("UTF-8").length);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                return;
            }
        }

    }
}
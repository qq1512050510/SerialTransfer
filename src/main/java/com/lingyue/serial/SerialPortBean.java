package com.lingyue.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 端口实例
 *
 * @author PF14EBBQ
 */
public class SerialPortBean {


    private String portIdString;

    /**
     * 监听器,我的理解是独立开辟一个线程监听串口数据 串口通信管理类
     */
    private CommPortIdentifier portId;
    /**
     * 有效连接上的端口的枚举
     */
    private Enumeration<?> portList;
    /**
     * 从串口来的输入流
     */
    private InputStream inputStream;
    /**
     * 向串口输出的流
     */
    private OutputStream outputStream;
    /**
     * 串口的引用
     */
    private SerialPort serialPort;
    /**
     * 堵塞队列用来存放读到的数据
     */
    private BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>();

    public String getPortIdString() {
        return portIdString;
    }

    public void setPortIdString(String portIdString) {
        this.portIdString = portIdString;
    }

    public CommPortIdentifier getPortId() {
        return portId;
    }

    public void setPortId(CommPortIdentifier portId) {
        this.portId = portId;
    }

    public Enumeration<?> getPortList() {
        return portList;
    }

    public void setPortList(Enumeration<?> portList) {
        this.portList = portList;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }

    public void setSerialPort(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    public BlockingQueue<String> getMsgQueue() {
        return msgQueue;
    }

    public void setMsgQueue(BlockingQueue<String> msgQueue) {
        this.msgQueue = msgQueue;
    }
}

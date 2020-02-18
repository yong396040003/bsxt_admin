package com.yong.bsxt_admin.jutil;

import com.yong.bsxt_admin.pojo.Bsxt;
import com.yong.bsxt_admin.service.serviceImpl.BsxtServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Description:
 * Date: 18:19 2019/8/20
 *
 * @author yong
 * @see
 */
public class ReadWriteFile {
    private static Logger logger = LoggerFactory.getLogger(BsxtServiceImpl.class);

    /**
     * 从文件夹里读取设计要求和目录
     *
     * @param bsxt
     * @return
     */
    public static void readRequireAndBody(Bsxt bsxt) {
        String path = bsxt.getRequire();
        File file = new File(path);
        if (!file.exists()) {
            logger.warn("文件不存在");
        }
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String str;
            StringBuilder stringBuilder = new StringBuilder();
            while ((str = bufferedReader.readLine()) != null) {
                stringBuilder.append(str).append("\n");
            }

            String[] body = stringBuilder.toString().split("yyyyyy");
            bsxt.setRequire(body[0]);
            bsxt.setBody(body[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将简介写到文件里
     *
     * @param bsxt
     */
    public static void writeRequireAndBody(Bsxt bsxt) {
        //设置简介
        //将字符串写到文件里
        try {
            File dirFile = new File(ResourcePath.resourcePath + "/" + bsxt.getSetTeacherNum());
            if (!dirFile.exists()) {
                try {
                    //创建书的目录
                    dirFile.mkdirs();
                } catch (Exception e) {
                    logger.error("创建文件目录失败");
                }
            }
            File file = new File(ResourcePath.resourcePath + "/" + bsxt.getSetTeacherNum() + "/" + bsxt.getCode().substring(0,6) + ".txt");
            //如果文件不存在就创建文件
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStream outputStream = new FileOutputStream(file);
            //任务书和任务要求以\r\n隔开
            String body = bsxt.getRequire() + "yyyyyy" + bsxt.getBody();
            outputStream.write(body.getBytes());
            outputStream.close();

            bsxt.setRequire(ResourcePath.resourcePath + "/" + bsxt.getSetTeacherNum() + "/" + bsxt.getCode().substring(0,6) + ".txt");
            bsxt.setBody(ResourcePath.resourcePath + "/" + bsxt.getSetTeacherNum() + "/" + bsxt.getCode().substring(0,6) + ".txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

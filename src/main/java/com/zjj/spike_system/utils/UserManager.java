package com.zjj.spike_system.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjj.spike_system.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class UserManager {

    /**
     * 添加用户
     * @param count
     * @throws Exception
     */
    public static void addUser(int count) throws Exception {
        List<User> users = new ArrayList<User>(count);
        for (int i=1; i<=count; i++){
            User user = new User();
            user.setId(222L+ i);
            user.setNickname("user"+i);
            user.setSlat("123abc");
            user.setPassword(MD5Util.inputPassToDBPass("123456", user.getSlat()));
            user.setLogincount(1);
            users.add(user);
        }
        log.info("=========用户创建完成");
        Connection conn = getConnection();
        String sql = "insert into spike_system_user (id,nickname,password,slat,logincount) values(?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i=0; i<users.size(); i++){
            User user = users.get(i);
            ps.setLong(1, user.getId());
            ps.setString(2, user.getNickname());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getSlat());
            ps.setInt(5, user.getLogincount());
            // 添加批量
            ps.addBatch();
        }
        ps.executeBatch();
        ps.clearParameters();
        conn.close();
        log.info("=========插入数据完成");
        getToken(users);
        log.info("=========token生成完成");


    }

    /**
     * 生成token并写入文件
     * @param users
     * @throws Exception
     */
    public static void getToken(List<User> users) throws Exception {
        String loginUrl = "http://localhost:8001/spike_system/user/login";
        File file = new File("C:\\Users\\18264\\Desktop\\config.txt");
        if (file.exists())
            file.delete();
        RandomAccessFile rw = new RandomAccessFile(file, "rw");
        rw.seek(0);
        for(int i=0; i<users.size(); i++){
            User user = users.get(i);
            String s = doPostJson(loginUrl, "{\n" +
                    "    \"nickname\": \""+user.getNickname()+"\",\n" +
                    "    \"password\": \"123456\"\n" +
                    "}");

            String row = user.getNickname() + "," + s;
            rw.seek(rw.length());
            rw.write(row.getBytes());
            rw.write("\r\n".getBytes());
        }
    }

    /**
     * 发送post 请求
     * @param url 请求地址
     * @param json 请求参数
     * @return
     */
    public static String doPostJson(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            Header[] headers = response.getHeaders("Set-Cookie");
            for (Header h:headers) {
                HeaderElement[] elements = h.getElements();
                for (HeaderElement e : elements)
                    if ("token".equals(e.getName()))
                        resultString = e.getValue();
            }
//            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return resultString;
    }

    // 获取连接
    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://192.168.1.117:3306/spike_system?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
        String name = "root";
        String pwd = "123456";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(url, name, pwd);
        return connection;
    }

    // main方法执行
    public static void main(String[] args) throws Exception {
        addUser(2);
    }
}

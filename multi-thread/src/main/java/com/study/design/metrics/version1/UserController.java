package com.study.design.metrics.version1;

import com.study.design.metrics.version1.entity.UserVo;

import java.util.concurrent.TimeUnit;

/**
 * 说明:
 *
 * @USER: Cgh
 * @DATE: 2021/12/6
 */
public class UserController {

    private Metrics metrics = new Metrics();

    public UserController() {
        metrics.startRepeatedReport(60, TimeUnit.SECONDS);
    }

    public void register(UserVo user){
        long startTime = System.currentTimeMillis();
        metrics.recordTimeStamp("register", startTime);

        long respTime = System.currentTimeMillis() - startTime;
        metrics.recordResponseTime("register", respTime);
    }



    public UserVo login(String telephone, String password){
        long startTime = System.currentTimeMillis();
        metrics.recordTimeStamp("login", startTime);
        UserVo userVo = new UserVo();
        long respTime = System.currentTimeMillis() - startTime;
        metrics.recordResponseTime("login", respTime);

        return userVo;
    }
}

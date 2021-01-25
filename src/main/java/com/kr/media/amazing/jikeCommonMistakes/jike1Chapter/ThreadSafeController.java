package com.kr.media.amazing.jikeCommonMistakes.jike1Chapter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: caozhenlong
 * @Date: 2021-01-23
 * @Description:
 */
@RestController
@RequestMapping("/api")
public class ThreadSafeController {

    private ThreadLocal<Integer> currentUser = ThreadLocal.withInitial(() -> null);

    /**
     * 程序运行在 Tomcat 中，执行程序的线程是 Tomcat 的工作线程，而 Tomcat 的工作线程是基于线程池的。
     * 顾名思义，线程池会重用固定的几个线程，一旦线程重用，那么很可能首次从 ThreadLocal 获取的值是之前其他用户的请求遗留的值。这时，ThreadLocal 中的用户信息就是其他用户的信息
     * <p>
     * 设置一下 Tomcat 的参数，把工作线程池最大线程数设置为 1，这样始终是同一个线程在处理请求
     * <p>
     * 解决：使用类似 ThreadLocal 工具来存放一些数据时，需要特别注意在代码运行完后，显式地去清空设置的数据。
     *
     * @param userId
     * @return
     */
    @GetMapping("threadLocal/test")
    public Map wrong(@RequestParam("userId") Integer userId) {
        // 设置用户信息之前先查询一次 ThreadLocal 中的用户信息
        String before = Thread.currentThread().getName() + ":" + currentUser.get();
        // 设置用户信息到 ThreadLocal
        currentUser.set(userId);

        try {
            // 设置用户信息之后再查询一次 ThreadLocal 中的用户信息
            String after = Thread.currentThread().getName() + ":" + currentUser.get();

            //汇总输出两次查询结果
            Map<String, String> result = new HashMap<>();
            result.put("before", before);
            result.put("after", after);
            return result;
        } finally {
            currentUser.remove();
        }

    }




}

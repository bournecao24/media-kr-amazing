package com.kr.media.amazing.jikeCommonMistakes.jike1Chapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * 有一个含 900 个元素的 Map，现在再补充 100 个元素进去，这个补充操作由 10 个线程并发进行
 *
 *
 * @Author: caozhenlong
 * @Date: 2021-01-23
 * @Description:
 */

@RestController
@RequestMapping("/api")
@Slf4j
public class ConcurrentHashMpSafe {


    private static int THREAD_COUNT = 10;

    private static int ITEM_COUNT = 1000;


    /**
     * 在每一个线程的代码逻辑中先通过 size 方法拿到当前元素数量，计算 ConcurrentHashMap 目前还需要补充多少元素，并在日志中输出了这个值，然后通过 putAll 方法把缺少的元素添加进去
     *
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/concurrentMap/wrong")
    public String wrong() throws InterruptedException {

        // 初始 900 个元素
        ConcurrentHashMap<String, Long> concurrentHashMap = getData(ITEM_COUNT - 10);
        log.info("init size:{}", concurrentHashMap.size());

        // 使用线程池并发处理逻辑
        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, 10).parallel().forEach(i -> {

            // 查询还需要补充多少个元素
            int gap = ITEM_COUNT - concurrentHashMap.size();
            log.info("gap ={}", gap);
            //补充元素
            concurrentHashMap.putAll(getData(gap));
        }));

        // 等待所有任务完成
        forkJoinPool.shutdown();
        // 阻塞直到所有任务都在执行完之后执行关闭请求
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        // 最后元素个数会是1000吗？
        log.info("finish size:{}", concurrentHashMap.size());
        return "OK";
    }

    /**
     *  解决方法：整段逻辑加锁
     *
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/concurrentMap/right")
    public String right() throws InterruptedException {

        ConcurrentHashMap<String, Long> concurrentHashMap = getData(ITEM_COUNT - 10);
        log.info("init size:{}", concurrentHashMap.size());

        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, 10).parallel().forEach(i -> {
            synchronized (concurrentHashMap){
                // 查询还需要补充多少个元素
                int gap = ITEM_COUNT - concurrentHashMap.size();
                log.info("gap ={}", gap);
                //补充元素
                concurrentHashMap.putAll(getData(gap));
            }
        }));

        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.MILLISECONDS);
        log.info("finish size:{}", concurrentHashMap.size());
        return "OK";

    }




    /**
     * 帮助方法，用来获得一个指定元素数量模拟数据的ConcurrentHashMap
     *
     * @param count
     * @return
     */
    private ConcurrentHashMap<String, Long> getData(int count) {
        return LongStream.rangeClosed(1, count)
                .boxed().collect(Collectors.toConcurrentMap(i -> UUID.randomUUID().toString(), Function.identity(), (o1, o2) -> o1, ConcurrentHashMap::new));
    }

}

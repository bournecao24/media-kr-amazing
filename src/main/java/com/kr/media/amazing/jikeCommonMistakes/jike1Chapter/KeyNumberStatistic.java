package com.kr.media.amazing.jikeCommonMistakes.jike1Chapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 使用 Map 来统计 Key 出现次数的场景
 * 使用 ConcurrentHashMap 来统计，Key 的范围是 10。
 *
 * 使用最多 10 个并发，循环操作 1000 万次，每次操作累加随机的 Key。
 *
 * 如果 Key 不存在的话，首次设置值为 1
 *
 * @Author: caozhenlong
 * @Date: 2021-01-23
 * @Description:
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class KeyNumberStatistic {

    // 循环次数
    private static int LOOP_COUNT = 10000000;
    //线程数量
    private static int THREAD_COUNT = 10;
    //元素数量
    private static int ITEM_COUNT = 10;


    @RequestMapping("KeyNumber/test")
    public String good() throws InterruptedException {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start("normaluse");
        Map<String, Long> resultMap = normalUse();
        stopwatch.stop();

        Assert.isTrue(resultMap.size() == ITEM_COUNT, "normaluse size error");
        Assert.isTrue(resultMap.entrySet().stream().mapToLong(Map.Entry::getValue).reduce(0, Long::sum) == LOOP_COUNT, "normaluse count error");

        stopwatch.start("gooduse");
        Map<String, Long> goodsUseResult = goodUse();
        stopwatch.stop();

        Assert.isTrue(goodsUseResult.size() == ITEM_COUNT, "gooduse size error");
        Assert.isTrue(goodsUseResult.entrySet().stream().mapToLong(Map.Entry::getValue).reduce(0, Long::sum) == LOOP_COUNT, "gooduse count error");
        log.info(stopwatch.prettyPrint());
        return "OK";
    }


    private Map<String, Long> normalUse() throws InterruptedException {

        ConcurrentHashMap<String, Long> concurrentHashMap = new ConcurrentHashMap<>(ITEM_COUNT);

        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, LOOP_COUNT).forEach(i -> {
            // 获取一个随机的 key
            String key = "item" + ThreadLocalRandom.current().nextInt(ITEM_COUNT);

            synchronized (concurrentHashMap) {
                if (concurrentHashMap.containsKey(key)) {
                    concurrentHashMap.put(key, concurrentHashMap.get(key) + 1);
                } else {
                    concurrentHashMap.put(key, 1L);
                }
            }
        }));

        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        return concurrentHashMap;
    }

    private Map<String, Long> goodUse() throws InterruptedException {
        ConcurrentHashMap<String, LongAdder> concurrentHashMap = new ConcurrentHashMap<>(ITEM_COUNT);

        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, LOOP_COUNT).forEach(i -> {
            // 获取一个随机的 key
            String key = "item" + ThreadLocalRandom.current().nextInt(ITEM_COUNT);

            // 利用 computeIfAbsent() 方法来实例化 LongAdder，然后利用 LongAdder 来进行线程安全计数
            concurrentHashMap.computeIfAbsent(key, k -> new LongAdder()).increment();
        }));

        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        //因为我们的 Value 是 LongAdder 而不是 Long，所以需要做一次转换才能返回
        return concurrentHashMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().longValue()));
    }

}

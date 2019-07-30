package com.newland.paas.common.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * List工具
 * 
 * @author SongDi
 * @date 2018/06/27
 */
public class ListUtils {

    public static <T, R> List<R> transformedList(List<T> list, Function<T, R> transformer) {
        ArrayList<R> answer = new ArrayList<R>();
        if (list != null && transformer != null) {
            for (T t : list) {
                answer.add(transformer.apply(t));
            }
        }
        return answer;
    }

    /**
     * 随机选取List中指定数目的元素
     * 
     * @param in
     * @param selectNum
     * @return
     */
    public static <T> List<T> randomSelection(List<T> in, int selectNum) {
        if (in == null || in.size() == 0 || in.size() < selectNum) {
            return Collections.emptyList();
        }
        if (in.size() == selectNum) {
            return in;
        }
        ArrayList<T> answer = new ArrayList<T>();
        Set<Integer> randomIndex = new HashSet<>(selectNum);
        SecureRandom random;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            return Collections.emptyList();
        }
        while (randomIndex.size() < selectNum) {
            randomIndex.add(random.nextInt(in.size()));
        }
        for (Integer index : randomIndex) {
            answer.add(in.get(index));
        }
        return answer;
    }
}

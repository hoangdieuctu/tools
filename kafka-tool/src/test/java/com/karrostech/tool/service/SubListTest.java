package com.karrostech.tool.service;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class SubListTest {

    @Test
    public void testSublist() {
        List<Integer> items = new ArrayList<>(100);
        IntStream.range(0, 100).forEach(i -> items.add(i));

        List<Integer> subItems = items.subList(50, 60);
        Assert.assertEquals(50, subItems.get(0), 0);
        Assert.assertEquals(59, subItems.get(9), 0);
    }

}

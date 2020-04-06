package com.karrostech.tool.util;

import com.karrostech.tool.constant.GeneratorType;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class GeneratorBuilderTest {

    private GeneratorBuilder builder = new GeneratorBuilder();

    @Test
    public void testExtractKey() {
        Assert.assertEquals(GeneratorType.random_int, builder.extractKey("random_int(2,6)"));
        Assert.assertEquals(GeneratorType.random_uuid, builder.extractKey("random_uuid"));
        Assert.assertEquals(GeneratorType.random_uuid, builder.extractKey("random_uuid(5,10)"));
        Assert.assertEquals(GeneratorType.random_default, builder.extractKey("check_defalut(5,10)"));
    }

    @Test
    public void testExtractParams() {
        List<String> params = builder.extractParams("random_int(2,6)");
        Assert.assertEquals(2, params.size());
        Assert.assertEquals("2", params.get(0));
        Assert.assertEquals("6", params.get(1));
    }
}

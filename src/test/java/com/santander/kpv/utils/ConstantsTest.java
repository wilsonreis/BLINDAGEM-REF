package com.santander.kpv.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
class ConstantsTest {

    @Test
    void testConstants() {
        assertEquals(1000L, Constants.SECOND);
        assertEquals(Constants.MINUTE, 60 * Constants.SECOND);
        assertEquals(Constants.HOUR, 60 * Constants.MINUTE);
    }
}

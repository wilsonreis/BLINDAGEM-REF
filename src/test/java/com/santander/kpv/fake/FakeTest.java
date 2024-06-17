package com.santander.kpv.fake;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FakeTest {
    private Fake fake;

    @BeforeEach
    void setUp() {
        fake = new Fake();
    }

    @AfterEach
    void tearDown() {
        fake = null;
    }

    @Test
    void getUserName() {
        assertNull(fake.getUserName());
    }

    @Test
    void getSystemId() {
        assertNull(fake.getSystemId());
    }

    @Test
    void getRemoteAddr() {
        assertNull(fake.getRemoteAddr());
    }

    @Test
    void getLoginTimestamp() {
        assertNull(fake.getLoginTimestamp());
    }

    @Test
    void setUserName() {
        fake.setUserName("John Doe");
        assertEquals("John Doe", fake.getUserName());
    }

    @Test
    void setSystemId() {
        fake.setSystemId("12345");
        assertEquals("12345", fake.getSystemId());
    }

    @Test
    void setRemoteAddr() {
        fake.setRemoteAddr("192.168.1.100");
        assertEquals("192.168.1.100", fake.getRemoteAddr());
    }

    @Test
    void setLoginTimestamp() {
        fake.setLoginTimestamp("2023-06-13T10:15:30");
        assertEquals("2023-06-13T10:15:30", fake.getLoginTimestamp());
    }

    @Test
    void testEquals() {
        Fake fake1 = new Fake("John Doe", "12345", "192.168.1.100", "2023-06-13T10:15:30");
        Fake fake2 = new Fake("John Doe", "12345", "192.168.1.100", "2023-06-13T10:15:30");
        Fake fake3 = new Fake("Jane Smith", "67890", "10.0.0.1", "2023-06-14T08:00:00");

        assertEquals(fake1, fake2);
        assertNotEquals(fake1, fake3);
    }

    @Test
    void canEqual() {
        Fake fake1 = new Fake("John Doe", "12345", "192.168.1.100", "2023-06-13T10:15:30");
        Fake fake2 = new Fake("John Doe", "12345", "192.168.1.100", "2023-06-13T10:15:30");

        assertTrue(fake1.canEqual(fake2));
    }

    @Test
    void testHashCode() {
        Fake fake1 = new Fake("John Doe", "12345", "192.168.1.100", "2023-06-13T10:15:30");
        Fake fake2 = new Fake("John Doe", "12345", "192.168.1.100", "2023-06-13T10:15:30");

        assertEquals(fake1.hashCode(), fake2.hashCode());
    }

    @Test
    void testToString() {
        Fake johnDoe = new Fake("John Doe", "12345", "192.168.1.100", "2023-06-13T10:15:30");

        assertEquals("Fake(userName=John Doe, systemId=12345, remoteAddr=192.168.1.100, loginTimestamp=2023-06-13T10:15:30)", johnDoe.toString());
    }

    @Test
    void builder() {
        Fake johnDoe = Fake.builder()
                .userName("John Doe")
                .systemId("12345")
                .remoteAddr("192.168.1.100")
                .loginTimestamp("2023-06-13T10:15:30")
                .build();

        assertEquals("John Doe", johnDoe.getUserName());
        assertEquals("12345", johnDoe.getSystemId());
        assertEquals("192.168.1.100", johnDoe.getRemoteAddr());
        assertEquals("2023-06-13T10:15:30", johnDoe.getLoginTimestamp());
    }
}

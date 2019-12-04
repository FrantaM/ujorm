/*
 * Copyright 2019-2019 Pavel Ponec
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ujorm.tools.thread;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;
import org.ujorm.tools.thread.MultiJob.MultiJobException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Pavel Ponec
 */
public class MultiJobTest {

    private final Logger logger = Logger.getLogger(getClass().getName());

    /**
     * Test of getStream method, of class MultiRun.
     */
    @Test
    public void testGetStream() {
        System.out.println("run");

        Stream<Long> result = MultiJob.forParams(1, 2, 3).run(p -> p * 10L);

        List<Long> sortedList = result.sorted().collect(Collectors.toList());
        assertEquals(3, sortedList.size());
        assertEquals(10L, sortedList.get(0).longValue());
    }

    /**
     * Test of getSingle method, of class MultiRun.
     */
    @Test
    public void testRunToStream() {
        System.out.println("runToStream");

        Stream<Long> result = MultiJob.forParams(1, 2, 3).runToStream(p -> Stream.of(p * 10L, p * 100L));

        List<Long> sortedList = result.sorted().collect(Collectors.toList());
        assertEquals(6, sortedList.size());
        assertEquals(10L, sortedList.get(0).longValue());
    }

    /**
     * Check a timeout
     */
    @Test
    public void testCheckTimeout() {
        System.out.println("getTimeout");
        Duration timeout = Duration.ofMillis(100);
        MultiJobException result = null;
        Stream<Long> stream = null;

        try {
            MultiJob.forParams(100, 200, 500).run(p -> sleep(p), timeout)
                .mapToLong(Long::longValue)
                .sum();
        } catch (MultiJobException e) {
            result = e;
        }

        assertTrue(stream == null);
        assertTrue(result != null);
        assertTrue(result.getCause() instanceof TimeoutException);
    }

    /**
     * Check Time of work..
     */
    @Test
    public void testTimeOfWork() {
        System.out.println("timeOfWork");

        int jobCount = 100;
        Integer[] params = new Integer[jobCount];
        Arrays.fill(params, 1);

        LocalDateTime start = LocalDateTime.now();
        long sum = MultiJob.forParams(params).run(p -> sleep(p * 1_000))
                .mapToLong(Long::longValue)
                .sum();
        LocalDateTime stop = LocalDateTime.now();

        Duration duration = Duration.between(start, stop);
        assertTrue(duration.getSeconds() < (jobCount / 4));
        assertEquals(jobCount * 1_000, (long) sum);

        logger.log(Level.INFO, String.format("My work took %s seconds.", duration.getSeconds()));
    }

    /**
     * Check Time of ParallelStream.
     */
    @Test
    public void testTimeOfParallelStream() {
        System.out.println("timeOfParallelStream");

        int jobCount = 100;
        Integer[] params = new Integer[jobCount];
        Arrays.fill(params, 1);

        LocalDateTime start = LocalDateTime.now();
        long sum = Arrays.stream(params)
                .parallel()
                .map(p -> sleep(p * 1_000))
                .mapToLong(Long::longValue)
                .sum();
        LocalDateTime stop = LocalDateTime.now();

        Duration duration = Duration.between(start, stop);
        assertTrue(duration.getSeconds() < (jobCount / 4));
        assertEquals(jobCount * 1_000, (long) sum);

        logger.log(Level.INFO, String.format("My work took %s seconds.", duration.getSeconds()));
    }

    private long sleep(int millis) {
        try {
            Thread.sleep(millis);
            logger.log(Level.INFO, "Waiting {} millis", millis);
            return millis;
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "An interrupting of the test", e);
            return -1L;
        }
    }
}

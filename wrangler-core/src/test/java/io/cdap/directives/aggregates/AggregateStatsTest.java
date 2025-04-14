/*
 * Copyright © 2025 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package io.cdap.directives.aggregates;

import io.cdap.wrangler.TestingRig;
import io.cdap.wrangler.api.Row;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Tests {@link AggregateStatsTest}
 */

public class AggregateStatsTest {

    @Test
    public void testAggregateStatsDirective() throws Exception {
        // Input with mixed units
        List<Row> input = Arrays.asList(
                new Row("data_transfer_size", "10KB").add("response_time", "150ms"),   // ~0.0095 MB, 0.15 sec
                new Row("data_transfer_size", "1.5MB").add("response_time", "2.1s"),   // 1.5 MB, 2.1 sec
                new Row("data_transfer_size", "0").add("response_time", "0ms"),        // Edge case: zero
                new Row("data_transfer_size", "512B").add("response_time", "500us")    // ~0.000000488 MB, 0.0005 sec
        );

        String[] recipe = new String[]{
                "aggregate-stats :data_transfer_size :response_time total_size_mb total_time_sec"
        };

        List<Row> results = TestingRig.execute(recipe, input);

        // Expect a single row with total aggregates
        Assert.assertEquals(1, results.size());
        Row aggregated = results.get(0);

        // Calculate expected total size in MB
        double totalBytes = (10 * 1024) + // 10KB
                (1.5 * 1024 * 1024) + // 1.5MB
                0 + // 0B
                512; // 512B
        double expectedTotalMB = totalBytes / (1024.0 * 1024.0); // using binary MB

        // Calculate expected total time in seconds
        double expectedTotalSec = 0.150 + 2.1 + 0.0 + 0.0005;

        Assert.assertEquals(expectedTotalMB,
                (Double) aggregated.getValue("total_size_mb"), 0.001);
        Assert.assertEquals(expectedTotalSec,
                (Double) aggregated.getValue("total_time_sec"), 0.001);
    }
}


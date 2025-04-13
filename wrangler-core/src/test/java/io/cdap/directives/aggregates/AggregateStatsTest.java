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

import java.util.Arrays;
import java.util.List;


import org.junit.Test;
import org.junit.Assert;


import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.TestingRig;

public class AggregateStatsTest {

    @Test
    public void testAggregateStatsDirective() throws Exception {
        List<Row> input = Arrays.asList(
                new Row("data_transfer_size", "10KB").add("response_time", "150ms"),
                new Row("data_transfer_size", "1.5MB").add("response_time", "2.1s")
        );

        String[] recipe = new String[]{
                "aggregate-stats :data_transfer_size :response_time total_size_mb total_time_sec"
        };

        List<Row> results = TestingRig.execute(recipe, input);

        Assert.assertEquals(2, results.size());

        Row row1 = results.get(0);
        Row row2 = results.get(1);

        double expectedMB1 = 10 * 1024 / (1024.0 * 1024.0); // ~0.0095 MB
        double expectedMB2 = 1.5; // MB already

        double expectedSec1 = 0.150;
        double expectedSec2 = 2.1;

        Assert.assertEquals(expectedMB1, (Double) row1.getValue("total_size_mb"), 0.001);
        Assert.assertEquals(expectedSec1, (Double) row1.getValue("total_time_sec"), 0.001);
        Assert.assertEquals(expectedMB2, (Double) row2.getValue("total_size_mb"), 0.001);
        Assert.assertEquals(expectedSec2, (Double) row2.getValue("total_time_sec"), 0.001);
    }
}


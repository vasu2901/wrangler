/*
 *  Copyright © 2017-2019 Cask Data, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 */

package io.cdap.directives.aggregates;

import io.cdap.wrangler.api.Arguments;
import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.DirectiveExecutionException;
import io.cdap.wrangler.api.DirectiveParseException;
import io.cdap.wrangler.api.ExecutorContext;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.ColumnName;
import io.cdap.wrangler.api.parser.Text;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.parser.UsageDefinition;

import java.util.Collections;
import java.util.List;
/**
 * A directive that calculates aggregated size and time statistics.
 */

public class AggregateStats implements Directive {
    private String sizeColumn;
    private String timeColumn;
    private String outputSizeColumn;
    private String outputTimeColumn;

    @Override
    public UsageDefinition define() {
        return UsageDefinition.builder("aggregate-stats").build(); // 👈 empty but valid
    }

    @Override
    public void initialize(Arguments args) throws DirectiveParseException {
        sizeColumn = ((ColumnName) args.value("sizeColumn")).value();
        timeColumn = ((ColumnName) args.value("timeColumn")).value();
        outputSizeColumn = ((Text) args.value("outputSizeColumn")).value();
        outputTimeColumn = ((Text) args.value("outputTimeColumn")).value();
    }

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext context) throws DirectiveExecutionException {
        // Your logic here, or remove if unused
        return Collections.emptyList();
    }


    @Override
    public void destroy() {

    }

    private long parseBytes(Object val) {
        if (val instanceof String) {
            return new ByteSize((String) val).getBytes();
        }
        if (val instanceof Number) {
            return ((Number) val).longValue();
        }
        return 0;
    }

    private long parseTime(Object val) {
        if (val instanceof String) {
            return new TimeDuration((String) val).getMilliseconds();
        }
        if (val instanceof Number) {
            return ((Number) val).longValue();
        }
        return 0;
    }
}

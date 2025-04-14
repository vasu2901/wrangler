/*
 * Copyright © 2017-2019 Cask Data, Inc.
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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cdap.wrangler.api.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.cdap.wrangler.api.annotations.PublicEvolving;

/**
 * Token class representing time durations like "150ms", "2.1s", etc.
 */
@PublicEvolving
public class TimeDuration implements Token {

    private final long milliseconds;

    public TimeDuration(String value) {
        this.milliseconds = parse(value.trim().toLowerCase());
    }

    private long parse(String value) {
        if (value.endsWith("ms")) {
            return (long) (Double.parseDouble(value.replace("ms", "")));
        }
        if (value.endsWith("s")) {
            return (long) (Double.parseDouble(value.replace("s", "")) * 1000);
        }
        if (value.endsWith("ns")) {
            return (long) (Double.parseDouble(value.replace("ns", "")) / 1_000_000);
        }
        throw new IllegalArgumentException("Invalid time duration format: " + value);
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    @Override
    public Object value() {
        return milliseconds;
    }

    @Override
    public TokenType type() {
        return TokenType.TIME_DURATION;
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("type", type().name());
        object.addProperty("value", milliseconds);
        return object;
    }
}

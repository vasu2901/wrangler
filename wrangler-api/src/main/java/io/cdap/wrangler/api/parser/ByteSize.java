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
 * See the License for the specific language governing permissions and limitations under the License.
 */

package io.cdap.wrangler.api.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.cdap.wrangler.api.annotations.PublicEvolving;

/**
 * Token class representing byte size values like "10KB", "1.5MB", etc.
 */
@PublicEvolving
public class ByteSize implements Token {
    private final long bytes;

    public ByteSize(String value) {
        this.bytes = parseBytes(value.trim().toLowerCase());
    }

    private long parseBytes(String value) {
        if (value.endsWith("kb")) {
            return (long) (Double.parseDouble(value.replace("kb", "")) * 1024);
        }
        if (value.endsWith("mb")) {
            return (long) (Double.parseDouble(value.replace("mb", "")) * 1024 * 1024);
        }
        if (value.endsWith("gb")) {
            return (long) (Double.parseDouble(value.replace("gb", "")) * 1024 * 1024 * 1024);
        }
        if (value.endsWith("b")) {
            return Long.parseLong(value.replace("b", ""));
        }
        throw new IllegalArgumentException("Invalid byte size format: " + value);
    }

    public long getBytes() {
        return bytes;
    }

    @Override
    public Object value() {
        return bytes;
    }

    @Override
    public TokenType type() {
        return TokenType.BYTE_SIZE;
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("type", type().name());
        object.addProperty("value", bytes);
        return object;
    }
}

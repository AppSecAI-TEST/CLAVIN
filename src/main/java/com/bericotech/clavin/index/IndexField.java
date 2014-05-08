/*
 * Copyright 2014 Berico Technologies.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bericotech.clavin.index;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The fields of the Lucene gazetteer index.
 */
public enum IndexField {
    INDEX_NAME("indexName"),
    GEONAME("geoname"),
    GEONAME_ID("geonameID"),
    PARENT_ID("parentID"),
    POPULATION("population"),
    HISTORICAL("historical"),
    FEATURE_CODE("featureCode");

    /**
     * The class logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(IndexField.class);

    /**
     * The key of this field in the index.
     */
    private final String key;

    private IndexField(final String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }

    /**
     * Get the value of this field as set in the given document or <code>null</code>
     * if the field is not set or cannot be retrieved.
     * @param <T> the expected return type
     * @param doc the input document
     * @return the value of this field in the input document, if it has been set, or <code>null</code>
     */
    @SuppressWarnings("unchecked")
    public <T> T getValue(final Document doc) {
        IndexableField field = doc.getField(key);
        Object value = null;
        if (field != null) {
            switch (this) {
                case INDEX_NAME:
                case GEONAME:
                    value = field.stringValue();
                    break;
                case GEONAME_ID:
                case PARENT_ID:
                    value = field.numericValue().intValue();
                    break;
                case POPULATION:
                    value = field.numericValue().longValue();
                    break;
                case HISTORICAL:
                case FEATURE_CODE:
                    // these fields are not stored
                    LOG.warn("Attempting to retrieve value for an unstored field: [{}]", this);
                    break;
                default:
                    LOG.error("Attempting to retrieve value for an unconfigured field: [{}]", this);
                    break;
            }
        }
        return (T) value;
    }

    /**
     * Gets the integer value representing the provided boolean value in
     * the Lucene index.
     * @param inBool the boolean value
     * @return the numeric value representing the boolean in the index
     */
    public static int getBooleanIndexValue(final boolean inBool) {
        return inBool ? 1 : 0;
    }
}

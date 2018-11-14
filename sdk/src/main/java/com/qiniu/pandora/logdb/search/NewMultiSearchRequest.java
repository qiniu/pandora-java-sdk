package com.qiniu.pandora.logdb.search;

import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContent;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

public class NewMultiSearchRequest {

    public static int MAX_CONCURRENT_SEARCH_REQUESTS_DEFAULT = 200;

    /**
     * parse responses
     *
     * @param multiSearchRequest request
     * @param xContent           xContent
     * @return multiSearchResponse in bytes
     * @throws IOException read fail
     */
    public static byte[] writeMultiLineFormat(MultiSearchRequest multiSearchRequest, XContent xContent) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        for (SearchRequest request : multiSearchRequest.requests()) {
            try (XContentBuilder xContentBuilder = XContentBuilder.builder(xContent)) {
                xContentBuilder.startObject();
                if (request.indices() != null) {
                    xContentBuilder.field("index", request.indices());
                }
                if (request.indicesOptions() != null && request.indicesOptions() != SearchRequest.DEFAULT_INDICES_OPTIONS) {
                    if (request.indicesOptions().expandWildcardsOpen() && request.indicesOptions().expandWildcardsClosed()) {
                        xContentBuilder.field("expand_wildcards", "all");
                    } else if (request.indicesOptions().expandWildcardsOpen()) {
                        xContentBuilder.field("expand_wildcards", "open");
                    } else if (request.indicesOptions().expandWildcardsClosed()) {
                        xContentBuilder.field("expand_wildcards", "closed");
                    } else {
                        xContentBuilder.field("expand_wildcards", "none");
                    }
                    xContentBuilder.field("ignore_unavailable", request.indicesOptions().ignoreUnavailable());
                    xContentBuilder.field("allow_no_indices", request.indicesOptions().allowNoIndices());
                }
                if (request.types() != null) {
                    xContentBuilder.field("types", request.types());
                }
                if (request.searchType() != null) {
                    xContentBuilder.field("search_type", request.searchType().name().toLowerCase(Locale.ROOT));
                }
                if (request.requestCache() != null) {
                    xContentBuilder.field("request_cache", request.requestCache());
                }
                if (request.preference() != null) {
                    xContentBuilder.field("preference", request.preference());
                }
                if (request.routing() != null) {
                    xContentBuilder.field("routing", request.routing());
                }
                xContentBuilder.endObject();
                xContentBuilder.bytes().writeTo(output);
            }
            output.write(xContent.streamSeparator());
            try (XContentBuilder xContentBuilder = XContentBuilder.builder(xContent)) {
                if (request.source() != null) {
                    request.source().toXContent(xContentBuilder, ToXContent.EMPTY_PARAMS);
                } else {
                    xContentBuilder.startObject();
                    xContentBuilder.endObject();
                }
                xContentBuilder.bytes().writeTo(output);
            }
            output.write(xContent.streamSeparator());
        }
        return output.toByteArray();
    }
}

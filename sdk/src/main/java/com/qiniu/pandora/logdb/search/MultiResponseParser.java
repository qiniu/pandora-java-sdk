package com.qiniu.pandora.logdb.search;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.xcontent.ConstructingObjectParser;
import org.elasticsearch.common.xcontent.XContentParser;

import java.io.IOException;
import java.util.List;

import static com.qiniu.pandora.logdb.search.SearchBase.innerFromXContent;
import static org.elasticsearch.common.xcontent.ConstructingObjectParser.constructorArg;

public class MultiResponseParser {
    private static final ParseField RESPONSES = new ParseField(Fields.RESPONSES);

    static final class Fields {
        static final String RESPONSES = "responses";
    }

    private static final ConstructingObjectParser<MultiSearchResponse, Void> PARSER = new ConstructingObjectParser<>("multi_search",
            true, a -> new MultiSearchResponse(((List<MultiSearchResponse.Item>) a[0]).toArray(new MultiSearchResponse.Item[0])));

    static {
        PARSER.declareObjectArray(constructorArg(), (p, c) -> itemFromXContent(p), RESPONSES);
    }

    private static MultiSearchResponse.Item itemFromXContent(XContentParser parser) throws IOException {
        // This parsing logic is a bit tricky here, because the multi search response itself is tricky:
        // 1) The json objects inside the responses array are either a search response or a serialized exception
        // 2) Each response json object gets a status field injected that ElasticsearchException.failureFromXContent(...) does not parse,
        //    but SearchResponse.innerFromXContent(...) parses and then ignores. The status field is not needed to parse
        //    the response item. However in both cases this method does need to parse the 'status' field otherwise the parsing of
        //    the response item in the next json array element will fail due to parsing errors.

        MultiSearchResponse.Item item = null;
        String fieldName = null;

        XContentParser.Token token = parser.nextToken();
        assert token == XContentParser.Token.FIELD_NAME;
        outer:
        for (; token != XContentParser.Token.END_OBJECT; token = parser.nextToken()) {
            switch (token) {
                case FIELD_NAME:
                    fieldName = parser.currentName();
                    if ("error".equals(fieldName)) {
                        item = new MultiSearchResponse.Item(null, ElasticsearchException.failureFromXContent(parser));
                    } else if ("status".equals(fieldName) == false) {
                        item = new MultiSearchResponse.Item(innerFromXContent(parser), null);
                        break outer;
                    }
                    break;
                case VALUE_NUMBER:
                    if ("status".equals(fieldName)) {
                        // Ignore the status value
                    }
                    break;
            }
        }
        assert parser.currentToken() == XContentParser.Token.END_OBJECT;
        return item;
    }

    /**
     * @param parser parser
     * @return MultiSearchResponse
     */
    public static MultiSearchResponse fromXContext(XContentParser parser) {
        return PARSER.apply(parser, null);
    }
}

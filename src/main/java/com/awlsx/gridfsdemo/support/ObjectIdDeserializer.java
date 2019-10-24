package com.awlsx.gridfsdemo.support;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.bson.types.ObjectId;

import java.io.IOException;

public class ObjectIdDeserializer extends StdDeserializer<ObjectId> {
    public ObjectIdDeserializer() {
        super(ObjectId.class);
    }

    protected ObjectIdDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ObjectId deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String id = jsonParser.getValueAsString();
        return new ObjectId(id);
    }
}
package com.awlsx.gridfsdemo.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.bson.types.ObjectId;

import java.io.IOException;

public class ObjectIdSerializer extends StdSerializer<ObjectId> {

    public ObjectIdSerializer() {
        super(ObjectId.class);
    }

    protected ObjectIdSerializer(Class<ObjectId> t) {
        super(t);
    }

    @Override
    public void serialize(ObjectId objectId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (objectId == null) {
            jsonGenerator.writeNull();
        } else {
            jsonGenerator.writeString(objectId.toString());
        }
    }
}
package org.trips.service_framework.utils;

import com.google.protobuf.*;

/**
 * @author Abhinav Tripathi 26/02/20
 */
public class ProtoMapperUtil {
    public Long toJavaDataType(Int64Value value) {
        return value.getValue();
    }

    public Int64Value toProtoInt64(Long value) {
        return Int64Value.newBuilder().setValue(value).build();
    }

    public Long toJavaDataType(UInt64Value value) {
        return value.getValue();
    }

    public UInt64Value toProtoUInt64(Long value) {
        return UInt64Value.newBuilder().setValue(value).build();
    }

    public Integer toJavaDataType(UInt32Value value) {
        return value.getValue();
    }

    public UInt32Value toProtoUInt32(Integer value) {
        return UInt32Value.newBuilder().setValue(value).build();
    }

    public Integer toJavaDataType(Int32Value value) {
        return value.getValue();
    }

    public Int32Value toProtoInt32(Integer value) {
        return Int32Value.newBuilder().setValue(value).build();
    }

    public Double toJavaDataType(DoubleValue value) {
        return value.getValue();
    }

    public DoubleValue toProtoDouble(Double value) {
        return DoubleValue.newBuilder().setValue(value).build();
    }

    public Boolean toJavaDataType(BoolValue value) {
        return value.getValue();
    }

    public BoolValue toProtoBool(Boolean value) {
        return BoolValue.newBuilder().setValue(value).build();
    }

    public String toJavaDataType(StringValue value) {
        return value.getValue();
    }

    public StringValue toProtoString(String value) {
        return StringValue.newBuilder().setValue(value).build();
    }
}

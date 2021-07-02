package io.common.hoony.noticeboard.common.type;

import io.common.hoony.noticeboard.error.DkargoException;
import io.common.hoony.noticeboard.error.ErrorCode;

import javax.persistence.AttributeConverter;
import java.util.stream.Stream;

public class FlagTypeConverter implements AttributeConverter<FlagType, String> {

    @Override
    public String convertToDatabaseColumn(FlagType flagType) {
        if (flagType == null) return null;
        return flagType.getValue();
    }

    @Override
    public FlagType convertToEntityAttribute(String value) {
        if (value == null) return null;

        return Stream.of(FlagType.values())
                .filter(status -> status.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new DkargoException(ErrorCode.FLAG_INTERNAL_SERVER_ERROR));
    }
}

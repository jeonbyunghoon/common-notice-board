package io.common.hoony.noticeboard.common.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum FlagType {

    Y("Y", "활성"),
    N("N", "비활성");

    private String value;
    private String description;

    @JsonCreator
    public static FlagType enumOf(String value) {
        return Arrays.stream(FlagType.values())
                .filter(t -> t.getValue().equals(value))
                .findAny().orElse(null);
    }

    @JsonValue
    public String getValue() { return value; }

}

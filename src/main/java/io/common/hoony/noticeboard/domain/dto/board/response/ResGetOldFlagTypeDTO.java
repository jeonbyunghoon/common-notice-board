package io.common.hoony.noticeboard.domain.dto.board.response;

import io.common.hoony.noticeboard.common.type.FlagType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResGetOldFlagTypeDTO {
    private FlagType pwActiveFlag;
}

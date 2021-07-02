package io.common.hoony.noticeboard.domain.dto.comment.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReqDeleteCommentListDTO {

    private Long boardId; // 게시판 ID

    public ReqDeleteCommentListDTO(long boardId){
        this.boardId = boardId;
    }

}
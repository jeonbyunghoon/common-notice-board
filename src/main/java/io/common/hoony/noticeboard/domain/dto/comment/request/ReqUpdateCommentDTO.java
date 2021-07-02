package io.common.hoony.noticeboard.domain.dto.comment.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReqUpdateCommentDTO {

    private Long commentId; //댓글 ID
    private Long boardId; //게시판 ID
    private Long userId; //작성자
    private String contents; //내용

}

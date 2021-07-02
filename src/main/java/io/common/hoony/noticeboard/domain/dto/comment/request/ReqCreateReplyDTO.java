package io.common.hoony.noticeboard.domain.dto.comment.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReqCreateReplyDTO {

    private Long boardId; //게시판 ID
    private Long userId; //작성자
    private String contents; //내용
    private int commentGroup; //댓글 집합(부모의)
    private int commentOrder; //댓글 순서(부모의)
    private int commentLayer; //댓글 계층(부모의)

}

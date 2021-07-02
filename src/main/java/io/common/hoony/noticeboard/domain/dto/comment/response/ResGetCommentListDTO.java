package io.common.hoony.noticeboard.domain.dto.comment.response;

import io.common.hoony.noticeboard.common.type.FlagType;
import io.common.hoony.noticeboard.domain.entity.Comment;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResGetCommentListDTO {

    private List<GetComment> list = new ArrayList<>();

    public ResGetCommentListDTO(List<Comment> comments) {
        this.list = comments.stream().map(c -> new GetComment(c)).collect(Collectors.toList());
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GetComment {
        private Long boardId; //게시글 ID
        private Long commentId; //댓글 ID
        private int commentGroup; //댓글 집합
        private int commentOrder; //댓글 순서
        private int commentLayer; //댓글 계층
        private Long userId; //작성자 ID
        private String contents; //내용
        private LocalDateTime createAt; //생성 날짜
        private LocalDateTime deleteAt; //삭제 날짜
        private FlagType deleteFlag; //삭제 여부
        private LocalDateTime updateAt; //수정 날짜

        public GetComment(Comment comment) {
            this.boardId = comment.getBoard().getBoardId();
            this.commentId = comment.getCommentId();
            this.commentGroup = comment.getCommentGroup();
            this.commentOrder = comment.getCommentOrder();
            this.commentLayer = comment.getCommentLayer();
            this.userId = comment.getUserId();
            this.contents = comment.getContents();
            this.createAt = comment.getCreateAt();
            this.createAt = comment.getDeleteAt();
            this.deleteFlag = comment.getDeleteFlag();
            this.updateAt = comment.getUpdateAt();
        }

    }

}

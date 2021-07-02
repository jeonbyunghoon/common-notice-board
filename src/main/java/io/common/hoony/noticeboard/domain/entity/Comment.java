package io.common.hoony.noticeboard.domain.entity;

import io.common.hoony.noticeboard.common.type.FlagType;
import io.common.hoony.noticeboard.common.type.FlagTypeConverter;
import io.common.hoony.noticeboard.domain.dto.comment.request.ReqCreateCommentDTO;
import io.common.hoony.noticeboard.domain.dto.comment.request.ReqCreateReplyDTO;
import io.common.hoony.noticeboard.domain.dto.comment.response.ResCreateReplyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Comment {

    //댓글 ID [PK]
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT(20) COMMENT '댓글 ID'", insertable = true, updatable = false)
    private Long commentId;

    //게시글 ID [FK]
    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    //작성자
    @Column(columnDefinition = "BIGINT(20) COMMENT '작성자ID'", nullable = false, insertable = true, updatable = true)
    private Long userId;

    //내용
    @Column(columnDefinition = "VARCHAR(500) COMMENT '내용'", nullable = false, insertable = true, updatable = true)
    private String contents;

    //댓글 그룹
    @Column(columnDefinition = "INT(11) COMMENT '댓글 집합'", nullable = false, insertable = true, updatable = true)
    private int commentGroup;

    //댓글 순서
    @Column(columnDefinition = "INT(11) COMMENT '댓글 순서'", nullable = false, insertable = true, updatable = true)
    private int commentOrder;

    //댓글 계층
    @Column(columnDefinition = "INT(11) COMMENT '댓글 계층'", nullable = false, insertable = true, updatable = true)
    private int commentLayer;

    //삭제 여부
    @Column(columnDefinition = "VARCHAR(2) COMMENT '삭제 여부'", nullable = false, insertable = true, updatable = true)
    @Convert(converter = FlagTypeConverter.class)
    private FlagType deleteFlag;

    //생성 날짜
    @Column(columnDefinition = "DATETIME COMMENT '생성 날짜'", nullable = false, insertable = true, updatable = false)
    private LocalDateTime createAt;

    //수정 날짜
    @Column(columnDefinition = "DATETIME COMMENT '수정 날짜'", nullable = false, insertable = true, updatable = true)
    private LocalDateTime updateAt;

    //삭제 날짜
    @Column(columnDefinition = "DATETIME COMMENT '삭제 날짜'", nullable = true, insertable = true, updatable = true)
    private LocalDateTime deleteAt;

    //댓글의 1차 저장 (commentGroup 값을 일단 0으로 Save, 이후 로직에서 CommentGroup 값 변경)
    public Comment(ReqCreateCommentDTO reqCreateCommentDTO, Board board) {
        this.board = board;
        this.userId = reqCreateCommentDTO.getUserId();
        this.contents = reqCreateCommentDTO.getContents();
        this.commentGroup = 0; //임의값
        this.commentOrder = 0;
        this.commentLayer = 0;
        this.deleteFlag = FlagType.N;
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
        this.deleteAt = null;
    }

    public Comment(ReqCreateReplyDTO reqCreateReplyDTO, Board board) {
        this.board = board;
        this.userId = reqCreateReplyDTO.getUserId();
        this.contents = reqCreateReplyDTO.getContents();
        this.commentGroup = reqCreateReplyDTO.getCommentGroup();
        this.commentOrder = reqCreateReplyDTO.getCommentOrder() + 1;
        this.commentLayer = reqCreateReplyDTO.getCommentLayer() + 1;
        this.deleteFlag = FlagType.N;
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
        this.deleteAt = null;
    }

}

package io.common.hoony.noticeboard.repository;

import io.common.hoony.noticeboard.common.type.FlagType;
import io.common.hoony.noticeboard.domain.entity.Board;
import io.common.hoony.noticeboard.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    //가장 최근 생성된 순으로 댓글/답글 값 조회
    @Query("SELECT c FROM Comment c WHERE c.board.boardId = ?1 AND c.deleteFlag = ?2 ORDER BY c.commentGroup DESC, c.commentOrder ASC")
    List<Comment> findAllCommentList(long boardId, FlagType deleteFlag);

    //가장 최근 생성된 댓글의 ID 값 조회
    @Query("SELECT MAX(c.commentId) FROM Comment c")
    long latestCommentId();

    //특정 게시물에 대한 특정 댓글(commentGroup) 여부 확인)
    boolean existsByBoardAndCommentGroup(Board board, int commentGroup);

    //최근 생성된 댓글의 ID 값을 commentGroup에 반영
    @Modifying
    @Query("UPDATE Comment c SET c.commentGroup = ?1 WHERE c.commentId = ?2")
    void commentGroupUpdate(int intLatestCommentId, long latestCommentId);

    //답글 순서 변경
    @Modifying
    @Query("UPDATE Comment c SET c.commentOrder = c.commentOrder + 1 WHERE c.commentGroup = ?1 AND c.commentOrder > ?2")
    void replyOrderUpdate(int commentGroup, int commentOrder);

    //댓글/답글 업데이트
    @Modifying
    @Query("UPDATE Comment c SET c.contents = ?4, c.updateAt = ?5 WHERE c.commentId = ?1 AND c.board.boardId = ?2 AND c.userId = ?3")
    void updateComment(long commentId, long boardId, long userId, String contents, LocalDateTime updateAt);

    //특정 댓글/답글의 삭제되지 않은 자식 여부 확인
    boolean existsByBoardAndCommentGroupAndCommentOrderAndCommentLayerAndDeleteFlag(Board board, int commentGroup, int commentOrderConfirmValue, int commentLayerConfirmValue, FlagType deleteFlag);

    //특정 게시물의 하나의 답글/댓글 삭제
    @Modifying
    @Query("UPDATE Comment c SET c.deleteFlag = ?4, c.deleteAt = ?5 WHERE c.commentId = ?1 AND c.board.boardId = ?2 AND c.userId = ?3")
    void deleteComment(long commentId, long boardId, long userId, FlagType deleteFlag, LocalDateTime deleteAt);

    //특정 게시물에 해당하는 모든 답글/댓글 삭제
    @Modifying
    @Query("UPDATE Comment c SET c.deleteFlag = ?2, c.deleteAt = ?3 WHERE c.board.boardId = ?1")
    void deleteComments(long boardId, FlagType deleteFlag, LocalDateTime deleteAt);

}

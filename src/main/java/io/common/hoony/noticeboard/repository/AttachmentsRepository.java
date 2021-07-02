package io.common.hoony.noticeboard.repository;

import io.common.hoony.noticeboard.common.type.FlagType;
import io.common.hoony.noticeboard.domain.entity.Attachments;
import io.common.hoony.noticeboard.domain.entity.Board;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AttachmentsRepository extends JpaRepository<Attachments, Long> {

    //특정 게시판에 대한 파일첨부 List 조회
    @Query("SELECT a FROM Attachments a WHERE a.board = ?1 AND a.deleteFlag = 'N' ORDER BY a.attachmentsId ASC")
    List<Attachments> findAttachmentsList(Board board);

    //특정 게시물의 파일첨부 초기화
    @Modifying
    @Query("UPDATE Attachments a SET a.deleteFlag = ?2, a.deleteAt = ?3 WHERE a.board.boardId = ?1 AND a.deleteFlag = ?4")
    void deleteAttachments(long boardId, FlagType deleteFlag, LocalDateTime deleteAt, FlagType deleteFlagConfirmValue);

    //수정하게되면 전체 삭제 후 삭제 안된 것만 다시 되돌린다.
    @Modifying
    @Query("UPDATE Attachments a SET a.deleteFlag = ?2, a.deleteAt = null WHERE a.board.boardId = ?1 AND a.deleteFlag = ?3 AND a.attachmentsId = ?4")
    void revertAttachments(long boardId, FlagType deleteFlag, FlagType deleteFlagConfirmValue, long attachmentId);
}

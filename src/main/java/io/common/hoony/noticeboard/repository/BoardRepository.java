package io.common.hoony.noticeboard.repository;

import io.common.hoony.noticeboard.common.type.FlagType;
import io.common.hoony.noticeboard.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    //삭제목록을 제외한 목록 조회 (자신의 게시글은 비공개일때도 보이고, 자신의 게시글이 아니면 안보임)
    @Query("SELECT b FROM Board b WHERE (b.privateFlag = 'Y' AND b.userId = ?1 and b.deleteFlag = 'N') or (b.privateFlag = 'N' and b.deleteFlag = 'N') order by b.boardId desc")
    List<Board> findAllBoardList(long userId); //List<Board> findAllActiveBoards(long userId);

    //전체 검색
    @Query("SELECT b FROM Board b WHERE ((b.privateFlag = 'Y' AND b.userId = ?1 and b.deleteFlag = 'N') or (b.privateFlag = 'N' and b.deleteFlag = 'N')) and ((CAST(b.userId as string) LIKE %?2%) or (b.title LIKE %?2%) or (b.contents LIKE %?2%)) ORDER BY b.boardId DESC")
    List<Board> findAllSearchBoardList(long userId, String searchValue);

    //작성자 검색
    @Query("SELECT b FROM Board b WHERE ((b.privateFlag = 'Y' AND b.userId = ?1 and b.deleteFlag = 'N') or (b.privateFlag = 'N' and b.deleteFlag = 'N')) and CAST(b.userId as string) LIKE %?2% ORDER BY b.boardId DESC")
    List<Board> findAllUserIdSearchBoardList(long userId, String searchValue);

    //제목 검색
    @Query("SELECT b FROM Board b WHERE ((b.privateFlag = 'Y' AND b.userId = ?1 and b.deleteFlag = 'N') or (b.privateFlag = 'N' and b.deleteFlag = 'N')) and b.title LIKE %?2% ORDER BY b.boardId DESC")
    List<Board> findAllTitleSearchBoardList(long userId, String searchValue);

    //내용 검색
    @Query("SELECT b FROM Board b WHERE ((b.privateFlag = 'Y' AND b.userId = ?1 and b.deleteFlag = 'N') or (b.privateFlag = 'N' and b.deleteFlag = 'N')) and b.contents LIKE %?2% ORDER BY b.boardId DESC")
    List<Board> findAllContentsSearchBoardList(long userId, String searchValue);


    //특정 게시물이 자신의 게시물인지 확인
    boolean existsByBoardIdAndUserId(long boardId, long userId);

    //조회수 증가
    @Modifying
    @Query("UPDATE Board b SET b.views = b.views + 1 WHERE b.boardId = ?1")
    void updateViews(long boardId);

}

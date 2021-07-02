package io.common.hoony.noticeboard.repository;

import io.common.hoony.noticeboard.domain.entity.Board;
import io.common.hoony.noticeboard.domain.entity.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Long> {

    //특정 게시판에 대한 게시판_카테고리 List 조회
    List<BoardCategory> findByBoard(Board board);

    //검색 시 카테고리 값이 한개이고 "전체 - 0"일 때 해당하는 게시판_카테고리 List조회
    @Query("SELECT bc FROM BoardCategory bc GROUP BY bc.board.boardId ORDER BY bc.board.boardId DESC")
    List<BoardCategory> findAllSearchBoardCategoryList();

    //검색 시 카테고리 값이 한개이고 "전체 - 0"이 아닐 때 해당하는 게시판_카테고리 List조회
    @Query("SELECT bc FROM BoardCategory bc WHERE bc.category.categoryId = ?1 GROUP BY bc.board.boardId ORDER BY bc.board.boardId DESC")
    List<BoardCategory> findAllOneSearchBoardCategoryList(long categoryIdOne);

    //검색 시 카테고리 값이 두개일때 해당하는 게시판_카테고리 List조회
    @Query("SELECT bc FROM BoardCategory bc WHERE bc.category.categoryId = ?1 or bc.category.categoryId = ?2 GROUP BY bc.board.boardId ORDER BY bc.board.boardId DESC")
    List<BoardCategory> findAllTwoSearchBoardCategoryList(long categoryIdOne, long categoryIdTwo);

    //검색 시 카테고리 값이 세개일때 해당하는 게시판_카테고리 List조회
    @Query("SELECT bc FROM BoardCategory bc WHERE bc.category.categoryId = ?1 or bc.category.categoryId = ?2 or bc.category.categoryId = ?3 GROUP BY bc.board.boardId ORDER BY bc.board.boardId DESC")
    List<BoardCategory> findAllThreeSearchBoardCategoryList(long categoryIdOne, long categoryIdTwo, long categoryIdThree);

}

package io.common.hoony.noticeboard.service;

import io.common.hoony.noticeboard.domain.dto.board.request.ReqCreateBoardDTO;
import io.common.hoony.noticeboard.domain.dto.board.request.ReqUpdateBoardDTO;
import io.common.hoony.noticeboard.domain.dto.board.response.ResCreateBoardDTO;
import io.common.hoony.noticeboard.domain.dto.board.response.ResUpdateBoardDTO;
import io.common.hoony.noticeboard.domain.entity.Board;
import io.common.hoony.noticeboard.domain.entity.BoardCategory;
import io.common.hoony.noticeboard.domain.entity.Category;
import io.common.hoony.noticeboard.error.DkargoException;
import io.common.hoony.noticeboard.error.ErrorCode;
import io.common.hoony.noticeboard.repository.BoardCategoryRepository;
import io.common.hoony.noticeboard.repository.BoardRepository;
import io.common.hoony.noticeboard.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardCategoryService {

    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final BoardCategoryRepository boardCategoryRepository;


    /**
     * 저장된 게시물의 카테고리 정보 저장 (다중선택가능하며, 1개 이상 / 3개 이하) - Service
     * @param resCreateBoardDTO
     * @param reqCreateBoardDTO
     */
    public void createCategory(ResCreateBoardDTO resCreateBoardDTO, ReqCreateBoardDTO reqCreateBoardDTO) {

        Board board = boardRepository.findById(resCreateBoardDTO.getBoardId()).orElseThrow(() -> new DkargoException(ErrorCode.BOARD_ID_BAD_REQUEST));

        for(int i=0; i<reqCreateBoardDTO.getCategoryId().size(); i++){
            Category category = categoryRepository.findById(reqCreateBoardDTO.getCategoryId().get(i)).orElseThrow(() -> new DkargoException(ErrorCode.CATEGORY_ID_BAD_REQUEST));
            BoardCategory boardCategory = new BoardCategory(category);

            boardCategory.setBoard(board);

            boardCategoryRepository.save(boardCategory);
        }

    }

    /**
     * 카테고리 수정 (다중선택가능하며, 1개 이상 / 3개 이하) - Service
     * @param resUpdateBoardDTO
     * @param reqUpdateBoardDTO
     */
    public void updateCategory(ResUpdateBoardDTO resUpdateBoardDTO, ReqUpdateBoardDTO reqUpdateBoardDTO) {

        Board board = boardRepository.findById(resUpdateBoardDTO.getBoardId()).orElseThrow(() -> new DkargoException(ErrorCode.BOARD_ID_BAD_REQUEST));
        List<BoardCategory> deleteBoardCategory = boardCategoryRepository.findByBoard(board);

        if(board.getUserId() == reqUpdateBoardDTO.getUserId()){

            //기존의 게시판_카테고리에 저장된 내역 지움
            for(int i=0; i<deleteBoardCategory.size(); i++) {
                boardCategoryRepository.delete(deleteBoardCategory.get(i));
            }

            for(int i=0; i<reqUpdateBoardDTO.getCategoryId().size(); i++){
                Category category = categoryRepository.findById(reqUpdateBoardDTO.getCategoryId().get(i)).orElseThrow(() -> new DkargoException(ErrorCode.CATEGORY_ID_BAD_REQUEST));
                BoardCategory boardCategory = new BoardCategory(category);

                boardCategory.setBoard(board);
                boardCategoryRepository.save(boardCategory);
            }
        } else{
            throw new DkargoException(ErrorCode.BOARD_FORBIDDEN);
        }
    }

    /**
     * 삭제된 게시물의 전체 카테고리 값 영구 삭제 - Service
     * @param boardId
     */
    public void deleteCategory(long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new DkargoException(ErrorCode.BOARD_ID_BAD_REQUEST));
        List<BoardCategory> deleteBoardCategory = boardCategoryRepository.findByBoard(board);

        for(int i=0; i<deleteBoardCategory.size(); i++) {
            boardCategoryRepository.delete(deleteBoardCategory.get(i));
        }

    }

}

package io.common.hoony.noticeboard.service;

import io.common.hoony.noticeboard.common.type.FlagType;
import io.common.hoony.noticeboard.domain.dto.board.request.ReqCreateBoardDTO;
import io.common.hoony.noticeboard.domain.dto.board.request.ReqDeleteBoardDTO;
import io.common.hoony.noticeboard.domain.dto.board.request.ReqUpdateBoardDTO;
import io.common.hoony.noticeboard.domain.dto.board.response.*;
import io.common.hoony.noticeboard.domain.entity.Attachments;
import io.common.hoony.noticeboard.domain.entity.Board;
import io.common.hoony.noticeboard.domain.entity.BoardCategory;
import io.common.hoony.noticeboard.error.DkargoException;
import io.common.hoony.noticeboard.error.ErrorCode;
import io.common.hoony.noticeboard.repository.AttachmentsRepository;
import io.common.hoony.noticeboard.repository.BoardCategoryRepository;
import io.common.hoony.noticeboard.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardCategoryRepository boardCategoryRepository;
    private final AttachmentsRepository attachmentsRepository;

    /**
     * 게시글 목록 List 초기 조회 - Service
     * @param userId
     * @return
     */
    public ResGetBoardListDTO getBoardList(long userId) {

        List<Board> boards = boardRepository.findAllBoardList(userId);

        ResGetBoardListDTO.GetBoardCategoryInfo getBoardCategoryInfo;
        List<ResGetBoardListDTO.GetBoardCategoryInfo> getBoardCategoryInfos = new ArrayList<>();

        //case 1
        for (Board board : boards) {
            List<BoardCategory> boardCategories = boardCategoryRepository.findByBoard(board);
            getBoardCategoryInfo = new ResGetBoardListDTO.GetBoardCategoryInfo(board, boardCategories );
            getBoardCategoryInfos.add(getBoardCategoryInfo);

        }

        //case 2
       /* for (int i=0; i<boards.size(); i++) {
            List<BoardCategory> boardCategories = boardCategoryRepository.findByBoard(boards.get(i));
            getBoardCategoryInfo = new ResGetBoardListDTO.GetBoardCategoryInfo(boards.get(i), boardCategories);
            getBoardCategoryInfos.add(getBoardCategoryInfo);
        }*/

        return new ResGetBoardListDTO(getBoardCategoryInfos);

    }

    /**
     * 게시글 목록 : 키워드 검색 조회 - Service
     * @param userId
     * @param searchStatus
     * @param searchValue
     * @return
     */
    public ResGetBoardListDTO getKeywordSearchBoardList(long userId, String searchStatus, String searchValue) {

        List<Board> boards;

        if(searchValue == "" || searchValue == null){
            boards = boardRepository.findAllBoardList(userId);
        } else {

            switch (searchStatus) {
                case "all": //전체
                    boards = boardRepository.findAllSearchBoardList(userId, searchValue);
                    break;
                case "writer": //작성자
                    boards = boardRepository.findAllUserIdSearchBoardList(userId, searchValue);
                    break;
                case "title": //제목
                    boards = boardRepository.findAllTitleSearchBoardList(userId, searchValue);
                    break;
                case "contents": //내용
                    boards = boardRepository.findAllContentsSearchBoardList(userId, searchValue);
                    break;
                default:
                    throw new DkargoException(ErrorCode.KEYWORD_SEARCH_STATUS_BAD_REQUEST);
            }
        }

        ResGetBoardListDTO.GetBoardCategoryInfo getBoardCategoryInfo;
        List<ResGetBoardListDTO.GetBoardCategoryInfo> getBoardCategoryInfos = new ArrayList<>();

        for (Board board : boards) {
            List<BoardCategory> boardCategories = boardCategoryRepository.findByBoard(board);
            getBoardCategoryInfo = new ResGetBoardListDTO.GetBoardCategoryInfo(board, boardCategories);
            getBoardCategoryInfos.add(getBoardCategoryInfo);
        }

        return  new ResGetBoardListDTO(getBoardCategoryInfos);
    }

    /**
     * 게시글 목록 : 카테고리 검색 조회 - Service
     * @param userId
     * @param categoryList
     * @return
     */
    public ResGetBoardListDTO getCategorySearchBoardList(long userId, List<Long> categoryList) {

        List<Board> boards = new ArrayList<>();

        //검색한 카테고리 갯수에 따라 조회 파라미터 지정
        switch (categoryList.size()){
            case 1 :
                //값이 하나이고 "전체" 일 때
                if(categoryList.get(0) == 1){
                    List<BoardCategory> allSearchBoardCategories = boardCategoryRepository.findAllSearchBoardCategoryList();
                    for(int i=0; i<allSearchBoardCategories.size(); i++) {
                        boards.add(boardRepository.findById(allSearchBoardCategories.get(i).getBoard().getBoardId()).orElseThrow(() -> new DkargoException(ErrorCode.BOARD_ID_BAD_REQUEST)));
                    }
                    break;
                }
                else {
                    List<BoardCategory> oneSearchBoardCategories = boardCategoryRepository.findAllOneSearchBoardCategoryList(categoryList.get(0));
                    for(int i=0; i<oneSearchBoardCategories.size(); i++) {
                        boards.add(boardRepository.findById(oneSearchBoardCategories.get(i).getBoard().getBoardId()).orElseThrow(() -> new DkargoException(ErrorCode.BOARD_ID_BAD_REQUEST)));
                    }
                    break;
                }
            case 2 :
                List<BoardCategory> twoSearchBoardCategories = boardCategoryRepository.findAllTwoSearchBoardCategoryList(categoryList.get(0), categoryList.get(1));
                for(int i=0; i<twoSearchBoardCategories.size(); i++) {
                    boards.add(boardRepository.findById(twoSearchBoardCategories.get(i).getBoard().getBoardId()).orElseThrow(() -> new DkargoException(ErrorCode.BOARD_ID_BAD_REQUEST)));
                }
                break;
            case 3 :
                List<BoardCategory> threeSearchBoardCategories = boardCategoryRepository.findAllThreeSearchBoardCategoryList(categoryList.get(0), categoryList.get(1), categoryList.get(2));
                for(int i=0; i<threeSearchBoardCategories.size(); i++) {
                    boards.add(boardRepository.findById(threeSearchBoardCategories.get(i).getBoard().getBoardId()).orElseThrow(() -> new DkargoException(ErrorCode.BOARD_ID_BAD_REQUEST)));
                }
                break;
            default:
                throw new DkargoException(ErrorCode.SELECT_CATEGORY_LIST_BAD_REQUEST);
        }

        ResGetBoardListDTO.GetBoardCategoryInfo getBoardCategoryInfo;
        List<ResGetBoardListDTO.GetBoardCategoryInfo> getBoardCategoryInfos = new ArrayList<>();

        for (Board board : boards) {
            List<BoardCategory> boardCategories = boardCategoryRepository.findByBoard(board);
            getBoardCategoryInfo = new ResGetBoardListDTO.GetBoardCategoryInfo(board, boardCategories);
            getBoardCategoryInfos.add(getBoardCategoryInfo);
        }

        return  new ResGetBoardListDTO(getBoardCategoryInfos);

    }

    /**
     * 상세정보 조회 - Service
     * @param boardId
     * @return
     */
    @Transactional
    public ResGetBoardDtailDTO getBoardDtail (long boardId) {
        //조회수 +1
        boardRepository.updateViews(boardId);
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new DkargoException(ErrorCode.BOARD_ID_BAD_REQUEST));

        //해당 게시글의 카테고리 정보
        List<BoardCategory> boardCategories = boardCategoryRepository.findByBoard(board);
        //해당 게시글의 파일첨부 정보
        List<Attachments> attachments = attachmentsRepository.findAttachmentsList(board);


        return new ResGetBoardDtailDTO(board, boardCategories , attachments);

    }

    /**
     * 게시물 소유권 확인 - Service
     * @param boardId
     * @param userId
     * @return
     */
    public boolean getBoardOwnerConfirm(long boardId, long userId) {

        boolean confirmValue = boardRepository.existsByBoardIdAndUserId(boardId, userId);

        return confirmValue;

    }

    /**
     * 게시물의 기본 정보 및 상태 값 저장 - Service
     * @param reqCreateBoardDTO
     * @return
     */
    public ResCreateBoardDTO createBoard(ReqCreateBoardDTO reqCreateBoardDTO) {

        long contentsByteSize = reqCreateBoardDTO.getContents().getBytes(StandardCharsets.UTF_8).length;

        if(contentsByteSize > 50000){
            throw new DkargoException(ErrorCode.BOARD_CONTENTS_BAD_REQUEST);
        }else{
            Board board = new Board(reqCreateBoardDTO);
            boardRepository.save(board);

            return new ResCreateBoardDTO(board);
        }

    }

    /**
     * 게시물 비밀번호 활성화 여부 확인 - Service
     * @param boardId
     * @return
     */

    public FlagType ResGetBoardPasswordFlag (long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new DkargoException(ErrorCode.BOARD_ID_BAD_REQUEST));

        if(board.getPwActiveFlag().equals(FlagType.Y)) {
            return FlagType.Y; //활성화
        }else {
            return FlagType.N; //비활성화
        }

    }

    /**
     * 게시글 수정 - Service
     * @param reqUpdateBoardDTO
     * @return
     */
    public ResUpdateBoardDTO updateBoard(ReqUpdateBoardDTO reqUpdateBoardDTO) {
        Board board = boardRepository.findById(reqUpdateBoardDTO.getBoardId()).orElseThrow(() -> new DkargoException(ErrorCode.BOARD_ID_BAD_REQUEST));

        //자신의 글이 맞는지 확인
        if(board.getUserId() == reqUpdateBoardDTO.getUserId()) {

            board.setBoardId(board.getBoardId());
            board.setUserId(board.getUserId());
            board.setTitle(reqUpdateBoardDTO.getTitle());
            board.setContents(reqUpdateBoardDTO.getContents());
            board.setViews(board.getViews());
            board.setPwActiveFlag(reqUpdateBoardDTO.getPwActiveFlag());
            board.setAttachmentsFlag(reqUpdateBoardDTO.getAttachmentsFlag());
            board.setPrivateFlag(reqUpdateBoardDTO.getPrivateFlag());
            board.setCommentFlag(reqUpdateBoardDTO.getCommentFlag());
            board.setDeleteFlag(board.getDeleteFlag());
            board.setCreateAt(board.getCreateAt());
            board.setUpdateAt(LocalDateTime.now());
            board.setDeleteAt(null);

            boardRepository.save(board);

        } else {
            throw new DkargoException(ErrorCode.BOARD_FORBIDDEN);
        }

        return new ResUpdateBoardDTO(board);
    }

    /**
     * 게시물 기본정보 및 상태 값 수정하여 삭제 - Service
     * @param reqDeleteBoardDTO
     * @return
     */
    public ResDeleteBoardDTO deleteBoard(ReqDeleteBoardDTO reqDeleteBoardDTO) {
        Board board = boardRepository.findById(reqDeleteBoardDTO.getBoardId()).orElseThrow(() -> new DkargoException(ErrorCode.BOARD_ID_BAD_REQUEST));

        //자신의 글이 맞는지 확인 후 (deleteFlag, deleteAt) 수정
        if(board.getUserId() == reqDeleteBoardDTO.getUserId()) {
            board.setBoardId(board.getBoardId());
            board.setUserId(board.getUserId());
            board.setTitle(board.getTitle());
            board.setContents(board.getContents());
            board.setViews(board.getViews());
            board.setPwActiveFlag(board.getPwActiveFlag());
            board.setAttachmentsFlag(FlagType.N);
            board.setPrivateFlag(board.getPrivateFlag());
            board.setCommentFlag(FlagType.N);
            board.setDeleteFlag(FlagType.Y);
            board.setCreateAt(board.getCreateAt());
            board.setUpdateAt(board.getUpdateAt());
            board.setDeleteAt(LocalDateTime.now());

            boardRepository.save(board);

        } else {
            throw new DkargoException(ErrorCode.BOARD_FORBIDDEN);
        }

        return new ResDeleteBoardDTO(board);
    }

}

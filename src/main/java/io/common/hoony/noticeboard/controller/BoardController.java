package io.common.hoony.noticeboard.controller;

import io.common.hoony.noticeboard.common.type.FlagType;
import io.common.hoony.noticeboard.domain.dto.board.request.ReqCreateBoardDTO;
import io.common.hoony.noticeboard.domain.dto.board.request.ReqDeleteBoardDTO;
import io.common.hoony.noticeboard.domain.dto.board.request.ReqUpdateBoardDTO;
import io.common.hoony.noticeboard.domain.dto.board.response.*;
import io.common.hoony.noticeboard.domain.dto.comment.request.ReqDeleteCommentListDTO;
import io.common.hoony.noticeboard.domain.dto.password.response.ResGetBoardPasswordConfirmDTO;
import io.common.hoony.noticeboard.service.*;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;
    private final BoardPasswordService boardPasswordService;
    private final BoardCategoryService boardCategoryService;
    private final CommentService commentService;
    private final AttachmentsService attachmentsService;

    /**
     * 게시글 목록 : 초기 조회 - Controller
     * @param userId
     * @return
     */
    @ApiOperation(
            value = "게시글 목록 : 초기 조회",
            notes = "- 최초 게시물 목록을 보여줍니다.\t\n" +
                    "- 키워드 = '전체'/'', 카테고리 = '전체'"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "사용자 ID", required = true, dataType = "long", paramType =  "path")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = ResGetBoardListDTO.class)
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResGetBoardListDTO getBoardList(@PathVariable("userId") long userId) {
        //초기 조회
        ResGetBoardListDTO result = boardService.getBoardList(userId);
        return result;

    }

    /**
     * 게시글 목록 : 키워드/카테고리 검색 조회 - Controller
     * @param userId
     * @param searchStatus
     * @param searchValue
     * @param categoryIdList
     * @return
     */
    @ApiOperation(
            value = "게시글 목록 : 키워드/카테고리 검색 조회",
            notes = "- 키워드와 카테고리 지정을 통해 게시물 목록을 보여줍니다.\t\n" +
                    "- 키워드는 전체, 작성자, 제목, 내용 중 하나를 선택하고, 키워드 입력 값을 통해 조회합니다.\t\n" +
                    "- 카테고리는 최소 1개이상, 최대 3개이하 지정이 가능합니다."
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "사용자 ID", required = true, dataType = "long", paramType =  "path"),
            @ApiImplicitParam(name = "searchStatus", value = "키워드 메뉴", required = true, dataType = "string", paramType =  "query"),
            @ApiImplicitParam(name = "searchValue", value = "키워드", required = false, dataType = "string", paramType =  "query"),
            @ApiImplicitParam(name = "categoryIdList", value = "카테고리 List", required = true, dataType = "string", paramType =  "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = ResGetBoardListDTO.class)
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/search/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResGetBoardListDTO getSearchBoardList(@PathVariable("userId") long userId,
                                                                 @RequestParam("searchStatus") String searchStatus,
                                                                 @RequestParam(name = "searchValue", required = false) String searchValue,
                                                                 @RequestParam List<Long> categoryIdList) {

        //게시글 목록 : 키워드 검색 조회
        ResGetBoardListDTO keywordSearchList = boardService.getKeywordSearchBoardList(userId, searchStatus, searchValue);
        //게시글 목록 : 카테고리 검색 조회
        ResGetBoardListDTO categorySearchList = boardService.getCategorySearchBoardList(userId, categoryIdList);

        ResGetBoardListDTO result ;
        result = keywordSearchList;
        result.getList().retainAll(categorySearchList.getList());

        return result;

    }

    /**
     * 특정 게시글 상태 확인 - Controller
     * @param boardId
     * @param userId
     * @return
     */
    @ApiOperation(
            value = "게시물 상태 확인",
            notes = "- Return 값에 따라 게시물의 소유권 및 특정 상태를 확인할 수 있습니다.\t\n" +
                    "- 'me'(자신의 글) : 자신의 글은 비밀번호가 활성화 되어있어도 별도의 비밀번호입력 없이 게시물을 확인할 수 있고, 수정/삭제 기능이 활성화 됩니다.\t\n" +
                    "- 'other'(다른 사용자의 글) : 다른 사용자의 글로 게시된 내용만 확인 할 수 있습니다.\t\n" +
                    "- 'password'(다른 사용자의 글 + 비밀번호 활성화) : 다른 사용자의 글이면서 비밀번호가 활성화 되어있어, 비밀번호 입력을 통해 게시물을 확인 할 수 있습니다."
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "boardId", value = "게시글 ID", required = true, dataType = "long", paramType =  "query"),
            @ApiImplicitParam(name = "userId", value = "사용자 ID", required = true, dataType = "long", paramType =  "query"),
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = ResGetBoardListDTO.class)
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/status", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResGetBoardStatusDTO getBoardStatus(@RequestParam("boardId") long boardId, @RequestParam("userId") long userId) {

        //게시물 소유권 확인
        boolean confirmValue = boardService.getBoardOwnerConfirm(boardId, userId);

        String status;

        if(confirmValue) {
            status = "me"; //자신의 작성글 (자신의 글일때는 각 플레그에 상관없이 모든 정보를 다보여준다.)
            ResGetBoardStatusDTO result = new ResGetBoardStatusDTO(status);
            return result;
        } else {
            //게시물 비밀번호 활성화 여부 확인
            FlagType flag = boardService.ResGetBoardPasswordFlag(boardId);

            if(flag == FlagType.Y) {
                status = "password"; //비밀번호 활성화, 다른사람의 글 (이때는 비밀번호 입력 후 맞으면, 각플래그 값에 따라 보여준다.)
                ResGetBoardStatusDTO result = new ResGetBoardStatusDTO(status);
                return result;
            }else {
                status = "other"; //다른사람의 글 (플래그 값에 따라 보여준다.)
                ResGetBoardStatusDTO result = new ResGetBoardStatusDTO(status);
                return result;
            }

        }
    }

    /**
     * 게시물 상세조회 - Controller
     * @param boardId
     * @return
     */
    @ApiOperation(
            value = "게시물 상세 조회",
            notes = "- 선택 된 게시물의 상세 정보를 조회합니다.\t\n" +
                    "- 상세 조회 시 조회수가 1씩 증가합니다."
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "boardId", value = "게시글 ID", required = true, dataType = "long", paramType =  "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = ResGetBoardListDTO.class)
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/detail" , produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResGetBoardDtailDTO getBoardDtail(@RequestParam("boardId") long boardId) {

        ResGetBoardDtailDTO result = boardService.getBoardDtail(boardId);
        return result;
    }

    /**
     * 게시물 생성 - Controller
     * @param reqCreateBoardDTO
     * @return
     */
    @ApiOperation(
            value = "게시물 생성",
            notes = "- 제목,내용 처럼 기본적인 정보들 뿐 아니라, 카테고리, 비밀번호, 여러 활성화 등 값을 함께 입력합니다.\t\n" +
                    "- 비밀번호는 활성화를 안해도 NULL 값으로 DB에 저장합니다.\t\n" +
                    "- 비밀번호 암호화 시 SHA256를 사용합니다.\t\n" +
                    "- 카테고리는 최소 1개, 최대 3개까지 지정해야 게시물이 생성됩니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = ResGetBoardListDTO.class)
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public ResCreateBoardDTO createBoard(@Validated @RequestBody ReqCreateBoardDTO reqCreateBoardDTO) {

        //게시물의 기본 정보 및 상태 값 저장
        ResCreateBoardDTO result = boardService.createBoard(reqCreateBoardDTO);
        //저장된 게시물의 비밀번호 암호화 및 정보 저장
        boardPasswordService.createPassword(result, reqCreateBoardDTO.getPassword());
        //저장된 게시물의 카테고리 정보 저장
        boardCategoryService.createCategory(result, reqCreateBoardDTO);

        return result;

    }

    /**
     * 비밀번호 확인 - Controller
     * @param resGetBoardPasswordConfirmDTO
     * @return
     */
    @ApiOperation(
            value = "게시물 비밀번호 확인",
            notes = "- 비밀번호가 활성화된 다른 사용자의 게시물을 조회 할 때 비밀번호를 확인합니다.\t\n" +
                    "- SHA256암호화를 통해 암호화된 값을 서로 비교합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = ResGetBoardListDTO.class)
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/password")
    public Boolean getBoardPasswordConfirm(@Validated @RequestBody ResGetBoardPasswordConfirmDTO resGetBoardPasswordConfirmDTO) {

        boolean result = boardPasswordService.getBoardPasswordConfirm(resGetBoardPasswordConfirmDTO);
        return result;

    }

    /**
     * 게시글 수정 - Controller
     * @param reqUpdateBoardDTO
     * @return
     */
    @ApiOperation(
            value = "게시물 수정",
            notes = "- 기존에 작성했던 게시물 정보를 수정합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = ResGetBoardListDTO.class)
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/detail")
    public ResUpdateBoardDTO updateBoard(@RequestBody ReqUpdateBoardDTO reqUpdateBoardDTO) {
        //게시물의 기본 정보 및 상태 값 수정
        ResUpdateBoardDTO result = boardService.updateBoard(reqUpdateBoardDTO);
        //저장된 게시물의 비밀번호 암호화 및 정보 수정
        boardPasswordService.updatePassword(result, reqUpdateBoardDTO);
        //저장된 게시물의 카테고리 정보 수정 (다중선택가능하며, 1개 이상 / 3개 이하)
        boardCategoryService.updateCategory(result, reqUpdateBoardDTO);

        return result;

    }

    /**
     * 게시글 삭제 - Controller
     * @param reqDeleteBoardDTO
     * @return
     */
    @ApiOperation(
            value = "게시물 삭제",
            notes = "- 기존에 작성했던 게시물을 삭제합니다.\t\n" +
                    "- 실제로 DB에서 삭제하지 않고, 게시판 Table에서 삭제여부 상태값과, 삭제 날짜를 변경합니다.\t\n" +
                    "- 특정 게시물이 삭제되면 그에 따른 댓글/답글, 첨부파일 정보도 함께 삭제여부 상태값, 삭제 날짜를 변경합니다."

    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = ResGetBoardListDTO.class)
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/detail")
    public ResDeleteBoardDTO deleteBoard(@RequestBody ReqDeleteBoardDTO reqDeleteBoardDTO) {

        //게시물 기본정보 및 상태 값 수정하여 삭제
        ResDeleteBoardDTO result = boardService.deleteBoard(reqDeleteBoardDTO);

        //삭제된 게시물의 전체 카테고리 값 영구 삭제
        boardCategoryService.deleteCategory(reqDeleteBoardDTO.getBoardId());

        //삭제된 게시물의 전체 첨부파일 상태 값 수정하여 삭제
        attachmentsService.deleteAttachments(reqDeleteBoardDTO.getBoardId());

        //삭제된 게시물의 전체 댓글/답글 상태 값 수정하여 삭제
        ReqDeleteCommentListDTO reqDeleteCommentListDTO = new ReqDeleteCommentListDTO(reqDeleteBoardDTO.getBoardId());
        commentService.deleteComments(reqDeleteCommentListDTO);

        return result;
    }

}

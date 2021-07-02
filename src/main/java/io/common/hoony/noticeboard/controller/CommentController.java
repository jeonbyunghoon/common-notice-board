package io.common.hoony.noticeboard.controller;

import io.common.hoony.noticeboard.domain.dto.board.response.ResGetBoardListDTO;
import io.common.hoony.noticeboard.domain.dto.comment.request.ReqCreateCommentDTO;
import io.common.hoony.noticeboard.domain.dto.comment.request.ReqCreateReplyDTO;
import io.common.hoony.noticeboard.domain.dto.comment.request.ReqDeleteCommentDTO;
import io.common.hoony.noticeboard.domain.dto.comment.request.ReqUpdateCommentDTO;
import io.common.hoony.noticeboard.domain.dto.comment.response.*;
import io.common.hoony.noticeboard.service.CommentService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글/답글 목록 조회 - Controller
     * @param boardId
     * @return
     */
    @ApiOperation(
            value = "댓글/답글 목록 조회",
            notes = "- 게시물에 포함한 댓글/답글을 조회합니다.\t\n" +
                    "- 계층형으로 같은 계층안에서는 최신 작성순으로 상단에 나타냅니다.\t\n" +
                    "- 계층에는 제한이 없습니다. (UI에 따라 추후 변경)\t\n" +
                    "- 계층은 commentLayer 값으로 지정합니다."
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "boardId", value = "게시글 ID", required = true, dataType = "long", paramType =  "path")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = ResGetBoardListDTO.class)
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{boardId}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResGetCommentListDTO getCommentList(@PathVariable("boardId") long boardId) {
        //댓글/답글 목록 조회
        ResGetCommentListDTO result = commentService.getCommentList(boardId);

        return result;

    }

    /**
     * 댓글 작성 - Controller
     * @param reqCreateCommentDTO
     * @return
     */
    @ApiOperation(
            value = "댓글 생성",
            notes = "- 게시물의 최상위 댓글을 작성합니다.\t\n" +
                    "- 이후로 해당 댓글에 답글은 /reply API를 통해 작성합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = ResGetBoardListDTO.class)
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public ResCreateCommentDTO createComment(@Validated @RequestBody ReqCreateCommentDTO reqCreateCommentDTO) {

        ResCreateCommentDTO result = commentService.creatComment(reqCreateCommentDTO);

        return result;

    }

    /**
     * 답글 작성 - Controller
     * @param reqCreateReplyDTO
     * @return
     */
    @ApiOperation(
            value = "답글 생성",
            notes = "- 게시물의 최상위 댓글에 대한 답글을 작성합니다.\t\n" +
                    "- 댓글(최상위)를 제외한 모든 답글은 해당 API를 통해 작성합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = ResGetBoardListDTO.class)
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/reply")
    public ResCreateReplyDTO createReply(@Validated @RequestBody ReqCreateReplyDTO reqCreateReplyDTO) {

        ResCreateReplyDTO result = commentService.createReply(reqCreateReplyDTO);

        return result;

    }

    /**
     * 댓글/답글 수정 - Controller
     * @param reqUpdateCommentDTO
     * @return
     */
    @ApiOperation(
            value = "댓글/답글 수정",
            notes = "- 댓글과 답글의 정보를 수정합니다.\t\n" +
                    "- 답글/댓글 작성 API는 각각 분리되어있지만, 수정의 경우 해당 API를 함께 사용합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = ResGetBoardListDTO.class)
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public ResUpdateCommentDTO updateComment(@Validated @RequestBody ReqUpdateCommentDTO reqUpdateCommentDTO) {

        ResUpdateCommentDTO result = commentService.updateComment(reqUpdateCommentDTO);

        return result;
    }

    /**
     * 댓글/답글 삭제 - Controller
     * @param reqDeleteCommentDTO
     * @return
     */
    @ApiOperation(
            value = "댓글/답글 삭제",
            notes = "- 댓글/답글의 자식 유무 확인 후 삭제 진행\t\n" +
                    "- 선택한 댓글/답글을 삭제합니다.\t\n" +
                    "- 실제로 DB에서 삭제하지 않고, 댓글 Table에서 삭제여부 상태값과, 삭제 날짜를 변경합니다.\t\n" +
                    "- 자식 여부가 true이면 삭제할 수 없습니다."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = ResGetBoardListDTO.class)
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping
    public ResDeleteCommentDTO deleteComment(@Validated @RequestBody ReqDeleteCommentDTO reqDeleteCommentDTO) {
        //선택된 하나의 댓글/답글 삭제
        ResDeleteCommentDTO result = commentService.deleteComment(reqDeleteCommentDTO);

        return result;

    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    //    /**
//     * 댓글/답글의 자식여부 확인 - Controller (삭제되지 않은 자식 여부 확인)
//     * @param boardId
//     * @param commentGroup
//     * @param commentOrder
//     * @param commentLayer
//     * @return
//     */
//    @ApiOperation(
//            value = "댓글/답글 자식여부 확인",
//            notes = "- 선택한 댓글과 답글에 하위 답글이 있는지 확인합니다.\t\n" +
//                    "- 하위 답글 여부를 통해 결과적으로 해당 댓글/답글 삭제 여부를 판단합니다.\t\n" +
//                    "- Return 값이 true이면 자식 여부가 있기때문에 삭제가 불가능하고, false이면 삭제가 가능합니다."
//    )
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "boardId", value = "게시글 ID", required = true, dataType = "long", paramType =  "path"),
//            @ApiImplicitParam(name = "commentGroup", value = "댓글/답글 그룹", required = true, dataType = "integer", paramType =  "query"),
//            @ApiImplicitParam(name = "commentOrder", value = "그룹별 댓글/답글 순서", required = true, dataType = "integer", paramType =  "query"),
//            @ApiImplicitParam(name = "commentLayer", value = "댓글/답글의 계층", required = true, dataType = "integer", paramType =  "query")
//    })
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "성공", response = ResGetBoardListDTO.class)
//    })
//    @ResponseStatus(HttpStatus.OK)
//    @GetMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
//    public Boolean getReplyConfirm(@RequestParam("boardId") long boardId,
//                                                   @RequestParam("commentGroup") int commentGroup,
//                                                   @RequestParam("commentOrder") int commentOrder,
//                                                   @RequestParam("commentLayer") int commentLayer) {
//
//        boolean result = commentService.getReplyConfirm(boardId, commentGroup, commentOrder, commentLayer);
//
//        return result;
//    }

}

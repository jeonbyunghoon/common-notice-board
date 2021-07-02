package io.common.hoony.noticeboard.service;

import io.common.hoony.noticeboard.common.type.FlagType;
import io.common.hoony.noticeboard.domain.dto.comment.request.*;
import io.common.hoony.noticeboard.domain.dto.comment.response.*;
import io.common.hoony.noticeboard.domain.entity.Board;
import io.common.hoony.noticeboard.domain.entity.Comment;
import io.common.hoony.noticeboard.error.DkargoException;
import io.common.hoony.noticeboard.error.ErrorCode;
import io.common.hoony.noticeboard.repository.AttachmentsRepository;
import io.common.hoony.noticeboard.repository.BoardRepository;
import io.common.hoony.noticeboard.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    /**
     * 댓글/답글 목록 조회 - Service
     * @param boardId
     * @return
     */
    public ResGetCommentListDTO getCommentList(Long boardId) {

        FlagType deleteFlag = FlagType.N;

        List<Comment> comments = commentRepository.findAllCommentList(boardId, deleteFlag);

        return  new ResGetCommentListDTO(comments);
    }

    /**
     * 댓글 작성 - Service
     * @param reqCreateCommentDTO
     * @return
     */
    @Transactional
    public ResCreateCommentDTO creatComment (ReqCreateCommentDTO reqCreateCommentDTO) {

        Board board = boardRepository.findById(reqCreateCommentDTO.getBoardId()).orElseThrow(() -> new DkargoException(ErrorCode.BOARD_ID_BAD_REQUEST));
        //댓글의 1차 저장 (commentGroup 값을 일단 0으로 Save, 이후 로직에서 CommentGroup 값 변경)
        Comment temporaryComment = new Comment(reqCreateCommentDTO, board);
        commentRepository.save(temporaryComment);

        //방금 만든 temporaryComment의 ID 값을 commentGroup 값에 넣어 업데이트한다.
        Long latestCommentId = commentRepository.latestCommentId();
        int intLatestCommentId = latestCommentId.intValue();

        commentRepository.commentGroupUpdate(intLatestCommentId, latestCommentId);
        Comment comment = commentRepository.findById(latestCommentId).orElseThrow(() -> new DkargoException(ErrorCode.COMMENT_ID_BAD_REQUEST));

        return new ResCreateCommentDTO(comment);

    }

    /**
     * 답글 작성 - Service
     * @param reqCreateReplyDTO
     * @return
     */
    @Transactional
    public ResCreateReplyDTO createReply(ReqCreateReplyDTO reqCreateReplyDTO) {

        Board board = boardRepository.findById(reqCreateReplyDTO.getBoardId()).orElseThrow(() -> new DkargoException(ErrorCode.BOARD_ID_BAD_REQUEST));
        Comment comment = new Comment(reqCreateReplyDTO, board);

        if(commentRepository.existsByBoardAndCommentGroup(board, reqCreateReplyDTO.getCommentGroup())) {
            //답글 순서 변경
            commentRepository.replyOrderUpdate(reqCreateReplyDTO.getCommentGroup(), reqCreateReplyDTO.getCommentOrder());
            //답글 작성
            commentRepository.save(comment);

            return new ResCreateReplyDTO(comment);
        }else {
            throw new DkargoException(ErrorCode.COMMENT_BAD_REQUEST);
        }
    }

    /**
     * 댓글/답글 수정 - Service (댓글 ID, 게시판 ID, 작성자 ID 값으로 확인 후 업데이트)
     * @param reqUpdateCommentDTO
     * @return
     */
    @Transactional
    public ResUpdateCommentDTO updateComment(ReqUpdateCommentDTO reqUpdateCommentDTO) {

        LocalDateTime updateAt = LocalDateTime.now();

        commentRepository.updateComment(reqUpdateCommentDTO.getCommentId(),
                                        reqUpdateCommentDTO.getBoardId(),
                                        reqUpdateCommentDTO.getUserId().longValue(),
                                        reqUpdateCommentDTO.getContents(),
                                        updateAt);

        Comment comment = commentRepository.findById(reqUpdateCommentDTO.getCommentId()).orElseThrow(() -> new DkargoException(ErrorCode.COMMENT_ID_BAD_REQUEST));

        return new ResUpdateCommentDTO(comment);
    }

    /**
     * 선택된 하나의 댓글/답글 삭제 - Service
     * @param reqDeleteCommentDTO
     * @return
     */
    @Transactional
    public ResDeleteCommentDTO deleteComment(ReqDeleteCommentDTO reqDeleteCommentDTO) {

        Board board = boardRepository.findById(reqDeleteCommentDTO.getBoardId()).orElseThrow(() -> new DkargoException(ErrorCode.BOARD_ID_BAD_REQUEST));

        //자식 계층 변수 확인
        int commentOrderConfirmValue = reqDeleteCommentDTO.getCommentOrder() + 1;
        int commentLayerConfirmValue = reqDeleteCommentDTO.getCommentLayer() + 1;
        int commentGroup = reqDeleteCommentDTO.getCommentGroup();
        FlagType deleteFlag1 = FlagType.N;

        boolean confirmValue = commentRepository.existsByBoardAndCommentGroupAndCommentOrderAndCommentLayerAndDeleteFlag(board, commentGroup, commentOrderConfirmValue, commentLayerConfirmValue, deleteFlag1);

        if(confirmValue){
            throw  new DkargoException(ErrorCode.COMMENT_FORBIDDEN);
        }else {
           FlagType deleteFlag2 = FlagType.Y;
            LocalDateTime deleteAt = LocalDateTime.now();

            commentRepository.deleteComment(reqDeleteCommentDTO.getCommentId(),
                    reqDeleteCommentDTO.getBoardId(),
                    reqDeleteCommentDTO.getUserId(),
                    deleteFlag2,
                    deleteAt);

            Comment comment = commentRepository.findById(reqDeleteCommentDTO.getCommentId()).orElseThrow(() -> new DkargoException(ErrorCode.COMMENT_ID_BAD_REQUEST));

            return  new ResDeleteCommentDTO(comment);
        }

    }

    /**
     * 삭제된 게시물의 댓글/답글 상태 값 수정하여 삭제 - Service
     * @param reqDeleteCommentListDTO
     */
    @Transactional
    public void deleteComments(ReqDeleteCommentListDTO reqDeleteCommentListDTO) {

        FlagType deleteFlag = FlagType.Y;
        LocalDateTime deleteAt = LocalDateTime.now();

        commentRepository.deleteComments(reqDeleteCommentListDTO.getBoardId(), deleteFlag, deleteAt);

    }

//    /**
//     * 댓글/답글의 자식여부 확인 - Service (삭제되지 않은 자식 여부 확인)
//     * @param boardId
//     * @param commentGroup
//     * @param commentOrder
//     * @param commentLayer
//     * @return
//     */
//    public boolean getReplyConfirm(long boardId,
//                                   int commentGroup,
//                                   int commentOrder,
//                                   int commentLayer) {
//
//        Board board = boardRepository.findById(boardId).orElse(null);
//
//
//        //자식 계층 획인 변수
//        int commentOrderConfirmValue = commentOrder + 1;
//        int commentLayerConfirmValue = commentLayer + 1;
//        FlagType deleteFlag = FlagType.N;
//
//        boolean confirmValue = commentRepository.existsByBoardAndCommentGroupAndCommentOrderAndCommentLayerAndDeleteFlag(board, commentGroup, commentOrderConfirmValue, commentLayerConfirmValue, deleteFlag);
//        return confirmValue;
//
//    }

}

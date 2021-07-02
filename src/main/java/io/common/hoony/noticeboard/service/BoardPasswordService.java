package io.common.hoony.noticeboard.service;

import io.common.hoony.noticeboard.common.encryption.EncryptionUtils;
import io.common.hoony.noticeboard.common.type.FlagType;
import io.common.hoony.noticeboard.domain.dto.board.request.ReqUpdateBoardDTO;
import io.common.hoony.noticeboard.domain.dto.board.response.ResCreateBoardDTO;
import io.common.hoony.noticeboard.domain.dto.board.response.ResUpdateBoardDTO;
import io.common.hoony.noticeboard.domain.dto.password.response.ResGetBoardPasswordConfirmDTO;
import io.common.hoony.noticeboard.domain.entity.Board;
import io.common.hoony.noticeboard.domain.entity.BoardPassword;
import io.common.hoony.noticeboard.error.DkargoException;
import io.common.hoony.noticeboard.error.ErrorCode;
import io.common.hoony.noticeboard.repository.BoardPasswordRepository;
import io.common.hoony.noticeboard.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardPasswordService {

    private final BoardRepository boardRepository;
    private final BoardPasswordRepository boardPasswordRepository;


    /**
     * 저장된 게시물의 비밀번호 암호화(SHA-256) 및 정보 저장 - Service
     * @param resCreateBoardDTO
     * @param password
     */
    public void createPassword(ResCreateBoardDTO resCreateBoardDTO, String password) {

        BoardPassword boardPassword = new BoardPassword(password);
        Board board = boardRepository.findById(resCreateBoardDTO.getBoardId())
                .orElseThrow(() -> new DkargoException(ErrorCode.BOARD_ID_BAD_REQUEST));
        boardPassword.setBoard(board);
        //비밀번호 여부가 비활성화 시 무조건 NULL 값
        if(resCreateBoardDTO.getPwActiveFlag() == FlagType.N){
            boardPassword.setPassword(null);
        } else {
            boardPassword.setPassword(EncryptionUtils.encryptSHA256(boardPassword.getPassword()));
        }

        boardPasswordRepository.save(boardPassword);

    }

    /**
     * 특정 게시물의 비밀번호 확인 - Service (암호화 후 서로 비교, 복호화 X)
     * @param resGetBoardPasswordConfirmDTO
     * @return
     */
    public boolean getBoardPasswordConfirm(ResGetBoardPasswordConfirmDTO resGetBoardPasswordConfirmDTO) {

        Board board = boardRepository.findById(resGetBoardPasswordConfirmDTO.getBoardId())
                .orElseThrow(() -> new DkargoException(ErrorCode.BOARD_ID_BAD_REQUEST));
        BoardPassword boardPassword = boardPasswordRepository.findByBoard(board).orElseThrow(() -> new DkargoException(ErrorCode.BOARD_BAD_REQUEST));

        return boardPassword.getPassword().equals(EncryptionUtils.encryptSHA256(resGetBoardPasswordConfirmDTO.getPassword()));
    }

    /**
     * 비밀번호 업데이트 - Service (암호화 기능 - SHA-256 사용 가능)
     * @param resUpdateBoardDTO
     */
    public void updatePassword(ResUpdateBoardDTO resUpdateBoardDTO, ReqUpdateBoardDTO reqUpdateBoardDTO) {

        Board board = boardRepository.findById(resUpdateBoardDTO.getBoardId()).orElseThrow(() -> new DkargoException(ErrorCode.BOARD_ID_BAD_REQUEST));
        BoardPassword boardPassword = boardPasswordRepository.findByBoard(board).orElseThrow(() -> new DkargoException(ErrorCode.BOARD_PASSWORD_ID_BAD_REQUEST));

        if (board.getUserId() == reqUpdateBoardDTO.getUserId()) {
            //비밀번호 여부가 비활성화 시 무조건 NULL 값
            if(resUpdateBoardDTO.getPwActiveFlag() == FlagType.N) {
                boardPassword.setPasswordId(boardPassword.getPasswordId());
                boardPassword.setBoard(boardPassword.getBoard());
                boardPassword.setPassword(null);
                boardPassword.setCreateAt(boardPassword.getCreateAt());
                boardPassword.setUpdateAt(LocalDateTime.now());
            } else {
                boardPassword.setPasswordId(boardPassword.getPasswordId());
                boardPassword.setBoard(boardPassword.getBoard());
                boardPassword.setPassword(EncryptionUtils.encryptSHA256(reqUpdateBoardDTO.getPassword()));
                boardPassword.setCreateAt(boardPassword.getCreateAt());
                boardPassword.setUpdateAt(LocalDateTime.now());
            }

            boardPasswordRepository.save(boardPassword);
        }else {
            throw new DkargoException(ErrorCode.BOARD_FORBIDDEN);
        }

    }

}

package io.common.hoony.noticeboard.error;

import io.common.hoony.noticeboard.domain.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    CATEGORY_LIST_NO_CONTENT(204, 20001, "No contents (카테고리 목록 정보가 없습니다.)"),
    FILE_TYPE_NO_CONTENT(204, 20002, "No contents (파일 형식을 찾을 수 없습니다.)"),

    INVALID_FORMAT_BAD_REQUEST(400,40001,"Bad request (입력 데이터를 확인하십시오.)"),

    BOARD_ID_BAD_REQUEST(400, 40002, "Bad request (잘못 된 게시물 ID값 입니다.)"),
    CATEGORY_ID_BAD_REQUEST(400, 40003, "Bad request (잘못 된 카테고리 ID값 입니다.)"),
    ATTACHMENTS_ID_BAD_REQUEST(400, 40004, "Bad request (잘못 된 첨부파일 ID값 입니다.)"),
    COMMENT_ID_BAD_REQUEST(400, 40005, "Bad request (잘못 된 댓글/답글 ID값 입니다.))"),
    BOARD_PASSWORD_ID_BAD_REQUEST(400, 40006, "Bad request (잘못 된 게시판 비밀번호 ID값 입니다.)"),

    BOARD_BAD_REQUEST(400, 40007, "Bad request (잘못 된 게시물 값 입니다.)"),
    COMMENT_BAD_REQUEST(400, 40008, "Bad request (잘못 된 댓글/답글 정보입니다.)"),

    KEYWORD_SEARCH_STATUS_BAD_REQUEST(400, 40009, "Bad request (검색 키워드 상태값이 없습니다.)"),
    SELECT_CATEGORY_LIST_BAD_REQUEST(400, 40010, "Bad request (검색 시 필요한 카테고리가 없거나, 3개를 초과했습니다.)"),

    MULTIPARTFILE_STATUS_BAD_REQUEST(400, 40011, "Bad request (파일 업로드 시 필요한 상태 값을 확인해주세요.)"),
    BOARD_CONTENTS_BAD_REQUEST(400,40012, "Bad request (게시판 내용의 최대 Byte 크기를 초과했습니다.)"),
    LOAD_FILE_NAME_BAD_REQUEST(400, 40013, "Bad request (파일을 찾을 수 없습니다.)"),

    FLAG_FORBIDDEN(403, 40014, "Forbidden (변경할 수 없는 상태 값('N') 입니다.)"),

    BOARD_FORBIDDEN(403,40015, "Forbidden (게시판 정보를 수정/삭제 할 권한이 없습니다.)"),
    COMMENT_FORBIDDEN(403,40016, "Forbidden (자식 답글이 있어, 해당 댓글/답글을 삭제할 수 없습니다.)"),
    MULTIPARTFILE_NEW_BAD_REQUEST(403,40017, "Bad request (신규 등록 시에는 기존에 남길 첨부파일이 없으며, 하나 이상의 신규 파일을 업로드해야합니다.)"),
    MULTIPARTFILE_MAXIMUM_BAD_REQUEST(403,40018, "Bad request (파일 업로드 갯수는(기존 것 포함) 3개입니다.)"),

    DUPLICATED(405, 40019, "Duplicated (중복)"),

    INTERNAL_SERVER_ERROR(500,50001,"Internal Server Error (서버에서 처리중 에러가 발생하였습니다.)"),
    JSON_PARSING_ERROR(500, 50002,"Json Data Parsing Error (데이터 파싱중 에러가 발생하였습니다.)"),
    CONNECTION_REFUSED_ERROR(500, 50003,"Connection Refused (서버와 연결을 확인하십시오.)"),
    RESOURCE_ACCESS_ERROR(500, 50004, "Network I/O Error (서버와 연결 방식을 확인하십시오.)"),
    FLAG_INTERNAL_SERVER_ERROR(500, 50005, "Internal server error (요청 정보에 대한 응답 플레그 값이 이상합니다.)");

    private int status;
    private int code;
    private String message;

    public static ErrorCode enumOf(int code) {
        return Arrays.stream(ErrorCode.values())
                .filter(t -> t.getCode() ==code)
                .findAny().orElse(null);
    }

}

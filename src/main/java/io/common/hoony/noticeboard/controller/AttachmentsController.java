package io.common.hoony.noticeboard.controller;

import io.common.hoony.noticeboard.common.type.FlagType;
import io.common.hoony.noticeboard.domain.dto.Attachments.response.ResUploadAttachmentsDTO;
import io.common.hoony.noticeboard.domain.dto.board.response.ResGetBoardListDTO;
import io.common.hoony.noticeboard.domain.entity.Attachments;
import io.common.hoony.noticeboard.domain.entity.Board;
import io.common.hoony.noticeboard.domain.entity.BoardCategory;
import io.common.hoony.noticeboard.error.DKFileMaximumEAException;
import io.common.hoony.noticeboard.error.DkargoException;
import io.common.hoony.noticeboard.error.ErrorCode;
import io.common.hoony.noticeboard.repository.AttachmentsRepository;
import io.common.hoony.noticeboard.repository.BoardRepository;
import io.common.hoony.noticeboard.service.AttachmentsService;
import io.common.hoony.noticeboard.service.CommentService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/attachments")
public class AttachmentsController {

    @Autowired
    private final AttachmentsService attachmentsService;

    private final BoardRepository boardRepository;
    private final AttachmentsRepository attachmentsRepository;

    /**
     * 파일 다운로드 - Controller
     * @param fileName
     * @param request
     * @return
     */
    @ApiOperation(
            value = "파일 다운로드",
            notes = "- 파일이 저장된 URI(fileDownloadURI)를 통해 다운로드 합니다."
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName", value = "파일 명", required = true, dataType = "string", paramType =  "path")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = ResGetBoardListDTO.class)
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {

        //파일을 찾아 리소스 조회 (fileName == 다운로드 URI)
        Resource resource = attachmentsService.loadFileAsResource(fileName);

        //저장된 파일명(URI)에서 원본 파일명으로 변환
        String originalFIleName = attachmentsService.fileNameDecoder(fileName);

        String contentType = null;

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            throw new DkargoException(ErrorCode.FILE_TYPE_NO_CONTENT);
        }

        if(contentType == null) {
            contentType ="application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + originalFIleName + "\"" )
                .body(resource);

    }

    /**
     * 파일 업로드 - Controller (단일)
     * @param file
     * @return
     */
    @ApiOperation(
            value = "파일 업로드(단일)",
            notes = "- 결과값으로 파일명, 파일경로 뿐 아니라 파일타입, 사이즈 까지 보여줍니다."
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "첨부 파일(단일)", required = true, dataType = "__file", paramType = "form"),
            @ApiImplicitParam(name = "boardId", value = "게시글 ID", required = true, dataType = "long", paramType = "query")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public ResUploadAttachmentsDTO uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("boardId") long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new DkargoException(ErrorCode.BOARD_ID_BAD_REQUEST));

        if(board.getAttachmentsFlag() == FlagType.Y) {
        String originalFilename = file.getOriginalFilename();
        String fileName = attachmentsService.storeFile(file);

        String fileDownloadURI = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/attachments/")
                .path(fileName)
                .toUriString();

            Attachments attachments = new Attachments(originalFilename);
            attachments.setBoard(board);
            attachments.setLocationPath(fileDownloadURI);
            attachments.setFileType(file.getContentType());
            attachments.setSize(file.getSize());
            attachmentsRepository.save(attachments);

            return new ResUploadAttachmentsDTO(fileName, fileDownloadURI, file.getContentType(), file.getSize());

        }else {
            throw new DkargoException(ErrorCode.FLAG_FORBIDDEN);
        }

    }

    /**
     * 단일/다중 파일 업로드 및 파일 수정 - Controller
     * @param files
     * @param boardId
     * @param attachmentsIds
     * @return
     */
    @ApiOperation(
            value = "파일 업로드(다중)",
            notes = "- 파일 업로드시 기존에 냅둘파일들은 제외하고 DB에서 삭제 처리 후 다시 새로 생성한다.\t\n" +
                    "- 결과값으로 파일명, 파일경로 뿐 아니라 파일타입, 사이즈 까지 보여줍니다.\t\n" +
                    "- 최대 3개 까지 업로드할 수 있습니다.\t\n" +
                    "- 파일 업로드 및 수정을 함께 사용합니다.\t\n" +
                    "- 배열로 받아 단일/다중 둘다 처리합니다.\t\n" +
                    "- 해당 게시글의 첨부파일여부 값이 'Y' 일때만 테이블에 저장됩니다."
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "files", value = "첨부 파일(단일/다중)", required = false, dataType = "__file", paramType = "form"),
            @ApiImplicitParam(name = "boardId", value = "게시글 ID", required = true, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "attachmentsIds", value = "첨부파일 List", required = false, dataType = "string", paramType =  "query"),
            @ApiImplicitParam(name = "status", value = "등록/수정 상태 값", required = true, dataType = "string", paramType =  "query")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/multiple")
    public List<ResUploadAttachmentsDTO> uploadMultipleFiles(@RequestParam(value = "files", required = false) MultipartFile[] files,
                                                               @RequestParam("boardId") long boardId,
                                                               @RequestParam(value = "attachmentsIds",required = false) List<Long> attachmentsIds,
                                                             @RequestParam("status") String status) {

        int attachmentFileEaConfirm = 3-attachmentsIds.size();
        //추가 파일이 없으면 (무조건 수정)
        if(files == null){
            //기존 업로드 된 파일 모두 삭제
            attachmentsService.deleteAttachments(boardId);
            //기존 업로드 된 파일 중 유지할 것을 다시 복구
            attachmentsService.revertAttachments(boardId, attachmentsIds);

            List<Attachments> attachmentsList = new ArrayList<>();
            for(int i=0; i <= attachmentsIds.size()-1; i++ ){
                attachmentsList.add(attachmentsRepository.findById(attachmentsIds.get(i).longValue()).orElseThrow(() -> new DkargoException(ErrorCode.ATTACHMENTS_ID_BAD_REQUEST)));
            }

            return attachmentsList.stream().map(am -> attachmentsService.resAttachmentList(am)).collect(Collectors.toList());
        }else {//추가 파일이 있으면 (신규/수정)
            if((Arrays.asList(files).size() == 1 && Arrays.asList(files).get(0).isEmpty()) && attachmentsIds.size() == 0){
                throw new DkargoException(ErrorCode.ATTACHMENTS_ID_BAD_REQUEST); ///이거 다시변경!!
            }

            if (Arrays.asList(files).size() > attachmentFileEaConfirm) {
                throw new DkargoException(ErrorCode.MULTIPARTFILE_MAXIMUM_BAD_REQUEST);
            } else {
                if(status.equals("I")){//신규 등록
                    if(attachmentsIds.size() > 0 || (Arrays.asList(files).size() == 1 && Arrays.asList(files).get(0).isEmpty())){
                        throw new DkargoException(ErrorCode.MULTIPARTFILE_NEW_BAD_REQUEST);
                    }else {
                        return Arrays.asList(files)
                                .stream()
                                .map(file -> uploadFile(file, boardId))
                                .collect(Collectors.toList());
                    }
                }else if(status.equals("U")){//수정 등록

                    //기존 업로드된 파일들을 모두 삭제
                    attachmentsService.deleteAttachments(boardId);

                    if(attachmentsIds.size() > 0) {//기존 첨부파일 유지

                        //변경 안 된 것만 다시 복구
                        attachmentsService.revertAttachments(boardId, attachmentsIds);

                        if((Arrays.asList(files).size() == 1 && Arrays.asList(files).get(0).isEmpty())){ //신규 업로드 X
                            List<Attachments> attachmentsList = new ArrayList<>();
                            for(int i=0; i <= attachmentsIds.size()-1; i++ ){
                                attachmentsList.add(attachmentsRepository.findById(attachmentsIds.get(i).longValue()).orElseThrow(() -> new DkargoException(ErrorCode.ATTACHMENTS_ID_BAD_REQUEST)));
                            }

                            return attachmentsList.stream().map(am -> attachmentsService.resAttachmentList(am)).collect(Collectors.toList());

                        }else{//신규 업로드 O
                            return Arrays.asList(files)
                                    .stream()
                                    .map(file -> uploadFile(file, boardId))
                                    .collect(Collectors.toList());
                        }
                    }else {//기존 첨부파일 유지 X
                        return Arrays.asList(files)
                                .stream()
                                .map(file -> uploadFile(file, boardId))
                                .collect(Collectors.toList());
                    }
                }else{
                    throw new DkargoException(ErrorCode.MULTIPARTFILE_STATUS_BAD_REQUEST);
                }
            }
        }

    }

    /**
     * 첨부파일 전체 삭제 - Service
     * @param boardId
     */
    @ApiOperation(
            value = "등록된 첨부파일 삭제",
            notes = "- 실제로 DB상에서 삭제 플래그와, 삭제 날짜를 변경한다."
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "boardId", value = "게시글 ID", required = true, dataType = "long", paramType =  "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = ResGetBoardListDTO.class)
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping
    public void deleteMultipleFiles(@RequestParam("boardId") long boardId) {
        //기존 업로드된 파일들을 삭제
        attachmentsService.deleteAttachments(boardId);
    }

}

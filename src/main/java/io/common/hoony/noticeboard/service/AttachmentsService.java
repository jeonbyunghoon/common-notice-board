package io.common.hoony.noticeboard.service;

import io.common.hoony.noticeboard.common.type.FlagType;
import io.common.hoony.noticeboard.config.FileUploadConfig;
import io.common.hoony.noticeboard.domain.dto.Attachments.response.ResUploadAttachmentsDTO;
import io.common.hoony.noticeboard.domain.entity.Attachments;
import io.common.hoony.noticeboard.error.DKFileNotFoundException;
import io.common.hoony.noticeboard.error.DkargoException;
import io.common.hoony.noticeboard.error.ErrorCode;
import io.common.hoony.noticeboard.error.FileStorageException;
import io.common.hoony.noticeboard.repository.AttachmentsRepository;
import io.common.hoony.noticeboard.repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
//@RequiredArgsConstructor
@Service
public class AttachmentsService {

    @Autowired
    AttachmentsRepository attachmentsRepository;

    //실제로 upload하고 resouce를 반환해 다운로드 할 수 있게 해주는 부분
    private final Path fileStorageLocation;

    /**
     * 파일을 찾아 리소스 조회 - Service
     * @param fileName
     * @return
     */
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new DkargoException(ErrorCode.LOAD_FILE_NAME_BAD_REQUEST);
            }

        }catch (MalformedURLException ex) {
            throw new DkargoException(ErrorCode.LOAD_FILE_NAME_BAD_REQUEST);
        }
    }

    /**
     * 저장된 파일명(URI)에서 원본 파일명으로 변환 - Service
     * @param fileName
     * @return
     */
    public String fileNameDecoder(String fileName) {

        String originalFileFullName = null;

        try {
            originalFileFullName = URLDecoder.decode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e)  {
            e.printStackTrace();
        }

        int deletePointIndexOf = originalFileFullName.indexOf("]");
        String originalFIleNameValue = originalFileFullName.substring(deletePointIndexOf + 1);

        String originalFIleName = null;
        try {
            originalFIleName = new String(originalFIleNameValue.getBytes(StandardCharsets.UTF_8),"ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return originalFIleName;

    }

    @Autowired
    public AttachmentsService(FileUploadConfig fileUploadConfig){
        this.fileStorageLocation = Paths.get(fileUploadConfig.getLocation())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("업로드 된 파일이 저장 될 디렉토리를 만들 수 없습니다.", ex);
        }
    }

    // 의존성 주입이 이루어진 후 초기화를 수행
    // @PostConstruct가 붙은 메서드는 클래스가 service를 수행하기 전에 발생한다.
    // 이 메서드는 다른 리소스에서 호출되지 않는다해도 수행된다.
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 파일 업로드 - Service (Files.copy 메서드를 이용하여 설정한 경로로 파일 업로드)
     * @param file
     * @return
     */
    public String storeFile(MultipartFile file) {

        String originalFilename = file.getOriginalFilename();

        //중복 방지(date/uuid)
        String date = LocalDateTime.now().toString();
        //String uuid = UUID.randomUUID().toString();

        //String fileName = "[" + date + "][" + uuid + "]" + originalFilename;
        String fileName = "[" + date + "]" + originalFilename;

        try {

            if(fileName.contains("..")) {
                throw new FileStorageException("파일 이름에 잘 못된 경로 시퀀스가 있습니다. : " + fileName);
            }
            //대상 위치에 파일 복사 (동일 한 이름의 기존 파일 대체)
            Path targetlocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetlocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        }catch (IOException ex) {
            throw new FileStorageException("파일을 저장할 수 없습니다. (" + fileName + ")", ex);
        }
    }

    /**
     * 기존 업로드 된 파일 모두 삭제 / 삭제된 게시물의 전체 첨부파일 상태 값 수정하여 삭제 - Service
     * @param boardId
     */
    @Transactional
    public void deleteAttachments(long boardId){

        FlagType deleteFlag = FlagType.Y;
        LocalDateTime deleteAt = LocalDateTime.now();
        FlagType deleteFlagConfirmValue = FlagType.N;

        attachmentsRepository.deleteAttachments(boardId, deleteFlag, deleteAt, deleteFlagConfirmValue);

    }

    /**
     * 기존 업로드 된 파일 중 유지할 것을 다시 복구 - Service
     * @param boardId
     */
    @Transactional
    public void revertAttachments(long boardId, List<Long> attachmentsIds){

        long attachmentId;

        FlagType deleteFlag = FlagType.N;
        FlagType deleteFlagConfirmValue = FlagType.Y;

        for(int i=0; i <= attachmentsIds.size()-1; i++ ){
            attachmentId =attachmentsIds.get(i).longValue();
            attachmentsRepository.revertAttachments(boardId, deleteFlag, deleteFlagConfirmValue, attachmentId);
        }

    }

    public ResUploadAttachmentsDTO resAttachmentList(Attachments attachments){

        return new ResUploadAttachmentsDTO(attachments.getAttachmentsName(), attachments.getLocationPath(), attachments.getFileType(), attachments.getSize());
    }
}

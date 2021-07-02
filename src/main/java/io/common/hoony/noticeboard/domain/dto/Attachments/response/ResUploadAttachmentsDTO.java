package io.common.hoony.noticeboard.domain.dto.Attachments.response;

import io.common.hoony.noticeboard.domain.entity.Attachments;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResUploadAttachmentsDTO {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
    private String unit;

    public ResUploadAttachmentsDTO(String fileName, String fileDownloadUri, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
        this.unit = "Byte";
    }

}

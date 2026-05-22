package com.ferbo.gestion.api.response;

public class FileResponse 
{
    private String fileName;
    private String base64Content;
    private String contentType;

    public FileResponse(String fileName, String base64Content) {
        this.fileName = fileName;
        this.base64Content = base64Content;
        this.contentType = "application/pdf";
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBase64Content() {
        return base64Content;
    }

    public void setBase64Content(String base64Content) {
        this.base64Content = base64Content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
}

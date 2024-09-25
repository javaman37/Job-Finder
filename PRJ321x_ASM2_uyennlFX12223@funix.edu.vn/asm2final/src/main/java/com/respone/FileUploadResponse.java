package com.respone;

public class FileUploadResponse {
	private String fileName;
    private long size;
    private String downloadUri;

    public FileUploadResponse() {}

    public FileUploadResponse(String fileName, long size, String downloadUri) {
        this.fileName = fileName;
        this.size = size;
        this.downloadUri = downloadUri;
    }



	public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDownloadUri() {
        return downloadUri;
    }

    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }

}

package com.pivotalsoft.kammasevasamithi.Items;

/**
 * Created by Gangadhar on 10/30/2017.
 */

public class GalleryItem {
    private String galleryid;
    private String title;
    private String coverimage;


    public GalleryItem(String galleryid, String title, String coverimage) {
        this.galleryid = galleryid;
        this.title = title;
        this.coverimage = coverimage;
    }

    public String getGalleryid() {
        return galleryid;
    }

    public void setGalleryid(String galleryid) {
        this.galleryid = galleryid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverimage() {
        return coverimage;
    }

    public void setCoverimage(String coverimage) {
        this.coverimage = coverimage;
    }
}

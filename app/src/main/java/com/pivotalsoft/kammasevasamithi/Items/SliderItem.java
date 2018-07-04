package com.pivotalsoft.kammasevasamithi.Items;

/**
 * Created by Gangadhar on 10/30/2017.
 */

public class SliderItem {
    String picid ;
    String galleryid ;
    String albumimage ;

    public SliderItem(String picid, String galleryid, String albumimage) {
        this.picid = picid;
        this.galleryid = galleryid;
        this.albumimage = albumimage;
    }

    public String getPicid() {
        return picid;
    }

    public void setPicid(String picid) {
        this.picid = picid;
    }

    public String getGalleryid() {
        return galleryid;
    }

    public void setGalleryid(String galleryid) {
        this.galleryid = galleryid;
    }

    public String getAlbumimage() {
        return albumimage;
    }

    public void setAlbumimage(String albumimage) {
        this.albumimage = albumimage;
    }
}

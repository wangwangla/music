package com.example.learnandroid.bean;

public class MusicBean {
    private final long albumId;
    private final String albumName;
    private final long artistId;
    private final String artistName;
    private final int duration;
    private final long id;
    private final String title;
    private final int trackNumber;
    private final String path;

    public MusicBean() {
        this.id = -1;
        this.albumId = -1;
        this.artistId = -1;
        this.title = "";
        this.artistName = "";
        this.albumName = "";
        this.duration = -1;
        this.trackNumber = -1;
        this.path = "";
    }

    public MusicBean(long _id, long _albumId, long _artistId, String _title, String _artistName, String _albumName, int _duration, int _trackNumber,String path) {
        this.id = _id;
        this.albumId = _albumId;
        this.artistId = _artistId;
        this.title = _title;
        this.artistName = _artistName;
        this.albumName = _albumName;
        this.duration = _duration;
        this.trackNumber = _trackNumber;
        this.path = path;
    }

    @Override
    public String toString() {
        return "MusicBean{" +
                "albumId=" + albumId +
                ", albumName='" + albumName + '\'' +
                ", artistId=" + artistId +
                ", artistName='" + artistName + '\'' +
                ", duration=" + duration +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", trackNumber=" + trackNumber +
                '}';
    }

    public long getAlbumId() {
        return albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public long getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getDuration() {
        return duration;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public String getPath() {
        return path;
    }
}
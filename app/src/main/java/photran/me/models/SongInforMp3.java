package photran.me.models;

import org.json.JSONException;
import org.json.JSONObject;

public class SongInforMp3 {
    public static String KEY = "SongInforMp3";
    public static String SOURCE = "source";
    public static String LINK_DOWNLOAD = "link_download";
    public static String LINK_128 = "128";
    public static String LYRICS_FILE = "lyrics_file";

    public static String THUMBNAIL = "thumbnail";
    public static String SONG_ID_ENCODE = "song_id_encode";
    public static String TITLE = "title";
    public static String ARTIST = "artist";


    private String source_128;
    private String lyrics_file;
    private String thumbnail;
    private String song_id_encode;
    private String title;
    private String artist;

    public String getSource_128() {
        return source_128;
    }

    public void setSource_128(String source_128) {
        this.source_128 = source_128;
    }

    public String getLyrics_file() {
        return lyrics_file;
    }

    public void setLyrics_file(String lyrics_file) {
        this.lyrics_file = lyrics_file;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSong_id_encode() {
        return song_id_encode;
    }

    public void setSong_id_encode(String song_id_encode) {
        this.song_id_encode = song_id_encode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public static SongInforMp3 getSongFromJson(String data) {
        SongInforMp3 songInforMp3 = new SongInforMp3();
        try {
            JSONObject objRoot = new JSONObject(data);
            songInforMp3.setSource_128(objRoot
                    .getString("source_128"));

            songInforMp3.setSong_id_encode(objRoot
                    .getString(SongInforMp3.SONG_ID_ENCODE));
            songInforMp3.setArtist(objRoot
                    .getString(SongInforMp3.ARTIST));
            songInforMp3.setThumbnail(objRoot
                    .getString(SongInforMp3.THUMBNAIL));
            songInforMp3.setLyrics_file(objRoot
                    .getString(SongInforMp3.LYRICS_FILE));
            songInforMp3.setTitle(objRoot
                    .getString(SongInforMp3.TITLE));

        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }

        return  songInforMp3;
    }

}

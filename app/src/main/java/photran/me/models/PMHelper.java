package photran.me.models;

import android.content.Context;

import com.parse.ParseObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PMHelper {
	public static final int STUDY_ENGLISH = 1;

	
	private static String YOUTUBE_LINK = "http://img.youtube.com/vi/{***}/0.jpg";
    private static String TITLE = "title";
    private static String TITLES = "titles";
    private static String LINKS = "links";

	public static String getYoutubeIdFormUrl(String url) {
		String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

		Pattern compiledPattern = Pattern.compile(pattern);
		Matcher matcher = compiledPattern.matcher(url);

		if (matcher.find()) {
			return matcher.group();
		}
		return "";
	}

	// http://stackoverflow.com/questions/2068344/how-do-i-get-a-youtube-video-thumbnail-from-the-youtube-api
	public static String getImagePreviewFromYoutupeId(String youtubeId) {
		String link = YOUTUBE_LINK.replace("{***}", youtubeId);
		return link;
	}



	public static VideoYoutube getVideoYoutubeFormParseObject(ParseObject obj) {
		String links = obj.getString(LINKS);
		String titles = obj.getString(TITLE);

		if (titles == null) {
			titles = obj.getString(TITLES);
		}
		return new VideoYoutube(links, titles);
	}
    public static SongInforMp3 getSongInforMp3FormParseObject(ParseObject obj) {
        SongInforMp3 songInforMp3 = new SongInforMp3();

        songInforMp3.setTitle(obj.getString(SongInforMp3.TITLE));
        songInforMp3.setThumbnail(obj.getString(SongInforMp3.THUMBNAIL));
        songInforMp3.setLyrics_file(obj.getString(SongInforMp3.LYRICS_FILE));
        songInforMp3.setArtist(obj.getString(SongInforMp3.ARTIST));
        songInforMp3.setSong_id_encode(obj.getString(SongInforMp3.SONG_ID_ENCODE));
        songInforMp3.setSource_128(obj.getString("source128"));

        return  songInforMp3;
    }

    public static String getFromAssets(Context context,String fileName){
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getAssets().open(fileName) );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null){
                if(line.trim().equals(""))
                    continue;
                Result += line + "\r\n";
            }
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

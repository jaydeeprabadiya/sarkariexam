package com.sarkarinaukri.util;


import com.sarkarinaukri.model.BaseItem;
import com.sarkarinaukri.model.VideoItem;

import java.util.ArrayList;
import java.util.List;


//import s.com.testvide.model.PicItem;
//import s.com.testvide.model.TextItem;

/**
 * @author Wayne
 */
public class ItemUtils {

    private static final String VIDEO_URL1 = "https://video.sarkariexam.com/mp4s/1587371235_image.mp4";
    private static final String VIDEO_URL2 = "https://video.sarkariexam.com/mp4s/1587371205_image.mp4";
    private static final String VIDEO_URL3 = "https://video.sarkariexam.com/mp4s/1587371171_image.mp4";
    private static final String VIDEO_URL4 = "https://video.sarkariexam.com/mp4s/1587371126_image.mp4";

    private static final String PIC_URL1 = "https://play.sarkariexam.com/admin/videoThumb/1586502740.png";
    private static final String PIC_URL2 = "https://play.sarkariexam.com/admin/videoThumb/1586502652.png";
    private static final String PIC_URL3 = "https://play.sarkariexam.com/admin/videoThumb/1586502466.png";
    private static final String PIC_URL4 = "https://play.sarkariexam.com/admin/videoThumb/1586502404.png";

    public static List<BaseItem> generateMockData() {
        List<BaseItem> list = new ArrayList<>();

//        list.add(new TextItem("TextItem"));
//        list.add(new PicItem(PIC_URL1));
        list.add(new VideoItem(VIDEO_URL4, PIC_URL4));

//        list.add(new TextItem("TextItem"));
//        list.add(new PicItem(PIC_URL2));
        list.add(new VideoItem(VIDEO_URL3, PIC_URL3));

//        list.add(new TextItem("TextItem"));
//        list.add(new PicItem(PIC_URL3));
        list.add(new VideoItem(VIDEO_URL2, PIC_URL2));

//        list.add(new TextItem("TextItem"));
//        list.add(new PicItem(PIC_URL4));
        list.add(new VideoItem(VIDEO_URL1, PIC_URL1));

//        list.add(new TextItem("TextItem"));
//        list.add(new PicItem(PIC_URL1));
        list.add(new VideoItem(VIDEO_URL4, PIC_URL4));

//        list.add(new TextItem("TextItem"));
//        list.add(new PicItem(PIC_URL2));
        list.add(new VideoItem(VIDEO_URL3, PIC_URL3));

//        list.add(new TextItem("TextItem"));
//        list.add(new PicItem(PIC_URL3));
        list.add(new VideoItem(VIDEO_URL2, PIC_URL2));

//        list.add(new TextItem("TextItem"));
//        list.add(new PicItem(PIC_URL4));
        list.add(new VideoItem(VIDEO_URL1, PIC_URL1));

        return list;
    }

}

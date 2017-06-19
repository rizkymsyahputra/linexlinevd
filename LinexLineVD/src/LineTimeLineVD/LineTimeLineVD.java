/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LineTimeLineVD;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author KUCENG AER
 * https://timeline.line.me/post/_dUJSdRR9dyijV0JrB1ed7-_2WTKDtVvSLTij9lw/1149643681702072728
 */
public class LineTimeLineVD {
    private String videofilelink;
    private String thumbnailfilelink;
    private long filesize;
    private long thumbfilesize;
    public static final String THUMBCACHEPATH = "file:image\thumb.jpg";

    public LineTimeLineVD() {
    }
    
    public LineTimeLineVD(String link){
        init(link);
    }
    
    public void init(String link){
        try {
            String medialink = "";
            Document doc = Jsoup.connect(link).get();
            Elements metaTags  = doc.getElementsByTag("meta");
            
            for(Element metaTag : metaTags){
                if (metaTag.attr("property").compareTo("og:video")==0){
                    medialink = metaTag.attr("content");
                }
            }
            
            this.videofilelink = medialink.concat("/mp4");
            this.thumbnailfilelink = medialink.concat("/L800x800");
            
            
        } catch(IOException e){
            System.out.println(e);
        }
        getthumbnail();
    }
    
    public long getfilesize(){
        try {
            URL videoURL = new URL(videofilelink);
            URLConnection conn = videoURL.openConnection();
            filesize = conn.getContentLengthLong();
        } catch (IOException e) {
            System.out.println(e);
        }
        return filesize;
    }
    
    public String filesizeString(){
        String filesizestr = "";
        getfilesize();
        if(filesize >= 1e6){
            filesizestr = (long) (filesize / 1e6) + " MB";
        }else if(filesize < 1e6 && filesize > 1e3){
            filesizestr = (long) (filesize / 1e3) + " KB";
        }else{
            filesizestr = filesize +" Bytes";
        }
        return filesizestr;
    }
    
    public long getthumbfilesize(){
        try {
            URL videoURL = new URL(thumbnailfilelink);
            URLConnection conn = videoURL.openConnection();
            thumbfilesize = conn.getContentLengthLong();
        } catch (IOException e) {
            System.out.println(e);
        }
        return thumbfilesize;
    }
    
    public void getthumbnail(){
        try{
            URL downloadurl = new URL(thumbnailfilelink);
            
            ReadableByteChannel rbc = Channels.newChannel(downloadurl.openStream());
            FileOutputStream fos = new FileOutputStream(THUMBCACHEPATH);
            fos.getChannel().transferFrom(rbc,0,this.getthumbfilesize());
        } catch (IOException e){
            System.out.println(e);
        }
    }
    
    public void downloadvideo(String filepath){
        try{
            URL downloadurl = new URL(videofilelink);
            ReadableByteChannel rbc = Channels.newChannel(downloadurl.openStream());
            FileOutputStream fos = new FileOutputStream(filepath);
            fos.getChannel().transferFrom(rbc,0,filesize);
        } catch (IOException e){
            System.out.println(e);
        }
    }
    
    public static String getClipboards() throws IOException{
        String datax = "";
        try {
            datax = (String) Toolkit.getDefaultToolkit() 
                    .getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException ex) {
            System.out.println(ex);
        }
        return datax;
    }
    
    public static void main(String[] args) {
        LineTimeLineVD downinstance = new LineTimeLineVD("https://timeline.line.me/post/_dUlag09EY0F7sfbln6yadnQnxv7BroLlpIQrkzU/1149682611010011011");
        System.out.println("downloading "+downinstance.getthumbfilesize()+" byte video");
        downinstance.getthumbnail();
        System.out.println("SUCCESSFULLY DOWNLOADED");
    }
}

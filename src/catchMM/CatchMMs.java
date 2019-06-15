package catchMM;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CatchMMs {

    public static void mkdir(String text, String imgUrl) throws IOException {

        //解析地区 ”海南“
        String dirName = text.substring(0,2);
        System.out.println(dirName);

        //给每个地区创建一个文件夹
        File file = new File("/home/chr/code/MM/",dirName);
        System.out.println(file.mkdir());

        //创建”地区“文件夹路径
        String dirPath = "/home/chr/code/MM/"+dirName;

        //存储图片的路径
        String imgPath = "/home/chr/code/MM/"+dirName+"/"+text+".jpg";

        saveToFile(imgUrl,imgPath,dirPath);
    }


    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("https://www.duodia.com/daxuexiaohua").get();

            Elements els = doc.select("#main > div.row > article > div > a.thumbnail-container > img");

            for (Element el : els) {
                String  imageurl = el.attr("src");
                String  text = el.attr("alt");
                mkdir(text, imageurl);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 将图片保存相对应的位置(数据流)
     * @param imgUrl
     * @param imgPath
     * @param dirPath
     * @throws IOException
     */
    public static void saveToFile(String imgUrl, String imgPath, String dirPath) throws IOException {
        URL url = new URL(imgUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.connect();

        BufferedInputStream bf = new BufferedInputStream(httpURLConnection.getInputStream());
        File file= new File(dirPath);


        int size = 0;
        FileOutputStream fos = new FileOutputStream(imgPath);

        byte[] buff = new byte[1024];

        if(file.exists()) {
            while ((size = bf.read(buff)) != -1) {
                fos.write(buff, 0, size);
            }
            fos.flush();
        }
        fos.close();
        bf.close();
        httpURLConnection.disconnect();
    }
}
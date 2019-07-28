package gomoku;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

//关于音乐放不出声音：光一条play()代码很可能放不出声音，因为电脑太快了，还没放出声音程序就结束了，用一个循环加Thread.sleep(1000)就能看出效果了
public class Music {
    private String name;//可以用来选择你想要播放的音乐
    private File file; //放音乐文件的路径，注意一定要是WAV格式的音乐不然不可以播放
    private URL url;
    private URI uri;
    private AudioClip clip;

    public Music(String name){
        this.name = name;
        try
        {
            file = new File(name);
            uri=file.toURI();
            url = uri.toURL();
            clip = Applet.newAudioClip(url);
            //这三个地方是关键，没有这个不会有声音的
            //clip.loop();//循环播放
            //clip.play();//播放
            //clip.stop();//停止播放
            System.out.println("音乐文件已经打开");
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("播放错误！");
        }

    }

    public void stopMusic()//停止播放
    {
        clip.stop();
    }

    public void playMusic()//播放
    {
         clip.play();
    }

    public void loopMusic()//循环播放
    {
        clip.loop();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        try
        {
            file = new File(name);
            uri=file.toURI();
            url = uri.toURL();
            clip = Applet.newAudioClip(url);
            System.out.println("音乐文件已经打开");
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("播放错误！");
        }
    }
}

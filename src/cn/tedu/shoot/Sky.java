package cn.tedu.shoot;
import java.awt.image.BufferedImage;
/** 天空:是飞行物 */
public class Sky extends FlyingObject {
    private int speed; //移动速度
    private int y1; //第2张图片的y坐标
    /** 构造方法 */
    public Sky(){
        super(World.WIDTH,World.HEIGHT,0,0);
        speed = 1;
        y1 = -World.HEIGHT;
    }

    /** 重写step()移动 */
    public void step(){
        y+=speed;
        if(y>=World.HEIGHT){
            y=-World.HEIGHT;
        }
        y1+=speed;
        if(y1>=World.HEIGHT){
            y1=-World.HEIGHT;
        }
    }

    /** 重写getImage()获取图片 */
    public BufferedImage getImage(){ //每10毫秒走一次
        return Images.sky; //返回sky图片即可
    }

    /** 获取y1坐标 */
    public int getY1(){
        return y1; //返回y1坐标
    }
}




















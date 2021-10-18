package cn.tedu.shoot;
import java.awt.image.BufferedImage;
import java.util.Random;
/** 大敌机:是飞行物 */
public class BigAirplane extends FlyingObject implements EnemeyScore{
    private int speed; //移动速度
    /** 构造方法 */
    public BigAirplane(){
        super(66,89);
        speed = 2;
    }

    /** 重写step()移动 */
    public void step(){
       y+=speed;
    }

    int index = 1; //下标
    /** 重写getImage()获取图片 */
    public BufferedImage getImage(){ //每10毫秒走一次
        if(isLive()){
            return Images.bairs[0];
        }else if(isDead()){
            BufferedImage img = Images.bairs[index++];
            if(index==Images.bairs.length){
                state = REMOVE;
            }
            return img;
        }
        return null;
    }

    public int getScore(){
        return 3;
    }
}




















package cn.tedu.shoot;
import java.awt.image.BufferedImage;
import java.util.Random;
/** 小蜜蜂:是飞行物 */
public class Bee extends FlyingObject implements EnemyAward{
    private int xSpeed; //x坐标移动速度
    private int ySpeed; //y坐标移动速度
    private int awardType; //奖励类型
    /** 构造方法 */
    public Bee(){
        super(60,51);
        xSpeed = 1;
        ySpeed = 2;
        Random rand = new Random(); //随机数对象
        awardType = rand.nextInt(2); //0到1之间随机生成
    }

    /** 重写step()移动 */
    public void step(){
        y+=ySpeed;
        x+=xSpeed;
        if(x<=0 ||x>=World.WIDTH-width){
            xSpeed*=-1;
        }
    }

    int index = 1; //下标
    /** 重写getImage()获取图片 */
    public BufferedImage getImage(){ //每10毫秒走一次
        if(isLive()){
            return Images.bees[0];
        }else if(isDead()){
            BufferedImage img = Images.bees[index++];
            if(index==Images.bees.length){
                state = REMOVE;
            }
            return img;
        }
        return null;
    }

    /** 重写getAwardType奖励类型*/
    public int getAwardType(){
        return awardType;
    }
}




















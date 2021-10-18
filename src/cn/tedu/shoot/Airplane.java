package cn.tedu.shoot;
import java.awt.image.BufferedImage;
import java.util.Random;
/** 小敌机:是飞行物 */
public class Airplane extends FlyingObject implements EnemeyScore{
    private int speed; //移动速度
    /** 构造方法 */
    public Airplane(){ //Airplane a = new Airplane();
        super(48,50);
        speed = 2;
    }

    /** 重写step()移动 */
    public void step(){
        y+=speed;
    }

    int index = 1; //下标
    /** 重写getImage()获取图片 */
    public BufferedImage getImage(){ //每10毫秒走一次
        if(isLive()){              //若活着的
            return Images.airs[0]; //则直接返回airs[0]图片即可
        }else if(isDead()){ //若死了的
            BufferedImage img = Images.airs[index++]; //获取爆破图
            if(index==Images.airs.length){ //若index为5，则表示到最后一张爆破图了
                state = REMOVE;            //将当前状态修改为REMOVE删除的
            }
            return img; //返回爆破图
            /*
              执行过程:
                                  index=1
                10M  img=airs[1]  index=2          返回airs[1]
                20M  img=airs[2]  index=3          返回airs[2]
                30M  img=airs[3]  index=4          返回airs[3]
                40M  img=airs[4]  index=5(REMOVE)  返回airs[4]
                50M  不返回图片
             */
        }
        return null; //REMOVE状态时，不返回图片
    }
    public int getScore(){
        return 1;
    }
}




















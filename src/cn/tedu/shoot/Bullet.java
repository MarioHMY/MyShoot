package cn.tedu.shoot;

import java.awt.image.BufferedImage;

/** 子弹:是飞行物 */
public class Bullet extends FlyingObject {
    private int speed; //移动速度
    /** 构造方法 */    //Bullet b = new Bullet(100,200);
    public Bullet(int x,int y){ //子弹可以有多个，子弹的初始坐标要依赖于当前英雄机的坐标位置
        super(8,20,x,y);
        speed = 3;
    }

    /** 重写step()移动 */
    public void step(){
        y-=speed;
    }

    /** 重写getImage()获取图片 */
    public BufferedImage getImage(){ //每10毫秒走一次
        if(isLive()){             //若活着的
            return Images.bullet; //则直接返回子弹图片
        }else if(isDead()) { //若死了的
            state = REMOVE;  //则将当前状态修改为REMOVE删除的
        }
        return null; //死了的和删除的，都不返回图片
        /*
          执行过程:
            1)若活着的，返回bullet图片
            2)若死了的，将当前状态修改为REMOVE删除的，同时不返回图片
            3)若删除的，不返回图片
         */
    }
    /** 检测是否越界*/
    public boolean isOutOfBounds(){
        return y<=-height;//小于负的子弹高就算越界
    }
}




















package cn.tedu.shoot;

import java.awt.image.BufferedImage;

/** 英雄机:是飞行物 */
public class Hero extends FlyingObject {
    private int life;   //命
    private int fire;   //火力值
    /** 构造方法 */
    public Hero(){
        super(97,139,140,400);
        life = 3;
        fire = 0;
    }

    /** 重写step()移动 */
    public void step(){
        System.out.println("英雄机切换图片啦!");
    }

    int index = 0; //下标
    /** 重写getImage()获取图片 */
    public BufferedImage getImage(){ //每10毫秒走一次
        return Images.heros[index++%Images.heros.length]; //heros[0]和heros[1]来回切换
        /* 执行过程:
                              index=0
           10M  返回heros[0]  index=1
           20M  返回heros[1]  index=2
           30M  返回heros[0]  index=3
           40M  返回heros[1]  index=4
           50M  返回heros[0]  index=5
         */
    }

    /** 英雄机发射子弹(生成子弹对象) */
    public Bullet[] shoot(){
        int xStep = this.width/4; //1/4英雄机的宽
        int yStep = 20; //固定的20
        if(fire>0){ //双倍
            Bullet[] bs = new Bullet[2]; //2发子弹
            bs[0] = new Bullet(this.x+1*xStep,this.y-yStep); //x:英雄机的x+1/4英雄机的宽  y:英雄机的y-固定的20
            bs[1] = new Bullet(this.x+3*xStep,this.y-yStep); //x:英雄机的x+3/4英雄机的宽  y:英雄机的y-固定的20
            fire-=2; //发射一次双倍火力，则火力值减2
            return bs;
        }else{ //单倍
            Bullet[] bs = new Bullet[1]; //1发子弹
            bs[0] = new Bullet(this.x+2*xStep,this.y-yStep); //x:英雄机的x+2/4英雄机的宽  y:英雄机的y-固定的20
            return bs;
        }
    }

    /**英雄机移动*/
    public void moveTo(int x ,int y){//x=鼠标的x,y = 鼠标的y
        this.x = x-this.width/2;
        this.y = y-this.height/2;
    }

    /** 英雄机增命*/
    public void addLive(){
        life++;//
    }

    /** 获取英雄机的命*/
    public int getLife(){
        return life;
    }

    /** 英雄机减命*/
    public void subtractLife(){
        life--;
    }

    /** 英雄机增火力值*/
    public void addFire(){
        fire+=40;
    }

    /** 英雄机清除火力*/
    public void clearFire(){
        fire =0;
    }
}





















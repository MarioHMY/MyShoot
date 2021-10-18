package cn.tedu.shoot;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.util.Arrays;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** 整个游戏窗口 */
public class World extends JPanel {
    public static final int WIDTH = 400;  //窗口的宽
    public static final int HEIGHT = 700; //窗口的高

    public static final int START = 0;//启动状态
    public static final int RUNNING = 1;//运行状态
    public static final int PAUSE = 2;//暂停状态
    public static final int GAME_OVER = 3;//游戏结束状态
    private int state = START;//当前状态（默认启动状态）

    //如下对象为窗口中所显示的对象
    private Sky sky = new Sky();         //天空对象
    private Hero hero = new Hero();      //英雄机对象
    private FlyingObject[] enemies = {}; //敌人(小敌机、大敌机、小蜜蜂)数组
    private Bullet[] bullets = {};       //子弹数组

    /** 生成敌人(小敌机、大敌机、小蜜蜂)对象 */
    public FlyingObject nextOne(){
        Random rand = new Random(); //随机数对象
        int type = rand.nextInt(20); //0到19之间的随机数
        if(type<5){ //0到4时，返回小蜜蜂对象
            return new Bee();
        }else if(type<13){ //5到12时，返回小敌机对象
            return new Airplane();
        }else{ //13到19时，返回大敌机对象
            return new BigAirplane();
        }
    }

    private int enterIndex = 0; //敌人入场计数
    /** 敌人入场 */
    public void enterAction(){ //每10毫秒走一次
        enterIndex++; //每10毫秒增1
        if(enterIndex%40==0){ //每400(40*10)毫秒走一次
            FlyingObject obj = nextOne(); //获取敌人对象
            enemies = Arrays.copyOf(enemies,enemies.length+1); //扩容
            enemies[enemies.length-1] = obj; //将obj添加到enemies的最后一个元素上
        }
    }

    private int shootIndex = 0; //子弹入场计数
    /** 子弹入场 */
    public void shootAction(){ //每10毫秒走一次
        shootIndex++; //每10毫秒增1
        if(shootIndex%30==0){ //每300(30*10)毫秒走一次
            Bullet[] bs = hero.shoot(); //获取子弹数组对象
            bullets = Arrays.copyOf(bullets,bullets.length+bs.length); //扩容(bs有几个就扩大几个容量)
            System.arraycopy(bs,0,bullets,bullets.length-bs.length,bs.length); //数组的追加
        }
    }

    /** 飞行物的移动*/
    public void stepAction(){
        sky.step();
        for(int i = 0;i <enemies.length;i++){
            enemies[i].step();
        }for (int i = 0;i<bullets.length;i++){
            bullets[i].step();
        }
    }

    /** 删除越界的敌人*/
    public void outOfBoundsAction(){
        for (int i = 0;i<enemies.length;i++){//删除越界敌人
            if(enemies[i].isOutOfBounds() || enemies[i].isRemove()){//出界了或者是isRemove都删除
                enemies[i] = enemies[enemies.length-1];//将越界的敌人赋值为最后一个
                enemies = Arrays.copyOf(enemies,enemies.length-1);//缩容
            }
        }
        for(int i = 0;i<bullets.length;i++){
            if(bullets[i].isOutOfBounds() || bullets[i].isRemove()){//出界了或者是isRemove都删除
                bullets[i] = bullets[bullets.length-1];
                bullets = Arrays.copyOf(bullets,bullets.length-1);
            }
        }
    }

    private int score = 0;//玩家的得分


    /** 子弹与敌人碰撞*/
    public void BulletBangAction(){
        for(int i = 0;i<bullets.length;i++){
            Bullet b = bullets[i];
            for(int j = 0;j<enemies.length;j++){
                FlyingObject f = enemies[j];
                if(b.isLive() && f.isLive() && f.isHit(b)){
                    b.goDead();
                    f.goDead();
                    if(f instanceof EnemeyScore){
                        EnemeyScore es = (EnemeyScore)f;
                        score+=es.getScore();
                    }
                    if(f instanceof EnemyAward){
                        EnemyAward ea = (EnemyAward)f;
                        int type = ea.getAwardType();
                        switch (type){
                            case EnemyAward.FIRE:
                                hero.addFire();
                                break;
                            case EnemyAward.LIFE:
                                hero.addLive();
                                break;
                        }
                    }
                }
            }
        }
    }

    /** 英雄机与敌人碰撞*/
    public void HeroBangAction(){
        for(int i = 0;i<enemies.length;i++){
            FlyingObject f = enemies[i];
            if(f.isLive() && hero.isLive() && f.isHit(hero)){
                f.goDead();
                hero.subtractLife();
                hero.clearFire();
            }
        }
    }

    /**检测游戏结束*/
    public void checkGameOverAction(){
        if(hero.getLife()<=0) {
            state = GAME_OVER;
        }
    }

    /** 启动程序的执行 */
    public void action(){
        //鼠标侦听器
        MouseAdapter m = new MouseAdapter() {
            /** 重写mouse Moved（）鼠标移动事件*/
            public void mouseMoved(MouseEvent e){
                if(state == RUNNING){
                    int x = e.getX();
                    int y = e.getY();
                    hero.moveTo(x,y);
                }
            }
            /**重写mouseclcked鼠标点击事件*/
            public void mouseClicked(MouseEvent e){
                switch (state){
                    case START:
                        state=RUNNING;
                        break;
                    case GAME_OVER:
                        score = 0;
                        sky = new Sky();
                        hero= new Hero();
                        enemies = new FlyingObject[0];
                        bullets = new Bullet[0];
                        state=START;
                        break;
                }
            }
            /**鼠标移除事件*/
            public void mouseExited(MouseEvent e){
                state = PAUSE;
            }

            /**鼠标移入事件*/
            public void mouseEntered(MouseEvent e){
                state=RUNNING;
            }
        };
        this.addMouseListener(m);
        this.addMouseMotionListener(m);

        Timer timer = new Timer(); //定时器对象
        int interval = 10; //定时间隔(以毫秒为单位)
        timer.schedule(new TimerTask(){
            public void run(){ //定时干的事(每10毫秒走一次)
                if(state == RUNNING){
                    enterAction(); //敌人入场
                    shootAction(); //子弹入场
                    stepAction();//飞行物移动
                    outOfBoundsAction();//删除越界的敌人和子弹
                    BulletBangAction();//子弹与敌人的碰撞
                    HeroBangAction();//英雄机与敌人碰撞
                    checkGameOverAction();
                }
                repaint(); //重画(重新调用paint()方法)
            }
        },interval,interval); //定时计划表
    }

    /** 重写paint()画  g:画笔 */
    public void paint(Graphics g){ //每10毫秒走一次
        g.drawImage(sky.getImage(),sky.x,sky.y,null); //画天空
        g.drawImage(sky.getImage(),sky.x,sky.getY1(),null); //画天空2
        g.drawImage(hero.getImage(),hero.x,hero.y,null); //画英雄机
        for(int i=0;i<enemies.length;i++){ //遍历所有敌人
            FlyingObject f = enemies[i]; //获取每一个敌人
            g.drawImage(f.getImage(),f.x,f.y,null); //画敌人
        }
        for(int i=0;i<bullets.length;i++){ //遍历所有子弹
            Bullet b = bullets[i]; //获取每一个子弹
            g.drawImage(b.getImage(),b.x,b.y,null); //画子弹
        }
        g.drawString("score:"+score,10,10);
        g.drawString("life:"+ hero.getLife(),10,30);

        switch (state){
            case START:
                g.drawImage(Images.start,0,0,null);
                break;
            case PAUSE:
                g.drawImage(Images.pause,0,0,null);
                break;
            case GAME_OVER:
                g.drawImage(Images.gameover,0,0,null);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        World world = new World();
        frame.add(world);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH,HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true); //1)设置窗口可见  2)尽快调用paint()方法

        world.action(); //启动程序的执行
    }
}


/*
 1.问:为什么在main的外面创建引用?
   答:因为若将引用设计在main中，则引用就只能在main中被使用，其它方法都无法使用了，
     而World类中的会包含很多方法，这些方法中都会用到那一堆引用，
     所以将引用设计在main的外面，来扩大作用范围
 2.问:为什么要单独创建action()方法来测试?
   答:因为main()方法是static的，在static的方法中是无法访问那一堆引用的，
      所以单独创建一个非static的action()的来测试
      ----关于static的内容，面向对象第五天详细讲解
 3.问:为什么在main中得先创建World对象，而后再调用action()方法?
   答:因为main()方法是static的，在static的方法中是无法直接调用action()方法的，
      所以得先创建World对象，而后再调用action()方法
      ----关于static的内容，面向对象第五天详细讲解
 */

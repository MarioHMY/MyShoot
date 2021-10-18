package cn.tedu.shoot;
/** 奖励接口*/
public interface EnemyAward {
    public int FIRE = 0; //火力
    public int LIFE = 1; //命
    /**获取奖励类型（0或1）*/
    public int getAwardType();
}

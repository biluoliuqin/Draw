//实现统计抽卡次数、保底概率的功能。这里面不需要主函数。
class randomDraw  {
    //储存抽数和保底
    static int Count = 0;
    static int BasicCount = -50;
    static int BasicCount_p = -6;

    //储存抽卡结果
    static int FourStar = 0;
    static int FiveStar = 0;
    static int ThreeStar = 0;

    //储存修正概率
    static double BasicProbability = 0;
    static double BasicProbability_p = 0;

    //储存出卡类型
    static int Star = 0;
    //抽卡逻辑
    String raffle(){
        // 生成随机数
        double random = 0;
        //储存结果
        String result;
        //更新修正概率
        if(BasicCount>0)
            BasicProbability = BasicCount*0.05;
        // 根据保底次数，动态变化抽卡结果,未出金次数超过50次后，此后每次抽卡，五星概率增加5%
        if(BasicCount_p>0)
            BasicProbability_p = BasicCount_p*0.25;
        // 未出紫次数超过6次后，此后每次抽卡，四星概率增加10%
        random = Math.random();
        if (random < 0.01 + BasicProbability) {
            result = "五星";
            FiveStar++;
            Star+=100;
            BasicCount = -50;
            BasicProbability = 0;
        } else if (random < 0.03 + BasicProbability + BasicProbability_p) {
            result = "四星";
            FourStar++;
            Star+=10;
            BasicCount_p = -6;
            BasicProbability_p = 0;
        } else {
            result = "三星";
            ThreeStar++;
            Star+=1;
        }
        Count++;
        BasicCount++;
        BasicCount_p++;
        return result;
    }
    //访问出卡类型
    int getStar(){
        return Star;
    }
}
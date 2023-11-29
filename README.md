# Draw
Java课大作业，与朋友合作使用java的内置库swing实现的一个有简单GUI和动画的抽卡系统

# 运行环境配置说明
*Java Development Kit (JDK) 8 或更高版本

*一个支持Java的集成开发环境（IDE），例如IntelliJ IDEA或Eclipse

# 使用说明
示范的三星、四星、五星卡面图片由ai生成，
程序将从five_star、four_star、three_star的三个文件夹中随机选取图片。

因此，若您想要替换卡面，直接修改文件夹中的图片即可，而无需在意图片的名称.

代码文件在文件夹Draw/src中。

其中的代码文件：

GuiView.java为抽卡页面的gui实现

luckDrawResultView.java为展示的抽卡结果的gui实现

randomDraw.java为保底概率计算的算法实现

Main.java为主程序


# 保底概率算法说明
抽出五星的基础概率为1%。
连续未出五星次数超过50次后，此后每次抽卡，五星概率增加5%
抽出四星的基础概率为3%。
连续未出四星次数超过6次后，此后每次抽卡，四星概率增加25%。
出五星的优先级高于四星，且四星的保底不会因出五星而清除。

例：当抽数足够同时触发五星和四星保底时，下一抽将一定先出五星，再下一抽将一定出四星。

# 结果页面展示：抽卡页面
<img width="415" alt="image" src="https://github.com/LiJinHao999/Draw/assets/125203402/bdd151e5-07d5-4acf-bcc3-b91246c2ee39">


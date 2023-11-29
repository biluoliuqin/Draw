import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JLayeredPane; //导入JLayeredPane类
import javax.swing.JPanel;
import javax.swing.JButton; //导入JButton类
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

//图形页面绘制函数，实现图形页面显示，单抽按钮，十连抽按钮及关闭按钮，调用抽卡类中的结果，关闭按钮的功能直接在里面实现
class GuiView extends JFrame{
    private JButton but1, but2, but3;
    private JPanel pn0;
    //图形页面绘制
    public GuiView() {
        // 创建一个JFrame对象，该对象就是一个窗口
        JFrame frame = new JFrame("抽卡系统");
        frame.setBounds(100, 100, 2400, 1600);
        GridLayout gl = new GridLayout(4, 2, 5, 5); // 设置表格为3行两列排列，表格横向间距为5个像素，纵向间距为5个像素
        frame.setLayout(gl);

        // 背景组件
        ImageIcon imageIcon = new ImageIcon("draw/bg/bg.gif");
        Clip clip = null;
        //为背景添加声音
        try {
            // 获取音频输入流
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("draw/wav/bg.wav").getAbsoluteFile());

            // 创建 Clip 并打开音频输入流
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // 播放音频
            clip.start();

            //另外，当这个窗口在最上层的时候，播放音频
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        final Clip finalClip=clip;

        // 创建一个JLabel并设置其图标为ImageIcon对象
        JLabel label = new JLabel(imageIcon);

        // 将JLabel添加到窗口,并设置布局方式
        //add(label); //将背景标签添加到窗口
        setSize(2400, 1600);
        setContentPane(label); //将背景标签设置为内容面板
        label.setBounds(0, 0, getWidth(), getHeight()); //设置背景标签的大小和位置为窗口的大小和位置
        label.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT))); //设置背景标签的图标为缩放后的图像，以适应窗口的大小

        // 按钮组件，从外部引入三个图片按钮素材，替换按钮的背景图片，抽卡按钮是Draw\draw_button\one_draw_clear.png，十连抽按钮是Draw\draw_button\ten_draw_clear.png，关闭按钮是Draw\draw_button\close.png
        ImageIcon imageIcon1 = new ImageIcon("draw/draw_button/one_draw_clear.png");
        ImageIcon imageIcon2 = new ImageIcon("draw/draw_button/ten_draw_clear.png");
        ImageIcon imageIcon3 = new ImageIcon("draw/draw_button/close.png");

        //抽卡图片太大，将图片按比例缩小到五分之一的尺寸
        imageIcon1.setImage(imageIcon1.getImage().getScaledInstance(imageIcon1.getIconWidth()/5, imageIcon1.getIconHeight()/5, Image.SCALE_DEFAULT));
        imageIcon2.setImage(imageIcon2.getImage().getScaledInstance(imageIcon2.getIconWidth()/5, imageIcon2.getIconHeight()/5, Image.SCALE_DEFAULT));

        but1 = new JButton(" ");
        but1.setIcon(imageIcon1);
        pn0 = new JPanel();
        pn0.add(but1);
        add(pn0);


        //十连抽
        but2 = new JButton(" ");
        but2.setIcon(imageIcon2);
        pn0 = new JPanel();
        pn0.add(but2);
        add(pn0);

        //关闭按钮，点击后强制结束程序
        but3 = new JButton("");
        but3.setIcon(imageIcon3);
        pn0.add(but3);
        add(pn0);



        // 点击抽卡按钮弹出抽卡结果
        but1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                // 单抽卡
                OneDraw sck = new OneDraw(finalClip);
                sck.showRaffle();
                if(finalClip!=null){
                    finalClip.stop();
                }
            }
        });


        //点击十连抽按钮，在新页面里弹出十次的抽卡的结果
        but2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                // 十连抽卡
                TenDraw tck = new TenDraw(finalClip);
                tck.showRaffle();
                if(finalClip!=null){
                    finalClip.stop();
                }
            }
        });


        //监听图形上的×按钮，点击后强制结束程序
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //监听图形上的放大窗口按钮，点击后放大背景图片和按钮,再次点击后恢复原状
        addWindowStateListener(new WindowAdapter() {
            public void windowStateChanged(WindowEvent e) {
                if (e.getNewState() == Frame.MAXIMIZED_BOTH) {
                    label.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT)));
                } else if (e.getNewState() == Frame.NORMAL) {
                    label.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT)));
                }
            }
        });

        //强制结束程序
        but3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                System.exit(0);
            }
        });

        // 创建一个JLayeredPane，用于管理层次
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, getWidth(), getHeight()); //设置层次面板的大小和位置为窗口的大小和位置
        add(layeredPane); //将层次面板添加到窗口中

        // 将三个按钮添加到层次面板中，并设置它们的层次为最上层
        layeredPane.add(but1);
        layeredPane.setLayer(but1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(but2);
        layeredPane.setLayer(but2, JLayeredPane.POPUP_LAYER);
        layeredPane.add(but3);
        layeredPane.setLayer(but3, JLayeredPane.POPUP_LAYER);
        //调整三个按钮的位置，从左到右放在页面底部，大小为图片，要合理安排位置，使得按钮不会被遮挡
        but1.setBounds(400, 800, imageIcon1.getIconWidth(), imageIcon1.getIconHeight());
        but2.setBounds(1000,800, imageIcon2.getIconWidth(), imageIcon2.getIconHeight());

        //关闭按钮放在页面右上角
        but3.setBounds(1520, 25, imageIcon3.getIconWidth(), imageIcon3.getIconHeight());

        //创建一个渐变色标签，用于显示抽卡结果
        class GradientLabel extends JLabel{
            private static final long serialVersionUID = 1L;
            public GradientLabel(String text) {
                super(text);
                setOpaque(true);
            }
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                int width = getWidth();
                int height = getHeight();
                GradientPaint gp=new GradientPaint(0, 0, Color.CYAN, width, height, Color.MAGENTA);
                g2d.setPaint(gp);
                g2d.drawString(getText(),0,height/2);
                g2d.dispose();

            }
        }

        //在右上角显示当前抽数和保底情况，显示抽卡结果，并且1s都会刷新一次显示
        GradientLabel label1 = new GradientLabel("当前抽数："+randomDraw.Count+"当前出金概率："+(0.01+randomDraw.BasicProbability)*100+"%");
        label1.setFont(new Font("宋体", Font.BOLD, 30));
        label1.setBounds(625, 0, 800, 100);
        label1.setHorizontalAlignment(JLabel.CENTER);
        layeredPane.add(label1);
        layeredPane.setLayer(label1, JLayeredPane.POPUP_LAYER);
        GradientLabel label2 = new GradientLabel("抽卡结果：五星"+randomDraw.FiveStar+"个，四星"+randomDraw.FourStar+"个，三星"+randomDraw.ThreeStar+"个");
        label2.setFont(new Font("宋体", Font.BOLD, 30));
        label2.setBounds(585, 50, 800, 100);
        label2.setHorizontalAlignment(JLabel.CENTER);
        layeredPane.add(label2);
        layeredPane.setLayer(label2, JLayeredPane.POPUP_LAYER);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    label1.setText("当前抽数："+randomDraw.Count+"当前出金概率："+(0.01+randomDraw.BasicProbability)*100+"%");
                    label2.setText("抽卡结果：五星"+randomDraw.FiveStar+"个，四星"+randomDraw.FourStar+"个，三星"+randomDraw.ThreeStar+"个");
                }
            }
        }).start();
        
        // 基本组件已完成
        setVisible(true);
    }
}
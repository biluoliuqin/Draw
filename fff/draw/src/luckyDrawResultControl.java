//实现抽卡功能，单抽、十连抽
//存在交互，点击一次抽卡按钮弹出结果
//结果为五星、四星和三星中的一个
//结果弹出显示
//点击关闭按钮关闭程序
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.io.FilenameFilter;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import java.awt.event.WindowEvent;
import javax.sound.sampled.FloatControl;

class MyPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private BufferedImage image;

    public MyPanel(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}


//十连抽逻辑和十连抽结果显示
class TenDraw{
    private Clip clip;
    private JFrame frame;
    private JLabel imageLabel;
    private int currentImageIndex;
    protected JPanel starPanel;

    public TenDraw(Clip clip) {
        this.clip = clip;
        this.currentImageIndex = 0;
        initializeGUI();

        // 当窗口打开时，降低背景音乐的音量
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-20.0f);  // 调整音量
            }
            @Override
            public void windowClosed(WindowEvent e) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(6.0f);  // 恢复音量
            }
        });
    }

    private void playAnimation(int star) {
        String dirPath = ""; // 根据星级选择不同的动画
        if (star > 100) {
            dirPath = "draw/animation/GOLD.gif";
        } else if (star > 10) {
            dirPath = "draw/animation/purple.gif";
        } else if (star > 1) {
            dirPath = "draw/animation/one_blue.gif";
        }

        System.out.println("star: " + star);
        ImageIcon imageIcon = new ImageIcon(dirPath);
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(imageIcon.getIconWidth()*3, imageIcon.getIconHeight()*3, Image.SCALE_DEFAULT));
        imageLabel.setIcon(imageIcon);
    }

    private void initializeGUI() {
        frame = new JFrame("抽卡结果");
        frame.setBounds(0, 0, 2400, 1600);

        //调用windows命令，窗口默认最大化显示
        frame = new JFrame("抽卡结果");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);  // 添加这一行
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER); // 设置水平对齐方式为居中
        imageLabel.setVerticalAlignment(JLabel.CENTER); // 设置垂直对齐方式为居中
        frame.add(imageLabel);

        // 在初始化GUI时，添加一个专门用于显示星星的JPanel
        starPanel = new JPanel();
        starPanel.setLayout(new BoxLayout(starPanel, BoxLayout.Y_AXIS));
        frame.add(starPanel, BorderLayout.SOUTH);

        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e)  {
                updateImage();
                /*FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-20.0f);*/
            }
        });
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                clip.start();
            }
        });
    }

    //先定义一个十连抽函数，提前得到结果，返回值为数组PreResult
    String[] PreResult = new String[10];

    int PreTenRaffle(){
        int sum = 0;
        for(int i=0;i<10;i++){
            PreResult[i] = getResult();
            if(PreResult[i].equals("五星")){
                sum+=100;
            }
            else if(PreResult[i].equals("四星")){
                sum+=10;
            }
            else{
                sum+=1;
            }
        }
        return sum;
    }

    private void updateImage() {

        if (currentImageIndex < 10) {
            String result = PreResult[currentImageIndex];
            String dirPath;

            // 创建一个新的JPanel，用于存放星星
            JPanel starsPanel = new JPanel();
            starsPanel.setLayout(new BoxLayout(starsPanel, BoxLayout.X_AXIS));

            if (result.equals("三星")) {
                frame.getContentPane().setBackground(Color.CYAN);
                dirPath = "draw/three_star";
                try {
                    // 播放音频
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("draw/wav/display_wav.wav").getAbsoluteFile());
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);

                    // 获取音量控制，并增大音量
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(gainControl.getMaximum());  // 设置音量为最大值


                    clip.start();
                } catch (Exception ex) {
                    System.out.println("Error with playing sound.");
                    ex.printStackTrace();
                }

                //根据结果显示星星
                for (int i = 0; i < 3; i++) {
                    // 为每个星星创建一个新的JLabel，将星星图片设置为它的图标
                    ImageIcon starIcon = new ImageIcon("draw/bg/star.png");
                    JLabel starLabel = new JLabel();
                    starLabel.setIcon(starIcon);
                    starsPanel.add(starLabel);
                }
            } else if (result.equals("四星")) {
                frame.getContentPane().setBackground(new Color(221, 160, 221)); // Light purple
                dirPath = "draw/four_star";
                try {
                    // 播放音频
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("draw/wav/display_wav.wav").getAbsoluteFile());
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                } catch (Exception ex) {
                    System.out.println("Error with playing sound.");
                    ex.printStackTrace();
                }

                for (int i = 0; i < 4; i++) {
                    // 为每个星星创建一个新的JLabel，将星星图片设置为它的图标
                    ImageIcon starIcon = new ImageIcon("draw/bg/star.png");
                    JLabel starLabel = new JLabel();
                    starLabel.setIcon(starIcon);
                    starsPanel.add(starLabel);
                }
            } else {
                frame.getContentPane().setBackground(new Color(255, 230, 128)); // Gold
                dirPath = "draw/five_star";
                try {
                    // 播放音频
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("draw/wav/gold.wav").getAbsoluteFile());
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                } catch (Exception ex) {
                    System.out.println("Error with playing sound.");
                    ex.printStackTrace();
                }

                for (int i = 0; i < 5; i++) {
                    // 为每个星星创建一个新的JLabel，将星星图片设置为它的图标
                    ImageIcon starIcon = new ImageIcon("draw/bg/star.png");
                    JLabel starLabel = new JLabel();
                    starLabel.setIcon(starIcon);
                    starsPanel.add(starLabel);
                }
            }
            // 在添加新的星星面板之前，先从窗口中移除旧的星星面板
            starPanel.removeAll();

            // 将星星面板添加到starsPanel中
            starPanel.add(starsPanel);

            // 重新绘制窗口
            starPanel.revalidate();
            starPanel.repaint();

            File dir = new File(dirPath);
            FilenameFilter imageFilter = (dir1, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".psd");
            File[] files = dir.listFiles(imageFilter);

            if (files != null && files.length > 0) {
                Random rand = new Random();
                File image = files[rand.nextInt(files.length)];
                ImageIcon imageIcon = new ImageIcon(image.getPath());
                imageLabel.setIcon(imageIcon);
                currentImageIndex++;
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-20.0f);
            }
        } else {
            // 已经显示了所有图片，可以添加相应的处理逻辑
            System.out.println("已经显示了所有图片");
            //监听鼠标，点击屏幕后直接关闭窗口，
            frame.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e)  {
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                }
            });
        }
    }

    private String getResult() {
        // 这里替换成你的抽卡逻辑
        randomDraw draw = new randomDraw();
        return draw.raffle();
    }

    public void showRaffle() {
        // 初始化GUI并显示窗口
        frame.setVisible(true);

        // 预计算十连抽结果并播放动画
        playAnimation(PreTenRaffle());

        //同时播放音频Draw/wav/ten_draw.wav
        try {
            // 获取音频输入流
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("draw/wav/ten_draw.wav").getAbsoluteFile());

            // 创建 Clip 并打开音频输入流
            Clip ten_draw = AudioSystem.getClip();
            ten_draw.open(audioInputStream);

            // 播放音频
            ten_draw.start();

            //添加一个鼠标监听，点击以后，立刻不播放此音频
            frame.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e)  {
                    ten_draw.stop();
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        
        // 延迟7秒后执行你的操作，只执行一次
        new Timer(7000, e  -> {
            SwingUtilities.invokeLater(() -> {

                updateImage(); // 显示第一张图片
                
            });
            ((Timer)e.getSource()).stop(); // 停止Timer
        }).start();
    }
}

//类似地，单抽卡并显示
class OneDraw {
    private Clip clip;
    private JFrame frame;
    private JLabel imageLabel;
    private int currentImageIndex;
    protected JPanel starPanel;

    public OneDraw(Clip clip) {
        this.clip = clip;
        this.currentImageIndex = 0;
        initializeGUI();

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);  // 添加这一行
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // 当窗口打开时，降低背景音乐的音量
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-20.0f);  // 调整音量
            }
            @Override
            public void windowClosed(WindowEvent e) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(6.0f);  // 恢复音量
            }
        });
    }

    private void playAnimation(int star) {
        String dirPath = ""; // 根据星级选择不同的动画
        if (star == 100) {
            dirPath = "draw/animation/one_gold.gif";
        } else if (star == 10) {
            dirPath = "draw/animation/one_purple.gif";
        } else if (star == 1) {
            dirPath = "draw/animation/one_blue.gif";
        }

        System.out.println("star: " + star);
        ImageIcon imageIcon = new ImageIcon(dirPath);
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(imageIcon.getIconWidth()*3, imageIcon.getIconHeight()*3, Image.SCALE_DEFAULT));
        imageLabel.setIcon(imageIcon);
    }

    private void initializeGUI() {
        frame = new JFrame("抽卡结果");
        frame.setBounds(0, 0, 2400, 1600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER); // 设置水平对齐方式为居中
        imageLabel.setVerticalAlignment(JLabel.CENTER); // 设置垂直对齐方式为居中
        frame.add(imageLabel);

        // 在初始化GUI时，添加一个专门用于显示星星的JPanel
        starPanel = new JPanel();
        starPanel.setLayout(new BoxLayout(starPanel, BoxLayout.Y_AXIS));
        frame.add(starPanel, BorderLayout.SOUTH);

        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e)  {
                updateImage();
            }
        });
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                clip.start();
            }
        });
    }

    //先定义一个单抽函数，提前得到结果，返回值为数组PreResult
    String PreResult;

    int PreTenRaffle(){
        int sum = 0;
        PreResult = getResult();
        if(PreResult.equals("五星")){
            sum+=100;
        }
        else if(PreResult.equals("四星")){
            sum+=10;
        }
        else{
            sum+=1;
        }
        return sum;
    }

    private void updateImage() {

        if (currentImageIndex < 1) {
            String result = PreResult;
            String dirPath;

            // 创建一个新的JPanel，用于存放星星
            JPanel starsPanel = new JPanel();
            starsPanel.setLayout(new BoxLayout(starsPanel, BoxLayout.X_AXIS));

            if (result.equals("三星")) {
                frame.getContentPane().setBackground(Color.CYAN);
                dirPath = "draw/three_star";

                for (int i = 0; i < 3; i++) {
                    // 为每个星星创建一个新的JLabel，将星星图片设置为它的图标
                    ImageIcon starIcon = new ImageIcon("draw/bg/star.png");
                    JLabel starLabel = new JLabel();
                    starLabel.setIcon(starIcon);
                    starsPanel.add(starLabel);
                }

            } else if (result.equals("四星")) {
                frame.getContentPane().setBackground(new Color(221, 160, 221)); // Light purple
                dirPath = "draw/four_star";

                for (int i = 0; i < 4; i++) {
                    // 为每个星星创建一个新的JLabel，将星星图片设置为它的图标
                    ImageIcon starIcon = new ImageIcon("draw/bg/star.png");
                    JLabel starLabel = new JLabel();
                    starLabel.setIcon(starIcon);
                    starsPanel.add(starLabel);
                }
            } else {
                frame.getContentPane().setBackground(new Color(255, 230, 128)); // Gold
                dirPath = "draw/five_star";
                try {
                    // 播放音频
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("fff/draw/wav/gold.wav").getAbsoluteFile());
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                } catch (Exception ex) {
                    System.out.println("Error with playing sound.");
                    ex.printStackTrace();
                }

                for (int i = 0; i < 5; i++) {
                    // 为每个星星创建一个新的JLabel，将星星图片设置为它的图标
                    ImageIcon starIcon = new ImageIcon("draw/bg/star.png");
                    JLabel starLabel = new JLabel();
                    starLabel.setIcon(starIcon);
                    starsPanel.add(starLabel);
                }

            }

            // 在添加新的星星面板之前，先从窗口中移除旧的星星面板
            starPanel.removeAll();

            // 将星星面板添加到starsPanel中
            starPanel.add(starsPanel);

            // 重新绘制窗口
            starPanel.revalidate();
            starPanel.repaint();


            File dir = new File(dirPath);
            FilenameFilter imageFilter = (dir1, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".psd");
            File[] files = dir.listFiles(imageFilter);

            if (files != null && files.length > 0) {
                Random rand = new Random();
                File image = files[rand.nextInt(files.length)];
                ImageIcon imageIcon = new ImageIcon(image.getPath());
                imageLabel.setIcon(imageIcon);
                currentImageIndex++;
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-20.0f);
            }
        } else {
            // 已经显示了所有图片，可以添加相应的处理逻辑
            System.out.println("已经显示了所有图片");
            //监听鼠标，点击屏幕后直接关闭窗口
            frame.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e)  {
                    frame.dispose();
                    //增大回原来音量
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(6.0f);
                }
            });
        }
    }

    private String getResult() {
        // 这里替换成你的抽卡逻辑
        randomDraw draw = new randomDraw();
        return draw.raffle();
    }

    public void showRaffle() {
        // 初始化GUI并显示窗口
        frame.setVisible(true);

        // 预计算十连抽结果并播放动画
        playAnimation(PreTenRaffle());
        //同时播放音频Draw/wav/ten_draw.wav
        try {
            // 获取音频输入流
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("draw/wav/one_draw.WAV").getAbsoluteFile());

            // 创建 Clip 并打开音频输入流
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // 播放音频
            clip.start();

            //添加一个鼠标监听，点击以后，不播放此音频
            frame.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e)  {
                    clip.stop();
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }


        // 延迟7秒后执行你的操作，只执行一次
        new Timer(7000, e  -> {
            SwingUtilities.invokeLater(() -> {
                updateImage(); // 显示第一张图片

            });
            ((Timer)e.getSource()).stop(); // 停止Timer
        }).start();
    }
}
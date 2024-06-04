package com.hamza.youtubedownload.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Other_Effects {

    private static final Effect frostEffect =
            new BoxBlur(10, 10, 3);
    //    private static final Effect frostEffect =
//            new GaussianBlur(70.0);

    public void boxBlur(){
        BoxBlur boxblur = new BoxBlur();

        //Setting the width of the box filter
        boxblur.setWidth(8.0f);

        //Setting the height of the box filter
        boxblur.setHeight(3.0f);

        //Setting the no of iterations
        boxblur.setIterations(3);

    }

    private Image copyBackground(Stage stage) {
        final int X = (int) stage.getX();
        final int Y = (int) stage.getY();
        final int W = (int) stage.getWidth();
        final int H = (int) stage.getHeight();

        try {
            java.awt.Robot robot = new java.awt.Robot();
            java.awt.image.BufferedImage image = robot.createScreenCapture(new java.awt.Rectangle(X, Y, W, H));

            return SwingFXUtils.toFXImage(image, null);
        } catch (java.awt.AWTException e) {
            System.out.println("The robot of doom strikes!");
            e.printStackTrace();

            return null;
        }
    }


}

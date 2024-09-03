package com.tianji.dam.controller;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;

public class TransPic {

    public static void main(String[] argv) throws Exception {
        File in = new File("D:\\img\\abc.jpg");
        BufferedImage source = ImageIO.read(in);

        int color = source.getRGB(0, 0);

        Image imageWithTransparentBackground = makeColorTransparent(source, new Color(color));

        BufferedImage transparentImage = imageToBufferedImage(imageWithTransparentBackground);

        File out = new File("D:\\img\\abc.png");
        ImageIO.write(transparentImage, "PNG", out);
    }

    private static Image makeColorTransparent(BufferedImage im, final Color color) {
        ImageFilter filter = new RGBImageFilter() {
            public int markerRGB = color.getRGB() | 0xFF000000;

            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                } else {
                    // nothing to do
                    return rgb;
                }
            }
        };

        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    private static BufferedImage imageToBufferedImage(Image image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return bufferedImage;
    }


}

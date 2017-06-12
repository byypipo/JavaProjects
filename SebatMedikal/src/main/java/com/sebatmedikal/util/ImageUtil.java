package com.sebatmedikal.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageUtil {
	public static byte[] resize(byte[] image) throws Exception {
		if (image == null) {
			LogUtil.logMessage(ImageUtil.class, "image cannot be null.");
			return null;
		}

		int newW = 300;
		int newH = 300;

		LogUtil.logMessage(ImageUtil.class, "BEFORE" + image.length);

		InputStream in = new ByteArrayInputStream(image);
		BufferedImage originalImage = ImageIO.read(in);
		BufferedImage resizeImage = resize(originalImage, newW, newH);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(resizeImage, "jpg", baos);
		baos.flush();
		byte[] resizedImage = baos.toByteArray();
		baos.close();

		LogUtil.logMessage(ImageUtil.class, "AFTER" + resizedImage.length);

		return resizedImage;
	}

	private static BufferedImage resize(BufferedImage img, int newW, int newH) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
		Graphics2D g = dimg.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
		g.dispose();
		return dimg;
	}
}

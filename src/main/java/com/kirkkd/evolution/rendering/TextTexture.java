package com.kirkkd.evolution.rendering;

import com.kirkkd.evolution.util.Color;
import com.kirkkd.evolution.util.Vec2d;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;

import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_BGRA;

public class TextTexture {
    private final int texID;
    private final int size;
    private final BufferedImage buf;
    private final Graphics2D g;
    private final ByteBuffer pixels;

    public TextTexture(int size, int fontSize) {
        this.size = size;
        buf = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
        g = buf.createGraphics();
        g.setFont(new Font("Arial", Font.PLAIN, fontSize));
//        g.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
        byte[] data = ((DataBufferByte)buf.getRaster().getDataBuffer()).getData();
        pixels = BufferUtils.createByteBuffer(data.length);

        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D,
                0,
                GL_RGBA8,
                size, size,
                0,
                GL_BGRA,
                GL_UNSIGNED_BYTE,
                (ByteBuffer)null);
    }

    public void updateText(String text, Color foreground) {
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, size, size);
        g.setComposite(AlphaComposite.SrcOver);

        g.setColor(new java.awt.Color(foreground.r, foreground.g, foreground.b, foreground.a));
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        int x = (size - textWidth) / 2;
        int y = (size - fm.getHeight()) / 2 + textHeight;
        g.drawString(text, x, y);

        byte[] data = ((DataBufferByte)buf.getRaster().getDataBuffer()).getData();
        pixels.clear();
        pixels.put(data);
        pixels.flip();

        glBindTexture(GL_TEXTURE_2D, texID);
        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, size, size,
                GL_BGRA, GL_UNSIGNED_BYTE, pixels);
    }

    public void render(Vec2d position) {
        double halfSize = size / 2.0;
        double x = position.x() - halfSize;
        double y = position.y() - halfSize;

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, texID);
        glBegin(GL_QUADS);
        glTexCoord2d(0, 0); glVertex2d(x, y);
        glTexCoord2d(1, 0); glVertex2d(x + size, y);
        glTexCoord2d(1, 1); glVertex2d(x + size, y + size);
        glTexCoord2d(0, 1); glVertex2d(x, y + size);
        glEnd();
        glDisable(GL_TEXTURE_2D);
    }

    public int getTextureID() { return texID; }
}


/*
 *  DLOG_RandomImageServlet.java
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Library General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  Author: Winter Lau
 *  http://dlog4j.sourceforge.net
 *
 */
package irille.pub.verify;

import org.apache.commons.lang3.RandomStringUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

/**
 * 用于产生注册用户时的随即图片以防止非法攻击
 *
 * @author liudong
 */
public class RandomImageServlet extends HttpServlet {
    public final static String RANDOM_LOGIN_KEY = "RANDOM_LOGIN_KEY";

    public void init() throws ServletException {
        System.setProperty("java.awt.headless", "true");
    }

    public static String getRandomLoginKey(HttpServletRequest req) {
        return (String) req.getSession().getAttribute(RANDOM_LOGIN_KEY);
    }

    /**
     * 随即生成验证图片并存放于HTTP会话中
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        boolean gif_enabled = support(req, "image/gif")
                || !support(req, "image/png");
        HttpSession ssn = req.getSession(true);
        String randomString = random();
        ssn.setAttribute(RANDOM_LOGIN_KEY, randomString);
        ;
        res.setContentType(gif_enabled ? "image/gif" : "image/png");
        res.setHeader("Pragma", "No-cache");
        res.setHeader("Cache-Control", "no-cache");
        res.setDateHeader("Expires", 0);
        /**
         * 增加颜色参数生成图片字体根据前台参数设定
         */
        render(randomString, gif_enabled, req.getParameter("fontcolor"), req.getParameter("background"), res.getOutputStream());
    }

    protected static String random() {
        return RandomStringUtils.randomNumeric(4);
    }

    /**
     * 根据要求的数字生成图片,背景为白色,字体大小16,字体颜色黑色粗体
     *
     * @param num 要生成的数字
     * @param out 输出流
     * @throws IOException
     */
    protected static void render(String num, boolean gif, String fontcolor, String background, OutputStream out) throws IOException {
        if (num.getBytes().length > 4)
            throw new IllegalArgumentException("The length of param num cannot exceed 4.");
        int width = 50;
        int height = 18;
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        g.setColor(getColor(background,Color.WHITE));
        g.fillRect(0, 0, width, height);
        Font mFont = new Font("Tahoma", Font.BOLD | Font.ITALIC, 16);
        g.setFont(mFont);
        g.setColor(getColor(fontcolor,Color.BLACK));
        g.drawString(num, 2, 15);
        if (gif) {
            AnimatedGifEncoder e = new AnimatedGifEncoder();
            e.setTransparent(Color.WHITE);
            e.start(out);
            e.setDelay(0);
            e.addFrame(bi);
            e.finish();
        } else {
            ImageIO.write(bi, "png", out);
        }
    }

    private static Color getColor(String color, Color defaultColor) {
        Color setColor = null;
        if (color != null && color.length() > 0) {
            if (color.indexOf("#") == -1) {
                setColor = Color.decode("#" + color);
            } else {
                setColor = Color.decode(color);
            }
            return setColor;
        }
        return defaultColor;
    }

    public static String getHeader(HttpServletRequest req, String name) {
        String value = req.getHeader(name);
        if (value != null)
            return value;
        Enumeration names = req.getHeaderNames();
        while (names.hasMoreElements()) {
            String n = (String) names.nextElement();
            if (n.equalsIgnoreCase(name)) {
                return req.getHeader(n);
            }
        }
        return null;
    }

    public static boolean support(HttpServletRequest req, String contentType) {
        String accept = getHeader(req, "accept");
        if (accept != null) {
            accept = accept.toLowerCase();
            return accept.indexOf(contentType.toLowerCase()) != -1;
        }
        return false;
    }

    protected static void main(String[] args) throws IOException {
//        String num = random();
//        System.out.println(num);
//        render(num, true, "#FFF", new FileOutputStream("D:\\test.gif"));
//        System.out.println("Image generated.");
    }
}

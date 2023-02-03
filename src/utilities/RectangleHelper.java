package utilities;

import java.awt.*;

public class RectangleHelper {
    public static boolean compare(Rectangle rect1, Rectangle rect2) {
        if (rect1 != null && rect2 != null) {
            return rect1.x == rect2.x && rect1.y == rect2.y && rect1.width == rect2.width && rect1.height == rect2.height;
        } else {
            return true;
        }
    }
}

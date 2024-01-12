package utilizations;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.example.Game;

public class LoadSave {

    public static final String PLAYER_ATLAS = "src/main/resources/enchant_sprite1.png";
    public static final String LEVEL_ATLAS = "src/main/resources/outside_sprites.png";
    public static final String LEVEL_ONE_DATA = "src/main/resources/level_one_data.png";
    public static final String MENU_BUTTONS = "src/main/resources/buttons_menu.png";
    public static final String MENU = "src/main/resources/menu.png";
    public static final String PAUSE = "src/main/resources/pause.png";
    public static final String SOUND_BUTTON = "src/main/resources/sound_button.png";
    public static final String URM_BUTTONS = "src/main/resources/urm_buttons.png";
    public static final String VOLUME_BUTTONS = "src/main/resources/volume_buttons.png";


    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage image = null;
        File is = new File(fileName);
        //InputStream is = LoadSave.class.getResourceAsStream("src/main/resources/enchant_sprite1.png");
        try {
            image = ImageIO.read(is);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
         return image;
        }

    public static int[][] GetLevelData() {
        int[][] lvlData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        BufferedImage image = GetSpriteAtlas(LEVEL_ONE_DATA);

        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getRed();
                if (value >= 48){
                    value = 0;
                }
                lvlData[j][i] = value;
            }
        }

        return lvlData;
    }
}

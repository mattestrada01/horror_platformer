package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.example.Game;

import utilizations.LoadSave;
import static utilizations.constants.PlayerConstants.*;
import static utilizations.helper.CanMoveHere;

public class Player extends Entity{

    private BufferedImage[][] animations;
    private int animationTick, animationIndex, animationSpeed = 40;
    private int playerAction = IDLE;
    private boolean left, right, up, down;
    private boolean moving = false, attacking = false, attacking2 = false, jumping = false, dead = false;
    private float playerSpeed = 2.0f;
    private int[][] lvlData;
    private float xOffset = 21*Game.SCALE;
    private float yOffset = 30*Game.SCALE;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitbox(x, y, 22*Game.SCALE, 34*Game.SCALE);
    }

    public void update() {
        updatePostion();
        updateAnimation();
        setAnimation();
    }

    public void render(Graphics g) {
        g.drawImage(animations[playerAction][animationIndex], (int)(hitbox.x - xOffset), (int)(hitbox.y - yOffset), width, height, null);
        drawHitbox(g);
    }

    private void updateAnimation() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if(animationIndex >= GetSpriteID(playerAction)) {
                animationIndex = 0;
                attacking = false;
                attacking2 = false;
                jumping = false;
                dead = false;
            }
        }
    }

    private void setAnimation() {

        int startAnimation = playerAction;

        if (moving) {
            playerAction = RUNNING;
            this.animationSpeed = 20;

        }
        else {
            playerAction = IDLE;
            this.animationSpeed = 40;

        }

        if (attacking) {
            playerAction = ATTACK_1;
            this.animationSpeed = 15;
        }

        if (attacking2) {
            playerAction = ATTACK_2;
            this.animationSpeed = 10;
        }

        if (jumping) {
            playerAction = JUMPING;
            this.animationSpeed = 15;
        }

        if (dead) {
            playerAction = DEAD;
            this.animationSpeed = 30;
        }

        if (startAnimation != playerAction) {
            resetAniTick();
        }
    }

    private void resetAniTick() {
        animationTick = 0;
        animationIndex = 0;
    }

    private void updatePostion() {

        moving = false;

        if(!left && !right && !up && !down){
            return;
        }

        float xSpeed = 0, ySpeed = 0;

        if (left && !right) {
            //moving = true;
            xSpeed = -playerSpeed;
       }
        else if (right && !left) {
            //moving = true;
            xSpeed = playerSpeed;
       }

        if (up && !down) {
            //moving = true;
            ySpeed = -playerSpeed;
       }
        else if (down && !up) {
            //moving = true;
            ySpeed = playerSpeed;
       }

   //    if(CanMoveHere(x+xSpeed, y+ySpeed, width, height, lvlData)){
   //         this.x += xSpeed;
   //        this.y += ySpeed;
   //         moving = true;
   //    }

          if(CanMoveHere(hitbox.x+xSpeed, hitbox.y+ySpeed, hitbox.width, hitbox.height, lvlData)){
            hitbox.x += xSpeed;
            hitbox.y += ySpeed;
            moving = true;
       }
    }

    public void setAnimationSpeed(int speed) {
        this.animationSpeed = speed;
    }

    private void loadAnimations() {

        //File is = new File("src/main/resources/enchant_sprite1.png");
        
        BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new BufferedImage[8][10];
        for (int j = 0; j < animations.length; j++){
             for (int i = 0; i < animations[j].length; i++){
                animations[j][i] = image.getSubimage(i*128, j*128, 128, 128);
            }
        }   
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void resetDirectionBooleans() {
        up = false;
        down = false;
        left = false;
        right = false;
    } 

    public void setAttack(boolean attacking) {
        this.attacking = attacking;
    }

    public void setAttack2(boolean attacking2) {
        this.attacking2 = attacking2;
    }

    public void setJump(boolean jumping) {
        this.jumping = jumping;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}

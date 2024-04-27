package entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.atomic.Atomic;

public class Player {
    private static final float SPEED = 300;

    private static final float ANIMATION_SPEED = 0.5f;
    private static final int PLAYER_PIXEL_WIDTH = 48;
    private static final int PLAYER_PIXEL_HEIGHT = 48;
    private static final int PLAYER_WIDTH = PLAYER_PIXEL_WIDTH*3;
    private static final int PLAYER_HEIGHT = PLAYER_PIXEL_HEIGHT*3;
    private static final float SWITCH_STATE_DELAY = 0.1f;
    private static final float SWITCH_STATE_JUMP_DELAY = 0.18f;
    private static final float JUMP_SPEED = SPEED*2.5f;

    float x, y, yBeforeJump;
    float stateTime = 0;

    int currentState;
    int currentDirection;

    private Animation[][] playerAnimation;

    private enum playerState{IDLE, WALK}
    private enum direction{DOWN,UP,LEFT,RIGHT}
    private boolean isMoving = false;
    private boolean isJumping = false;
    private float jumpTimer;
    private float stateTimer;

    public Player(){
        loadAnimation();

        currentState = playerState.IDLE.ordinal();
        currentDirection = direction.UP.ordinal();

        x = 0;
        y = 0;
        stateTimer = 0;
        jumpTimer = 0;
    }

    private void loadAnimation(){
        //Declaring Player Animation
        playerAnimation = new Animation[playerState.values().length][];
        playerAnimation[playerState.IDLE.ordinal()] = new Animation[direction.values().length];
        playerAnimation[playerState.WALK.ordinal()] = new Animation[direction.values().length];

        TextureRegion[][] playerIdleSpriteSheet= TextureRegion.split(new Texture("Characters/Basic Character Spritesheet Idle.png"),PLAYER_PIXEL_WIDTH,PLAYER_PIXEL_HEIGHT);
        TextureRegion[][] playerWalkSpriteSheet= TextureRegion.split(new Texture("Characters/Basic Character Spritesheet Walk.png"),PLAYER_PIXEL_WIDTH,PLAYER_PIXEL_HEIGHT);

        for (direction _direction : direction.values()) {
            playerAnimation[playerState.IDLE.ordinal()][_direction.ordinal()] = new Animation(ANIMATION_SPEED, playerIdleSpriteSheet[_direction.ordinal()]);
            playerAnimation[playerState.WALK.ordinal()][_direction.ordinal()] = new Animation(ANIMATION_SPEED, playerWalkSpriteSheet[_direction.ordinal()]);
        }
    }

    public void action(){
        if (isJumping){
            if (jumpTimer <= SWITCH_STATE_JUMP_DELAY/2 && jumpTimer >= 0) {
                if (y + (JUMP_SPEED * Gdx.graphics.getDeltaTime()) < yBeforeJump + (JUMP_SPEED * SWITCH_STATE_JUMP_DELAY)){
                    y += JUMP_SPEED * Gdx.graphics.getDeltaTime();
                }
                else if(y + (JUMP_SPEED * Gdx.graphics.getDeltaTime()) > yBeforeJump + (JUMP_SPEED * SWITCH_STATE_JUMP_DELAY)){
                    y = yBeforeJump + (JUMP_SPEED * SWITCH_STATE_JUMP_DELAY);
                }
            }
            else if (jumpTimer >= SWITCH_STATE_JUMP_DELAY/2 && jumpTimer <= SWITCH_STATE_JUMP_DELAY){
                if (y - (JUMP_SPEED * Gdx.graphics.getDeltaTime()) > yBeforeJump){
                    y -= JUMP_SPEED * Gdx.graphics.getDeltaTime();
                }
                else if(y - (JUMP_SPEED * Gdx.graphics.getDeltaTime()) < yBeforeJump){
                    y = yBeforeJump;
                }
            }
            else if(jumpTimer >= SWITCH_STATE_JUMP_DELAY){
                isJumping = false;
                jumpTimer = 0;
                stateTime = 0;
                y = yBeforeJump;
            }
            jumpTimer += Gdx.graphics.getDeltaTime();
        }

        else {
            if (isMoving) {
                if (stateTimer < SWITCH_STATE_DELAY) {
                    stateTimer += Gdx.graphics.getDeltaTime();
                } else {
                    isMoving = false;
                    stateTimer = 0;
                    stateTime = 0;
                }
            }

            //Controls
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                if (y+(SPEED*Gdx.graphics.getDeltaTime()) < Atomic.HEIGHT) {
                    y += SPEED * Gdx.graphics.getDeltaTime();
                } else{
                    y = Atomic.HEIGHT;
                }

                isMoving = true;
                currentDirection = direction.UP.ordinal();
                stateTimer = 0;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                if (y-(SPEED*Gdx.graphics.getDeltaTime()) > 0) {
                    y -= SPEED * Gdx.graphics.getDeltaTime();
                } else{
                    y = 0;
                }

                isMoving = true;
                currentDirection = direction.DOWN.ordinal();
                stateTimer = 0;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                if (x+(SPEED*Gdx.graphics.getDeltaTime()) < Atomic.WIDTH) {
                    x += SPEED * Gdx.graphics.getDeltaTime();
                } else{
                    x = Atomic.WIDTH;
                }

                isMoving = true;
                currentDirection = direction.RIGHT.ordinal();
                stateTimer = 0;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                if (x-(SPEED*Gdx.graphics.getDeltaTime()) > 0) {
                    x -= SPEED * Gdx.graphics.getDeltaTime();
                } else{
                    x = 0;
                }

                isMoving = true;
                currentDirection = direction.LEFT.ordinal();
                stateTimer = 0;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.J)) {
                yBeforeJump = y;
                isJumping = true;
            }
        }

        if (isMoving){
            currentState = playerState.WALK.ordinal();
        }
        else {
            currentState = playerState.IDLE.ordinal();
        }

        stateTime += Gdx.graphics.getDeltaTime();
//        System.out.println(isJumping);
//        System.out.println("x: " + x + ", y: " + y);
    }

    public TextureRegion getPlayerKeyFrame(){
        return (TextureRegion) playerAnimation[currentState][currentDirection].getKeyFrame(stateTime, true);
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public int getPlayerWidth(){
        return PLAYER_WIDTH;
    }

    public int getPlayerHeight(){
        return PLAYER_HEIGHT;
    }
}

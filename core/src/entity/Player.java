package entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.atomic.Atomic;

public class Player {
    private static final float SPEED = 300;

    private static final float ANIMATION_SPEED = 0.5f;
    private static final int PLAYER_PIXEL_WIDTH = 48;
    private static final int PLAYER_PIXEL_HEIGHT = 48;
    private static final int SIZE_MULTIPLIER = 3;
    private static final int PLAYER_WIDTH = PLAYER_PIXEL_WIDTH*SIZE_MULTIPLIER;
    private static final int PLAYER_HEIGHT = PLAYER_PIXEL_HEIGHT*SIZE_MULTIPLIER;
    private static final int TRANSPARENT_PIXEL = 16 * SIZE_MULTIPLIER;
    private static final float SWITCH_STATE_DELAY = 0.1f;
    private static final float SWITCH_STATE_JUMP_DELAY = 0.18f;
    private static final float JUMP_SPEED = SPEED*2.5f;
    private static final float SWITCH_STATE_ATTACK_DELAY = 0.5f;

    private float x, y, yBeforeJump;
    private float stateTime = 0;

    private int currentState;
    private int currentDirection;

    private Animation[][] playerAnimation;

    private enum playerState{IDLE, WALK, ATTACK}
    private enum direction{DOWN,UP,LEFT,RIGHT}
    private boolean isMoving = false;
    private boolean isJumping = false;
    private boolean isAttacking = false;
    private float attackTimer;
    private float jumpTimer;
    private float stateTimer;

    boolean isPlayer1;

    public Player(boolean isPlayer1){
        loadAnimation();
        loadPlayerConfig();
        this.isPlayer1 = isPlayer1;
    }

    private void loadPlayerConfig(){
        currentState = playerState.IDLE.ordinal();
        currentDirection = direction.UP.ordinal();

        x = 0 - TRANSPARENT_PIXEL;
        y = 0 - TRANSPARENT_PIXEL;
        stateTimer = 0;
        jumpTimer = 0;
        attackTimer = 0;
    }

    private void loadAnimation(){
        //Declaring Player Animation
        playerAnimation = new Animation[playerState.values().length][];
        playerAnimation[playerState.IDLE.ordinal()] = new Animation[direction.values().length];
        playerAnimation[playerState.WALK.ordinal()] = new Animation[direction.values().length];
        playerAnimation[playerState.ATTACK.ordinal()] = new Animation[direction.values().length];

        TextureRegion[][] playerIdleSpriteSheet= TextureRegion.split(new Texture("Characters/Basic Character Spritesheet Idle.png"),PLAYER_PIXEL_WIDTH,PLAYER_PIXEL_HEIGHT);
        TextureRegion[][] playerWalkSpriteSheet= TextureRegion.split(new Texture("Characters/Basic Character Spritesheet Walk.png"),PLAYER_PIXEL_WIDTH,PLAYER_PIXEL_HEIGHT);
        TextureRegion[][] playerAttackSpriteSheet= TextureRegion.split(new Texture("Characters/Basic Character Actions.png"),PLAYER_PIXEL_WIDTH,PLAYER_PIXEL_HEIGHT);

        for (direction _direction : direction.values()) {
            playerAnimation[playerState.IDLE.ordinal()][_direction.ordinal()] = new Animation(ANIMATION_SPEED, playerIdleSpriteSheet[_direction.ordinal()]);
            playerAnimation[playerState.WALK.ordinal()][_direction.ordinal()] = new Animation(ANIMATION_SPEED, playerWalkSpriteSheet[_direction.ordinal()]);
            playerAnimation[playerState.ATTACK.ordinal()][_direction.ordinal()] = new Animation(ANIMATION_SPEED, playerAttackSpriteSheet[_direction.ordinal()]);
        }
    }

    private void player1Control(){
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

        else if(isAttacking){
            if(attackTimer >= SWITCH_STATE_ATTACK_DELAY){
                currentState = playerState.IDLE.ordinal();
                //System.out.println("SERANG");
                isAttacking = false;
                stateTime = 0;
                attackTimer = 0;
            }
            attackTimer += Gdx.graphics.getDeltaTime();
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
                if (y+(SPEED*Gdx.graphics.getDeltaTime()) < Atomic.HEIGHT - PLAYER_HEIGHT + TRANSPARENT_PIXEL) {
                    y += SPEED * Gdx.graphics.getDeltaTime();
                } else{
                    y = Atomic.HEIGHT - PLAYER_HEIGHT + TRANSPARENT_PIXEL;
                }

                isMoving = true;
                currentDirection = direction.UP.ordinal();
                stateTimer = 0;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                if (y-(SPEED*Gdx.graphics.getDeltaTime()) > 0 - TRANSPARENT_PIXEL) {
                    y -= SPEED * Gdx.graphics.getDeltaTime();
                } else{
                    y = 0 - TRANSPARENT_PIXEL;
                }

                isMoving = true;
                currentDirection = direction.DOWN.ordinal();
                stateTimer = 0;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                if (x+(SPEED*Gdx.graphics.getDeltaTime()) < Atomic.WIDTH - PLAYER_WIDTH + TRANSPARENT_PIXEL) {
                    x += SPEED * Gdx.graphics.getDeltaTime();
                } else{
                    x = Atomic.WIDTH - PLAYER_WIDTH + TRANSPARENT_PIXEL;
                }

                isMoving = true;
                currentDirection = direction.RIGHT.ordinal();
                stateTimer = 0;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                if (x-(SPEED*Gdx.graphics.getDeltaTime()) > 0 - TRANSPARENT_PIXEL) {
                    x -= SPEED * Gdx.graphics.getDeltaTime();
                } else{
                    x = 0 - TRANSPARENT_PIXEL;
                }

                isMoving = true;
                currentDirection = direction.LEFT.ordinal();
                stateTimer = 0;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
                yBeforeJump = y;
                isJumping = true;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.K)){
                stateTime = 0;
                isAttacking = true;
            }
        }

        if (isAttacking){
            currentState = playerState.ATTACK.ordinal();
        }
        else if (isMoving){
            currentState = playerState.WALK.ordinal();
        }
        else{
            currentState = playerState.IDLE.ordinal();
        }

        stateTime += Gdx.graphics.getDeltaTime();
        System.out.println(currentState);
    }

    private void player2Control(){
        //
    }

    public void action(){
        if(isPlayer1){
            player1Control();
        }
        else{
            player2Control();
        }
    }

    public TextureRegion getPlayerKeyFrame(){
        if(isAttacking){
            return (TextureRegion) playerAnimation[currentState][currentDirection].getKeyFrame(stateTime, false);
        }
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

package de.hochschuletrier.gdw.ss15.game;

public class GameConstants {

    // Priorities for entity systems
    public static final int PRIORITY_PHYSIX = 0;
    public static final int PRIORITY_ENTITIES = 10;
    public static final int PRIORITY_CAMERA = 20;
    public static final int PRIORITY_MAP = 21;
    public static final int PRIORITY_ANIMATIONS = 30;
    public static final int PRIORITY_DEBUG_WORLD = 40;
    public static final int PRIORITY_HUD = 50;
    public static final int PRIORITY_REMOVE_ENTITIES = 1000;

    // PooledEngine parameters
    public static final int ENTITY_POOL_INITIAL_SIZE = 32;
    public static final int ENTITY_POOL_MAX_SIZE = 256;
    public static final int COMPONENT_POOL_INITIAL_SIZE = 32;
    public static final int COMPONENT_POOL_MAX_SIZE = 256;

    // Physix parameters
    public static final int POSITION_ITERATIONS = 3;
    public static final int VELOCITY_ITERATIONS = 8;
    public static final int BOX2D_SCALE = 40;
    
    public static float MUSIC_FADE_TIME = 2;
    public static int MAX_PLAYERS = 8;
    public static int SCORE_TO_WIN = 10;
    public static int GAME_TIME = 60 * 5;
    public static int GAME_OVER_TIME = 30;
    
    // Light parameters
    public static final int LIGHT_RAYS = 90;
    public static final float LIGHT_AMBIENT = 0.4f;
    public static final boolean LIGHT_BLUR = true;
    public static final int LIGHT_BLURNUM = 2;
    public static final boolean LIGHT_SHADOW = true;
    public static final boolean LIGHT_DIFFUSE = false;
    
    public static short MASK_EVERYTHING = 0xFFF;
    public static short MASK_WORLDOBJECT = 0x002;
    public static short MASK_WORLDSENSOR = 0x004;
    
    public static final boolean LIGHTS = true;
    
    //times
    public static final float NOT_STAUNNABLE_TIME=5f;//0.75f;
    public static final float STUNN_TIME=2f;
}

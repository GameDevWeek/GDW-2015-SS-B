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
}

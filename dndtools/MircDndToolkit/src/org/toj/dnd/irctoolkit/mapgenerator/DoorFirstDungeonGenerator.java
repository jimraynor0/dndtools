package org.toj.dnd.irctoolkit.mapgenerator;

import java.awt.Point;

import org.apache.log4j.Logger;
import org.toj.dnd.irctoolkit.configs.MapGenConfigs;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.util.RandomUtil;

public class DoorFirstDungeonGenerator {

    private static final String INITIAL_ROOM_PLACING_FAILED = "initial room placing failed.";
    private static final int MAX_ATTEMPTS = 1000;
    private MapGenConfigs config;
    private Logger log = Logger.getLogger(this.getClass());
    private int roomWidthMin;
    private int roomWidthMax;
    private int doorWidthMin;
    private int doorWidthMax;

    public DoorFirstDungeonGenerator() {
        config = new MapGenConfigs();
        roomWidthMin = config.getInt(MapGenConfigs.CONF_ROOM_WIDTH_MIN);
        roomWidthMax = config.getInt(MapGenConfigs.CONF_ROOM_WIDTH_MAX);
        doorWidthMin = Math.max(1,
                config.getInt(MapGenConfigs.CONF_DOOR_WIDTH_MIN));
        doorWidthMax = Math.min(roomWidthMin,
                config.getInt(MapGenConfigs.CONF_DOOR_WIDTH_MAX));
    }

    public Dungeon generateDungeon() {
        int width = Integer.parseInt(config.get(MapGenConfigs.CONF_MAP_WIDTH));
        int height = Integer
                .parseInt(config.get(MapGenConfigs.CONF_MAP_HEIGHT));
        int roomCount = Integer.parseInt(config
                .get(MapGenConfigs.CONF_ROOM_COUNT));
        Dungeon dungeon = new Dungeon(width, height);
        int roomCreated = 0;
        int failCount = 0;
        while (roomCreated < roomCount) {
            try {
                if (createRoom(dungeon)) {
                    roomCreated++;
                    failCount = 0;
                } else {
                    failCount++;
                    if (failCount > MAX_ATTEMPTS) {
                        ToolkitEngine.getEngine().fireErrorMsgWindow(
                                "地图太小啦，房间放不下啦！请调整参数！");
                        return null;
                    }
                }
            } catch (IllegalArgumentException e) {
                if (e.getMessage().equals(INITIAL_ROOM_PLACING_FAILED)) {
                    return null;
                }
            }
        }
        return dungeon;
    }

    private boolean createRoom(Dungeon dungeon) {
        if (dungeon.getRooms().isEmpty()) {
            createInitialRoom(dungeon, roomWidthMin, roomWidthMax);
        } else {
            Room neibour = dungeon.pickRandomRoom();
            String side = RandomUtil.pickSide();
            log.debug("Randomly picked neibour: " + neibour);
            log.debug("Randomly picked side: " + side);

            Door door = openDoor(neibour, side);
            log.debug("Door Opened: " + door);

            Room room = new Room();
            room.setWidth(RandomUtil.random(
                    Math.max(roomWidthMin, door.getWidth()), roomWidthMax));
            room.setHeight(RandomUtil.random(
                    Math.max(roomWidthMin, door.getWidth()), roomWidthMax));
            connectThroughDoor(room, door, side);

            if (!dungeon.canHave(room)) {
                log.debug("room cannot be placed, abandened");
                return false;
            }

            log.debug("door created: " + door);
            log.debug("room created: " + room);
            dungeon.getDoors().add(door);
            dungeon.addRoom(room);
        }
        return true;
    }

    private void connectThroughDoor(Room room, Door door, String side) {
        if ("North".equals(side)) {
            room.setPosX(RandomUtil.random(door.getEnd().x - room.getWidth()
                    + 1, door.getStart().x + 1));
            room.setPosY(door.getStart().y - room.getHeight());
        }
        if ("East".equals(side)) {
            room.setPosX(door.getStart().x + 1);
            room.setPosY(RandomUtil.random(door.getEnd().y - room.getHeight()
                    + 1, door.getStart().y + 1));
        }
        if ("South".equals(side)) {
            room.setPosX(RandomUtil.random(door.getEnd().x - room.getWidth()
                    + 1, door.getStart().x + 1));
            room.setPosY(door.getStart().y + 1);
        }
        if ("West".equals(side)) {
            room.setPosX(door.getStart().x - room.getWidth());
            room.setPosY(RandomUtil.random(door.getEnd().y - room.getHeight()
                    + 1, door.getStart().y + 1));
        }
    }

    private Door openDoor(Room room, String side) {
        Door door = new Door();
        int doorWidth = RandomUtil.random(doorWidthMin, doorWidthMax + 1);
        if ("North".equals(side)) {
            int y = room.getPosY() - 1;
            int xInit = RandomUtil.random(room.getPosX(),
                    room.getPosX() + room.getWidth() - doorWidth);
            for (int i = 0; i < doorWidth; i++) {
                door.getSquares().add(new Point(xInit + i, y));
            }
        }
        if ("East".equals(side)) {
            int x = room.getPosX() + room.getWidth();
            int yInit = RandomUtil.random(room.getPosY(),
                    room.getPosX() + room.getHeight() - doorWidth);
            for (int i = 0; i < doorWidth; i++) {
                door.getSquares().add(new Point(x, yInit + i));
            }
        }
        if ("South".equals(side)) {
            int y = room.getPosY() + room.getHeight();
            int xInit = RandomUtil.random(room.getPosX(),
                    room.getPosX() + room.getWidth() - doorWidth);
            for (int i = 0; i < doorWidth; i++) {
                door.getSquares().add(new Point(xInit + i, y));
            }
        }
        if ("West".equals(side)) {
            int x = room.getPosX() - 1;
            int yInit = RandomUtil.random(room.getPosY(),
                    room.getPosX() + room.getHeight() - doorWidth);
            for (int i = 0; i < doorWidth; i++) {
                door.getSquares().add(new Point(x, yInit + i));
            }
        }

        return door;
    }

    private void createInitialRoom(Dungeon dungeon, int roomWidthMin,
            int roomWidthMax) {
        Room room = new Room();
        room.setWidth(RandomUtil.random(roomWidthMin, roomWidthMax));
        room.setHeight(RandomUtil.random(roomWidthMin, roomWidthMax));
        room.setPosX((dungeon.getWidth() - room.getWidth()) / 2);
        room.setPosY((dungeon.getHeight() - room.getHeight()) / 2);
        if (!dungeon.canHave(room)) {
            ToolkitEngine.getEngine().fireErrorMsgWindow("房间比地图还大啦！请调整参数！");
            throw new IllegalArgumentException(INITIAL_ROOM_PLACING_FAILED);
        }
        log.debug("initial room created: " + room);
        dungeon.addRoom(room);
    }
}

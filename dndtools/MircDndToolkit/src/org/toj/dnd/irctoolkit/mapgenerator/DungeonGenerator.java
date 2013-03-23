package org.toj.dnd.irctoolkit.mapgenerator;

import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.toj.dnd.irctoolkit.configs.MapGenConfigs;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.util.RandomUtil;

public class DungeonGenerator {

    private static final String INITIAL_ROOM_PLACING_FAILED = "initial room placing failed.";
    private static final int MAX_ATTEMPTS = 1000;
    private MapGenConfigs config;
    private Logger log = Logger.getLogger(this.getClass());

    public DungeonGenerator() {
        config = new MapGenConfigs();
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
        int roomWidthMin = Integer.parseInt(config
                .get(MapGenConfigs.CONF_ROOM_WIDTH_MIN));
        int roomWidthMax = Integer.parseInt(config
                .get(MapGenConfigs.CONF_ROOM_WIDTH_MAX)) + 1;

        Room room = new Room();
        room.setWidth(RandomUtil.random(roomWidthMin, roomWidthMax));
        room.setHeight(RandomUtil.random(roomWidthMin, roomWidthMax));
        if (dungeon.getRooms().isEmpty()) {
            room.setPosX((dungeon.getWidth() - room.getWidth()) / 2);
            room.setPosY((dungeon.getHeight() - room.getHeight()) / 2);
            if (!dungeon.canHave(room)) {
                ToolkitEngine.getEngine().fireErrorMsgWindow("房间比地图还大啦！请调整参数！");
                throw new IllegalArgumentException(INITIAL_ROOM_PLACING_FAILED);
            }
        } else {
            Point door = placeRoom(room, dungeon);
            if (!dungeon.canHave(room)) {
                log.debug("room cannot be placed, abandened");
                return false;
            }
            Door oneSqDoor = new Door();
            oneSqDoor.getSquares().add(door);
            dungeon.getDoors().add(oneSqDoor);
            log.debug("door created: " + door);
        }

        log.debug("room created: " + room);
        dungeon.addRoom(room);
        return true;
    }

    private Point placeRoom(Room room, Dungeon dungeon) {
        Room neibour = dungeon.pickRandomRoom();
        log.debug("Randomly picked neibour: " + neibour);
        String side = RandomUtil.pickSide();
        log.debug("Randomly picked side: " + side);
        if ("North".equals(side)) {
            room.setPosX(RandomUtil.random(neibour.getPosX() - room.getWidth()
                    + 1, neibour.getPosX() + neibour.getWidth()));
            room.setPosY(neibour.getPosY() - room.getHeight() - 1);
        }
        if ("East".equals(side)) {
            room.setPosX(neibour.getPosX() + neibour.getWidth() + 1);
            room.setPosY(RandomUtil.random(neibour.getPosY() - room.getHeight()
                    + 1, neibour.getPosY() + neibour.getHeight()));
        }
        if ("South".equals(side)) {
            room.setPosX(RandomUtil.random(neibour.getPosX() - room.getWidth()
                    + 1, neibour.getPosX() + neibour.getWidth()));
            room.setPosY(neibour.getPosY() + neibour.getHeight() + 1);
        }
        if ("West".equals(side)) {
            room.setPosX(neibour.getPosX() - room.getWidth() - 1);
            room.setPosY(RandomUtil.random(neibour.getPosY() - room.getHeight()
                    + 1, neibour.getPosY() + neibour.getHeight()));
        }
        return placeDoor(side, room, neibour);
    }

    private Point placeDoor(String side, Room room, Room neibour) {
        Point door = new Point();
        if ("North".equals(side)) {
            List<Integer> keypoints = Arrays.asList(room.getPosX(),
                    room.getPosX() + room.getWidth() - 1, neibour.getPosX(),
                    neibour.getPosX() + neibour.getWidth() - 1);
            Collections.sort(keypoints);
            door.x = RandomUtil.random(keypoints.get(1), keypoints.get(2) + 1);
            door.y = room.getPosY() + room.getHeight();
        }
        if ("East".equals(side)) {
            door.x = neibour.getPosX() + neibour.getWidth();
            List<Integer> keypoints = Arrays.asList(room.getPosY(),
                    room.getPosY() + room.getHeight() - 1, neibour.getPosY(),
                    neibour.getPosY() + neibour.getHeight() - 1);
            Collections.sort(keypoints);
            door.y = RandomUtil.random(keypoints.get(1), keypoints.get(2) + 1);
        }
        if ("South".equals(side)) {
            List<Integer> keypoints = Arrays.asList(room.getPosX(),
                    room.getPosX() + room.getWidth() - 1, neibour.getPosX(),
                    neibour.getPosX() + neibour.getWidth() - 1);
            Collections.sort(keypoints);
            door.x = RandomUtil.random(keypoints.get(1), keypoints.get(2) + 1);
            door.y = neibour.getPosY() + neibour.getHeight();
        }
        if ("West".equals(side)) {
            door.x = room.getPosX() + room.getWidth();
            List<Integer> keypoints = Arrays.asList(room.getPosY(),
                    room.getPosY() + room.getHeight() - 1, neibour.getPosY(),
                    neibour.getPosY() + neibour.getHeight() - 1);
            Collections.sort(keypoints);
            door.y = RandomUtil.random(keypoints.get(1), keypoints.get(2) + 1);
        }
        return door;
    }
}

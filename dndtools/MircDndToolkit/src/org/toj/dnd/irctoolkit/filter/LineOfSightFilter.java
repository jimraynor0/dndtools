package org.toj.dnd.irctoolkit.filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.toj.dnd.irctoolkit.map.MapGridCell;
import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.util.AxisUtil;

public class LineOfSightFilter extends MapFilter {
    private static final MapGridCell OBSCURED_SPACE = new MapGridCell(
            Color.GRAY, Color.WHITE, "Îí");

    private static final long serialVersionUID = 7390606623325169731L;

    private static Logger log = Logger.getLogger(LineOfSightFilter.class);

    public static final int MASK_TYPE_LINE_OF_EFFECT = 0;
    public static final int MASK_TYPE_LINE_OF_SIGHT = 100;

    private transient MapGridCell[][] originalMap;
    private transient MapGridCell[][] maskedMap;
    private List<ViewPoint> viewpoints;
    private int maskType;

    public LineOfSightFilter(ViewPoint viewpoint, int maskType) {
        List<ViewPoint> viewpoints = new ArrayList<ViewPoint>(1);
        viewpoints.add(viewpoint);
        this.maskType = maskType;
    }

    public LineOfSightFilter(List<ViewPoint> viewpoints, int maskType) {
        this.maskType = maskType;
        this.viewpoints = viewpoints;
    }

    private List<ViewPoint> initViewPoints(List<ViewPoint> viewpoints) {
        List<ViewPoint> processed = new ArrayList<ViewPoint>();
        for (ViewPoint vp : viewpoints) {
            if (vp.getObj() != null) {
                processed.addAll(searchForVp(vp));
            } else {
                processed.add(vp);
            }
        }
        return processed;
    }

    private Set<ViewPoint> searchForVp(ViewPoint vp) {
        Set<ViewPoint> vps = new HashSet<ViewPoint>();
        for (int i = 0; i < originalMap.length; i++) {
            for (int j = 0; j < originalMap[0].length; j++) {
                if (vp.getObj().equals(originalMap[i][j].getCh())) {
                    ViewPoint p = new ViewPoint(i, j);
                    p.setRange(vp.getRange());
                    vps.add(p);
                }
            }
        }
        return vps;
    }

    private void initMaskedMap() {
        this.maskedMap = new MapGridCell[originalMap.length][originalMap[0].length];
        for (int i = 0; i < originalMap.length; i++) {
            for (int j = 0; j < originalMap[0].length; j++) {
                maskedMap[i][j] = OBSCURED_SPACE;
            }
        }
    }

    public MapGridCell[][] doApplyFilter(MapGridCell[][] map) {
        this.originalMap = map;
        initMaskedMap();
        List<ViewPoint> vps = initViewPoints(viewpoints);

        for (ViewPoint point : vps) {
            resolveVisibilityAtDistance(0, point);
        }
        return maskedMap;
    }

    private void resolveVisibilityAtDistance(int d, ViewPoint p) {
        if (p.getRange() > 0 && p.getRange() < d) {
            return;
        }
        if (p.getX() - d < 0 && p.getX() + d > originalMap.length
                && p.getY() - d < 0 && p.getY() + d > originalMap[0].length) {
            return;
        }

        boolean blockedInAllDirection = true;
        for (int x = p.getX() - d; x <= p.getX() + d; x++) {
            if (x < 0 || x >= originalMap.length) {
                continue;
            }
            for (int y = p.getY() - d; y <= p.getY() + d; y++) {
                if (y < 0 || y >= originalMap[0].length) {
                    continue;
                }

                double distance = Math.sqrt(Math.pow(x - p.getX(), 2)
                        + Math.pow(y - p.getY(), 2));
                if (distance > d + 1 - p.getOneAndHalfSquareSlope()
                        || distance < d - p.getOneAndHalfSquareSlope()) {
                    continue;
                }

                // if (x != p.getX() - d && x != p.getX() + d && y != p.getY() -
                // d
                // && y != p.getY() + d) {
                // continue;
                // }
                if (resolveVisibilityBetween(p.getX(), p.getY(), x, y)) {
                    blockedInAllDirection = false;
                }
            }
        }
        if (!blockedInAllDirection) {
            resolveVisibilityAtDistance(d + 1, p);
        }
    }

    private boolean resolveVisibilityBetween(int xStart, int yStart, int xEnd,
            int yEnd) {
        log("entering resolveVisibilityBetween(xStart=%s, yStart=%s, xEnd=%s, yEnd=%s)",
                xStart, yStart, xEnd, yEnd);

        double slope = Double.POSITIVE_INFINITY;
        if (yStart != yEnd) {
            slope = ((double) xStart - xEnd) / ((double) yStart - yEnd);
        }
        log("slope calculated: %s", slope);
        boolean blocked = false;
        boolean blockedAtEndingPoint = false;

        log("Math.abs(slope) <= 1? %s", Math.abs(slope) <= 1);
        if (Math.abs(slope) <= 1) {
            log("entering j loop");
            for (int j = 0; j <= (int) Math.abs(yEnd - yStart); j++) {
                log("j = %s", j);
                double iExact = (slope == Double.POSITIVE_INFINITY) ? 0 : j
                        * slope;
                log("iExact = %s", iExact);
                if (Math.abs(iExact) - Math.floor(Math.abs(iExact)) == 0.5) {
                    // check both
                    int i = (int) Math.floor(Math.abs(iExact));
                    log("i = %s", i);
                    int x = xEnd > xStart ? xStart + i : xStart - i;
                    log("x = %s", x);
                    int xAdj = xEnd > xStart ? x + 1 : x - 1;
                    log("xAdj = %s", xAdj);
                    int y = yEnd > yStart ? yStart + j : yStart - j;
                    log("y = %s", y);
                    log("isBlockedAt(x, y)? %s", isBlockedAt(x, y));
                    log("isBlockedAt(xAdj, y)? %s", isBlockedAt(xAdj, y));
                    if (isBlockedAt(x, y) && isBlockedAt(xAdj, y)) {
                        log("blocked set to true");
                        blocked = true;
                        break;
                    }
                } else {
                    int i = (int) Math.round(Math.abs(iExact));
                    log("i = %s", i);
                    int x = xEnd > xStart ? xStart + i : xStart - i;
                    log("x = %s", x);
                    int y = yEnd > yStart ? yStart + j : yStart - j;
                    log("y = %s", y);
                    log("isBlockedAt(x, y)? %s", isBlockedAt(x, y));
                    if (isBlockedAt(x, y)) {
                        log("blocked set to true");
                        blocked = true;
                        if (j == (int) Math.abs(yEnd - yStart)) {
                            log("blockedAtEndingPoint set to true");
                            blockedAtEndingPoint = true;
                        }
                        break;
                    }
                }
            }
        } else {
            log("entering i loop");
            for (int i = 0; i <= (int) Math.abs(xEnd - xStart); i++) {
                log("i = %s", i);
                double jExact = (slope == Double.POSITIVE_INFINITY) ? 0 : i
                        / slope;
                log("jExact = %s", jExact);
                if (Math.abs(jExact) - Math.floor(Math.abs(jExact)) == 0.5) {
                    // check both
                    int j = (int) Math.floor(Math.abs(jExact));
                    log("j = %s", j);
                    int x = xEnd > xStart ? xStart + i : xStart - i;
                    log("x = %s", x);
                    int y = yEnd > yStart ? yStart + j : yStart - j;
                    log("y = %s", y);
                    int yAdj = yEnd > yStart ? y + 1 : y - 1;
                    log("yAdj = %s", yAdj);
                    log("isBlockedAt(x, y)? %s", isBlockedAt(x, y));
                    log("isBlockedAt(x, yAdj)? %s", isBlockedAt(x, yAdj));
                    if (isBlockedAt(x, y) && isBlockedAt(x, yAdj)) {
                        log("blocked set to true");
                        blocked = true;
                        break;
                    }
                } else {
                    int j = (int) Math.round(Math.abs(jExact));
                    log("j = %s", j);
                    int x = xEnd > xStart ? xStart + i : xStart - i;
                    log("x = %s", x);
                    int y = yEnd > yStart ? yStart + j : yStart - j;
                    log("y = %s", y);
                    log("isBlockedAt(x, y)? %s", isBlockedAt(x, y));
                    if (isBlockedAt(x, y)) {
                        log("isBlockedAt(x, y)? %s", isBlockedAt(x, y));
                        blocked = true;
                        if (i == (int) Math.abs(xEnd - xStart)) {
                            log("blockedAtEndingPoint set to true");
                            blockedAtEndingPoint = true;
                        }
                        break;
                    }
                }
            }
        }

        if (!blocked || blockedAtEndingPoint) {
            maskedMap[xEnd][yEnd] = originalMap[xEnd][yEnd];
            // for (ViewPoint vp : viewpoints) {
            // if (vp.getX() == xEnd && vp.getY() == yEnd) {
            // maskedMap.getGrid()[xEnd][yEnd].setBackground(Color.TEAL);
            // }
            // }
        }

        return !blocked;
    }

    private void log(String str, Object... data) {
        if (data != null && data.length > 0) {
            for (Object d : data) {
                str = str.replaceFirst("\\%s", String.valueOf(d));
            }
        }
        log.debug(str);
    }

    private boolean isBlockedAt(int i, int j) {
        if (this.maskType == MASK_TYPE_LINE_OF_SIGHT) {
            return originalMap[i][j].isBlockLineOfSight();
        } else {
            return originalMap[i][j].isBlockLineOfEffect();
        }
    }

    @Override
    public String getType() {
        return "¿ÉÊÓ·¶Î§ÂË¾µ";
    }

    @Override
    public String getParams() {
        StringBuilder sb = new StringBuilder();
        for (ViewPoint vp : this.viewpoints) {
            if (vp != viewpoints.get(0)) {
                sb.append(",");
            }
            if (vp.getObj() == null) {
                sb.append(AxisUtil.toString("x", vp.getX())).append(
                        AxisUtil.toString("y", vp.getY()));
            } else {
                sb.append(vp.getObj());
            }
            if (vp.getRange() > 0 && vp.getRange() < 100) {
                sb.append("|").append(vp.getRange());
            }
        }
        return sb.toString();
    }
}

package com.schemafactor.rogueserver.common;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Queue;


public class PathFinding 
{
    private static final boolean DEBUG = false;

    public Point[] findPath(final int[][] map,
                            final Point position,
                            final Point destination) 
    {
        if (isOutOfMap(map, position.x, position.y)
                || isOutOfMap(map, destination.x, destination.y)
                || isBlocked(map, position.x, position.y)
                || isBlocked(map, destination.x, destination.y)) {
            return null;
        }

        Queue<Point> queue1 = new ArrayDeque<>();
        Queue<Point> queue2 = new ArrayDeque<>();

        queue1.add(position);

        map[position.y][position.x] = -1;

        for (int i = 2; !queue1.isEmpty(); i++) {
            if (queue1.size() >= map.length * map[0].length) {
                throw new IllegalStateException("Map overload");
            }

            for (Point point : queue1) {
                if (point.x == destination.x && point.y == destination.y) {
                    return arrived(map, i - 1, point);
                }

                final Queue<Point> finalQueue = queue2;
                final int finalStepCount = i;

                lookAround(map, point, (x, y) -> {
                    if (isBlocked(map, x, y)) {
                        return;
                    }

                    Point e = new Point(x, y);

                    finalQueue.add(e);

                    map[e.y][e.x] = -finalStepCount;
                });
            }

            if (DEBUG) {
                printMap(map);
            }

            queue1 = queue2;
            queue2 = new ArrayDeque<>();
        }

        resetMap(map);

        return null;
    }

    private static boolean isOutOfMap(final int[][] map,
                                      final int x,
                                      final int y) {
        return x < 0 || y < 0 || map.length <= y || map[0].length <= x;
    }

    private boolean isBlocked(final int[][] map, final int x, final int y) {
        final int i = map[y][x];
        return i < 0 || i == 1;
    }

    private Point[] arrived(final int[][] map, final int size, final Point p) {
        final Point[] optimalPath = new Point[size];

        computeSolution(map, p.x, p.y, size, optimalPath);

        resetMap(map);

        return optimalPath;
    }

    private void resetMap(final int[][] map) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] < 0) {
                    map[y][x] = 0;
                }
            }
        }
    }

    private void printMap(final int[][] map) {
        for (final int[] r : map) {
            for (final int i : r) {
                System.out.print(i + "\t");
            }

            System.out.println();
        }

        System.out.println("****************************************");
    }

    private void computeSolution(final int[][] map,
                                 final int x,
                                 final int y,
                                 final int stepCount,
                                 final Point[] optimalPath) {
        if (isOutOfMap(map, x, y)
                || map[y][x] == 0
                || map[y][x] != -stepCount) {
            return;
        }

        final Point p = new Point(x, y);

        optimalPath[stepCount - 1] = p;

        lookAround(map, p, (x1, y1) -> computeSolution(map, x1, y1, stepCount - 1, optimalPath));
    }

    private void lookAround(final int[][] map,
                            final Point p,
                            final Callback callback) {
        callback.look(map, p.x + 1, p.y + 1);
        callback.look(map, p.x - 1, p.y + 1);
        callback.look(map, p.x - 1, p.y - 1);
        callback.look(map, p.x + 1, p.y - 1);
        callback.look(map, p.x + 1, p.y);
        callback.look(map, p.x - 1, p.y);
        callback.look(map, p.x, p.y + 1);
        callback.look(map, p.x, p.y - 1);
    }

    private interface Callback {
        default void look(final int[][] map, final int x, final int y) {
            if (isOutOfMap(map, x, y)) {
                return;
            }
            onLook(x, y);
        }

        void onLook(int x, int y);
    }
}

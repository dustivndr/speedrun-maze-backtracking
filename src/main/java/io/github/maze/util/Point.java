package io.github.maze.util;

import java.util.Objects;

import java.util.Objects;

public final class Point {

    private final int row;
    private final int col;

    public Point(Point point) {
        row = point.row();
        col = point.col();
    }

    public Point(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int row() {
        return row;
    }

    public int col() {
        return col;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Point other)) {
            return false;
        }

        return row == other.row && col == other.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}
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

    /**
     * titik koordinat baris dan kolom di map
     *
     * @param row koordinat baris mulai dari 0
     * @param col koordinat kolom mulai dari 0
     */
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

    /**
     * @param obj object yang dibandingin sama objek this
     * @return true klo sama, false klo beda koordinat
     */
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

    /**
     *
     * @return value yang di koordinat yang di hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    /**
     * nge format print class Point
     * @return
     */
    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}
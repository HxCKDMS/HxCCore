package hxckdms.hxccore.configs;

import java.util.LinkedHashMap;

public class FakePlayerData {
    public LinkedHashMap<String, Warp> homes = new LinkedHashMap<>();
    public FakePlayerData() {}
    public static class Warp {
        public double xpos, ypos, zpos;
        public int dimension;
        public Warp() {}
        public Warp(double X, double Y, double Z, int D) {
            this.xpos = X;
            this.ypos = Y;
            this.zpos = Z;
            this.dimension = D;
        }
    }
}

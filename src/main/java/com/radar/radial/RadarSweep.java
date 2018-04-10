package com.radar.radial;

import com.radar.Data4Json;

public class RadarSweep implements Data4Json {
    public float[][] m_data;
    public float[] azimuth;
    public int gNum;
    public float gSize;//每个gate的长度
    public float gRange;
    public int m_nRows;
    public int m_nCols;
    public float m_llx = 0;
    public float m_lly = 0;


    public RadarSweep(float[][] m_data, float[] azimuth, int gNum, float gSize, float gRange) {
        this.m_data = m_data;
        this.azimuth = azimuth;
        this.gNum = gNum;
        this.gSize = gSize;
        this.gRange = gRange;

        this.m_nRows = gNum*2;
        this.m_nCols = gNum*2;
    }
}

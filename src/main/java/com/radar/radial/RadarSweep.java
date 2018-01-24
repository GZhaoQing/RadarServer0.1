package com.radar.radial;

import com.radar.Data4Json;

public class RadarSweep implements Data4Json {
    public float[] m_data;
    public float[] azimuth;
    public int gNum;

    public RadarSweep(float[] m_data, float[] azimuth, int gNum) {
        this.m_data = m_data;
        this.azimuth = azimuth;
        this.gNum = gNum;
    }
}

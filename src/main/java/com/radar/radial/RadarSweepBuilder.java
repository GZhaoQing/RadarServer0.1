package com.radar.radial;

import ucar.nc2.dt.RadialDatasetSweep;

import java.io.IOException;

public class RadarSweepBuilder {
    public RadarSweep build(RadialDatasetSweep.RadialVariable varRef) throws IOException {
        float[] data = varRef.readAllData();
        RadialDatasetSweep.Sweep sweep=varRef.getSweep(0);
        float[] azimuth=sweep.getAzimuth();
        int gNum=sweep.getGateNumber();
        return new RadarSweep(data,azimuth,gNum);
    }
}

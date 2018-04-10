package com.radar.radial;

import ucar.nc2.dt.RadialDatasetSweep;

import java.io.IOException;

public class RadarSweepBuilder {
    public RadarSweep build(RadialDatasetSweep.RadialVariable varRef) throws IOException {
        float[] rawData = varRef.readAllData();
        RadialDatasetSweep.Sweep sweep=varRef.getSweep(0);
        float[] azimuth=sweep.getAzimuth();
        int gNum=sweep.getGateNumber();

        float[][] data = new float[gNum*2][gNum*2];
        int offX;
        int offY;
        float rv;
        for(int i=0;i<azimuth.length;i++) {
//            System.out.println(azimuth[i]);
            for (int j = 0; j < gNum; j++) {
                rv = rawData[i * gNum + j];

//                System.out.println(v);
                offX = (int) (gNum + Math.cos((azimuth[i] - 90) / 180 * Math.PI) * j);
                offY = (int) (gNum + Math.sin((azimuth[i] - 90) / 180 * Math.PI) * j);
                data[offX][offY] = rv;
            }
        }

        return new RadarSweep(data,azimuth,gNum,sweep.getGateSize(),sweep.getRangeToFirstGate());
    }
}

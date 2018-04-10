package com.radar.grid;

import ucar.ma2.Array;
import ucar.nc2.Dimension;
import ucar.nc2.Variable;
import ucar.nc2.constants.AxisType;
import ucar.nc2.dataset.CoordinateAxis;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/*
*设计思路：接收一个存储着数据源NetcdfDataset对象ncds
*1. 通过FeatureDatasetFactoryManager将ncds读取到FeatureDataset对象fds，对fds操作，获得存储的影像数据(Array)和对应的Dimensions列表;
*2. 回到ncds，通过Dimension名称从ncds获取到CoordinateAxis对象，判断其类型AxisType，筛选两个表示平面的维度（x，y）;
*3. 从CoordinateAxis可以获取坐标数据的最大值和最小值，结合shape信息表示的数据数量，可以计算出RasterGrid2_Byte对象中的栅格单元边长；最小值作为数据起始位置和;
*4. 通过判断出的x,y维度对应的Length，将数据从Array存储到二维数据。
*/
public class RasterGridBuilder {
    public RasterGrid2_Byte build(Variable var,List<CoordinateAxis> axisList) throws IOException {
        float llx = 0;
        float lly = 0 ;
        float cellSize = -1 ;
        int nRows = -1 ;
        int nCols = -1;
        Byte[][] curData = null;
        float maxX = 0;
        float maxY = 0;


        //read data
        Array array =var.read();
        List<Dimension> dList=var.getDimensions();


        //find dimensions of x y
        Iterator it=dList.iterator();
        Dimension d=null;
        while(it.hasNext()){
            d=(Dimension) it.next();
            for(CoordinateAxis axis:axisList){
                if(d.getFullName().equals(axis.getFullName())){
                    AxisType type=axis.getAxisType();
                    if(nRows==-1 && type==AxisType.GeoX){
                        nRows=d.getLength();
                        llx=(float) axis.getMinValue();
                        maxX=(float) axis.getMaxValue();
                        break;
                    }else if(nCols==-1 && type==AxisType.GeoY){
                        nCols=d.getLength();
                        lly=(float) axis.getMinValue();
                        maxY=(float) axis.getMaxValue();
                        break;
                    }
                }
            }
        }
        //cell size
        if(nCols==nRows){
            cellSize=(maxX-llx)/nCols;
        }else if((maxX-llx)/nCols==(maxY-lly)/nRows){
            cellSize=(maxX-llx)/nCols;
        }
        //data rebuild
        curData=new Byte[nRows][nCols];
        for(int i=0;i<nCols;i++){
            for(int j=0;j<nRows;j++){
                curData[i][j]=array.getByte(j*nRows+i);
            }
        }


        return new RasterGrid2_Byte(llx,lly,cellSize,nRows,nCols,curData);
    }

//    private Variable reaFeaturedData(FeatureDataset fds){
//        List<VariableSimpleIF> vList=fds.getDataVariables();
//        Variable var=null;
//        Iterator it=vList.iterator();
//        while(it.hasNext()){
//            var=(Variable)it.next();
//            System.out.println(var.getDataType());
//            if(var.getDataType()== DataType.BYTE||var.getDataType()==DataType.SHORT){
//                return var;
//            }
//        }
//        return null;
//    }

}

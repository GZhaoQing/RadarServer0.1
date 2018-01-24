import com.fasterxml.jackson.databind.ObjectMapper;
import com.radar.Data4Json;
import com.radar.FileParser;
import com.radar.RadarFile;
import org.junit.Test;

import ucar.nc2.NCdumpW;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class TestPaser {
    @Test
    public void TestDimension() throws IOException {
//        FileParser p=new FileParser(System.getProperty("user.dir")+"\\src\\main\\resources\\KFWD_SDUS64_NCZGRK_201208150217");
//        RadarFile rf=p.parseWithImg("defualtpath");
//        LinkedHashMap lm=(LinkedHashMap)rf.getHeadfile().getDimention();
//        Set ks=lm.keySet();
//        Iterator kit=ks.iterator();
//        while(kit.hasNext()){
//            System.out.println(kit.next());
//        }
    }
    @Test
    public void  TestAttribute() throws IOException {
//        FileParser p=new FileParser(System.getProperty("user.dir")+"\\src\\main\\resources\\KFWD_SDUS64_NCZGRK_201208150217");
//        RadarFile rf=p.parseWithImg("defualtpath");
//        Map lm=rf.getHeadfile().getAttribute();
//        Set ks=lm.keySet();
//        Iterator ait=ks.iterator();
//        while(ait.hasNext()){
//            System.out.println(lm.get(ait.next()));
//        }
    }
    @Test
    public void  TestVariable() throws IOException {
//        FileParser p=new FileParser(System.getProperty("user.dir")+"\\src\\main\\resources\\KFWD_SDUS64_NCZGRK_201208150217");
//        RadarFile rf=p.parseWithImg("defualtpath");
//        Map lm=rf.getHeadfile().getVariable();
//        Set ks=lm.keySet();
//        Iterator ait=ks.iterator();
//        while(ait.hasNext()){
//            System.out.println(ait.next());
//        }
    }
    @Test
    public void  Test2Json() throws IOException {
        FileParser p=new FileParser();
        String fileIn=System.getProperty("user.dir")+"\\src\\main\\resources\\KEWX_SDUS54_N0VEWX_201707270600";
        RadarFile rf=p.readWithImg(fileIn,"");
        ObjectMapper mapper=new ObjectMapper();
        String j=mapper.writeValueAsString(rf);
        System.out.println(j);
    }
    @Test
    public void  Test2JsonD() throws IOException {
        FileParser p=new FileParser();
        String fileIn=System.getProperty("user.dir")+"\\src\\main\\resources\\SATE_L2_F2G_VISSR_MWB_LBT_SEC_LCN-IR2-20170527-0100.AWX";
        Data4Json rf=p.readGridData(fileIn);
        ObjectMapper mapper=new ObjectMapper();
        String j=mapper.writeValueAsString(rf);
        System.out.println(j);
    }
}

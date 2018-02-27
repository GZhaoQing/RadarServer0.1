import com.fasterxml.jackson.databind.ObjectMapper;
import com.radar.Data4Json;
import com.radar.FileParser;
import com.radar.RadarFile;
import org.junit.Test;

import ucar.nc2.NCdumpW;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        String fileIn=System.getProperty("user.dir")+"\\src\\main\\resources\\KFWD_SDUS64_NCZGRK_201208150217";
        Data4Json rf=p.readGridData(fileIn);
        ObjectMapper mapper=new ObjectMapper();
        String j=mapper.writeValueAsString(rf);
        System.out.println(j);
    }
    @Test
    public void TestDate() throws ParseException {
        String date="2012-08-15T02:12:24Z";
//        date = date.replace("Z", " UTC");//注意是空格+UTC
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date d = format.parse(date);
        Calendar c= new Calendar.Builder().setInstant(d).build();
        System.out.println(d.getMinutes());
    }
}

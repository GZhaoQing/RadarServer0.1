import com.radar.FileParser;
import com.radar.RadarHeadfile;
import com.radar.util.DfUtil;
import com.radar.util.FileDom;
import com.radar.util.SrchConst;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestSax {
    @Test
    public void testBriefInfo() throws IOException {
        FileParser fp=new FileParser();
        RadarHeadfile hf=fp.readBrief4XML(System.getProperty("user.dir")+"\\src\\main\\resources\\dataFiles\\KEWX\\KEWX_134il_sn.0013");
        System.out.println(hf);
    }
    @Test
    public void testWirteXML() throws SAXException, TransformerConfigurationException, IOException, ParserConfigurationException {
        DfUtil util=new DfUtil();
//        List list= new ArrayList();
        File f=new File(System.getProperty("user.dir")+"\\src\\main\\resources\\dataFiles");
        File file=new File(System.getProperty("user.dir")+"\\src\\main\\resources\\"+"fileList.xml");
        util.writeXML(util.getAllInnerFiles(f),file);
//        Map map = util.getAllInnerFiles(file);
//        Iterator it=map.entrySet().iterator();
//        while(it.hasNext()){
//            Map.Entry entry = (Map.Entry) it.next();
//
//        }
    }
    @Test
    public void testGetAllInnerFiles() throws TransformerConfigurationException, ClassNotFoundException, IOException, ParserConfigurationException, SAXException {
        DfUtil util = new DfUtil();
        File f=new File(System.getProperty("user.dir")+"\\src\\main\\resources\\dataFiles");
        FileDom dom = util.getAllInnerFiles(f);
        System.out.println(dom);

//        Iterator it=dom.getInnerFiles().entrySet().iterator();
//        while(it.hasNext()){
//            Map.Entry entry = (Map.Entry) it.next();
//            Object v = entry.getValue();
//            Class clazz = Class.forName(v.getClass().getName());
//            System.out.println(v);
//            System.out.println(v.getClass());
//
//            for(Object obj : (ArrayList)v){
//                System.out.println(obj.getClass());
//            }
//        }
    }
    @Test
    public void testSearch() throws ParserConfigurationException, SAXException, TransformerConfigurationException, IOException, ParseException {
        DfUtil util=new DfUtil();
        File file=new File(System.getProperty("user.dir")+"\\src\\main\\resources\\"+"fileList.xml");
        String date="2010-02-03T00:00:00.000Z";
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date d1= format1.parse(date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String date2="2012-08-16T02:12:24Z";
        Date d2 = format.parse(date2);
        Date[] dt= { d1 ,d2 };
        SrchConst sc = new SrchConst();
        sc.setPeriod(dt);
        System.out.println(util.search(file,sc));

//        String s= "{\"timeStart\":\"2010-02-03T00:00:00.000Z\",\"timeEnd\":\"2018-03-21T00:00:00.000Z\"}";
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.readValue(s,SearchParam.class);
    }
}

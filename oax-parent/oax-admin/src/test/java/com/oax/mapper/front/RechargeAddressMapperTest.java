package com.oax.mapper.front;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.dom.DOMDocumentType;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.oax.OaxApiApplicationTest;
import com.oax.common.AliOSSUtil;
import com.oax.common.enums.CoinTypeEnum;
import com.oax.entity.front.RechargeAddress;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/11
 * Time: 18:01
 */
@Component
@Slf4j
public class RechargeAddressMapperTest extends OaxApiApplicationTest {

    @Autowired
    private RechargeAddressMapper rechargeAddressMapper;
    @Test
    public void selectByCoinType() {

        List<RechargeAddress> rechargeAddressList = rechargeAddressMapper.selectByCoinType(CoinTypeEnum.ETH.getType());

        List<RechargeAddress> rechargeAddressList1 = rechargeAddressMapper.selectByCoinType(CoinTypeEnum.BTC.getType());

        List<RechargeAddress> rechargeAddressList2 = rechargeAddressMapper.selectByCoinType(CoinTypeEnum.ETH_TOKEN.getType());

        assert rechargeAddressList.size() > 0;
    }


    @Test
    public void testDom4J() {


        String url = "oax.plist";
        String filename = "oax.plist";

        Document document = DocumentHelper.createDocument();

        DocumentType documentType = new DOMDocumentType(
                "plist",
                "-//Apple//DTD PLIST 1.0//EN",
                "http://www.apple.com/DTDs/PropertyList-1.0.dtd");
        document.setDocType(documentType);
        Element plist = document.addElement("plist").addAttribute("version", "1.0");
        Element dict = plist.addElement("dict");
        Element dictkey = dict.addElement("key").addText("items");
        Element array = dict.addElement("array");
        Element arrayDict = array.addElement("dict");


        arrayDict.addElement("key").addText("assets");
        Element arrayArray = arrayDict.addElement("array");
        Element arrayArrayDict = arrayArray.addElement("dict");
        arrayArrayDict.addElement("key").addText("kind");
        arrayArrayDict.addElement("string").addText("software-package");
        arrayArrayDict.addElement("key").addText("url");
        arrayArrayDict.addElement("string").addText(url);


        arrayDict.addElement("key").addText("metadata");
        Element arrayArrayDict1 = arrayDict.addElement("dict");
        arrayArrayDict1.addElement("key").addText("bundle-identifier");
        arrayArrayDict1.addElement("string").addText("com.oax.oax");
        arrayArrayDict1.addElement("key").addText("bundle-version");
        arrayArrayDict1.addElement("string").addText("1.0.0");
        arrayArrayDict1.addElement("key").addText("kind");
        arrayArrayDict1.addElement("string").addText("software");
        arrayArrayDict1.addElement("key").addText("title");
        arrayArrayDict1.addElement("string").addText("OAX");
        OutputFormat format = OutputFormat.createPrettyPrint();
        try {
            File file = new File(filename);
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
            writer.write(document);
            writer.close();
            Date date = new Date();
            String dir = new SimpleDateFormat("yyyy/MM/dd").format(date);
            String s = AliOSSUtil.uploadFile(null,null,file, "test" + "/" + "app" + "/" + dir + "/");
            boolean b = FileUtils.deleteQuietly(file);
            log.info("xml:{}",s);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Autowired
    private TradeMapper tradeMapper;
    @Test
    public void test() {

        ArrayList<Integer> integers = new ArrayList<>();


        IntStream.range(0,100000).parallel()
                .forEach(e->{
                        log.info("add:{}",e);
                        integers.add(e);
                });

        log.info("integerssize:{}",integers.size());

    }

   @Autowired
   private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Test
    public void name() {

        long l = System.currentTimeMillis();
        Runnable runnable = () -> {

            add();
        };


        threadPoolTaskExecutor.execute(runnable);

        log.info("执行时间:"+(System.currentTimeMillis()-l));
    }

    private void add() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
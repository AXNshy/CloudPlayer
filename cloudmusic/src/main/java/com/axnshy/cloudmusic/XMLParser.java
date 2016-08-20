package com.axnshy.cloudmusic;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by axnshy on 2016/3/14.
 */


public class XMLParser {

    public Map<String,String> getMap(InputStream is) {
        //定义一个Map对像
        Map<String,String> map = new HashMap<String,String>();
        //使用pull解析xml文件=
        try {
            //获取Pull解析器工厂
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //获取Pull解析器
            XmlPullParser parser = factory.newPullParser();

            //设置输入流，并且编码
            parser.setInput(is,"UTF_8");

            // 获取事件类型
            int eventType = parser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT) {

                if(eventType == XmlPullParser.START_TAG) {
                    String name = parser.getName();
                    if("key".equals(name)) {
                        String key = parser.nextText();//取出"北京"
                        parser.next();  //指向</key>
                        parser.next();  //指向<string>
                        String value = parser.nextText();  //取出"101010100"
                        //把key-value放进到map对象中
                        map.put(key,value);
                    }
                }

                parser.next(); //指向</string>，第二次执行<key>
                eventType = parser.getEventType(); //获取的是</string>这个类型为结束标签,第二次获取的是<key>
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;

    }
    /*
    * 将map.KeySet获得的set集合变成一个ArrayList,
    *
    * */

    public static ArrayList<String> parseMap(Map< String,String> map) {
        ArrayList<String> list=new ArrayList<String>();

        if(map !=null && !map.isEmpty()) {
            Set<String> set = map.keySet();
            String[] array = new String[set.size()];
            set.toArray(array);
            for(int i=0;i<array.length;i++){
                list.add(array[i]);
            }
        }
        return list;
    }

}

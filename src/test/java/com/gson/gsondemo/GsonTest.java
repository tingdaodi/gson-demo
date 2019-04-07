package com.gson.gsondemo;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Description TODO
 *
 * @author Roye.L
 * @date 2019/4/2 22:02
 * @since 1.0
 */
public class GsonTest {

    @Test
    public void test01() throws IOException {
        Gson gson = new Gson();
        String jsonArray = "[\"Android\",\"Java\",\"PHP\"]";
        String[] strings = gson.fromJson(jsonArray, String[].class);

        List<String> stringList = gson.fromJson(jsonArray, new TypeToken<List<String>>() {}.getType());

        System.out.println(stringList);

        JsonWriter writer = new JsonWriter(new OutputStreamWriter(System.out));
        writer.beginObject() // throws IOException
                .name("name").value("怪盗kidou")
                .name("age").value(24)
                .name("email").nullValue() //演示null
                .endObject(); // throws IOException
        writer.flush(); // throws IOException
    }

    @Test
    public void test() {

        Gson gson = new GsonBuilder()
                .addSerializationExclusionStrategy(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes arg0) {
                        Expose expose = arg0.getAnnotation(Expose.class);
                        return expose != null && !expose.deserialize();
                    }
                    @Override
                    public boolean shouldSkipClass(Class<?> arg0) {
                        return false;
                    }
                }).create();
        System.out.println(gson.toJson(new GsonObject()));
    }

    class GsonObject {

        String field1 = "FIELD_1";
        String field2 = "FIELD_2";
        String field3 = "FIELD_3";
        String field4 = "FIELD_4";
        int field5 = 5;
        double field6 = 6.0;
        boolean field7 = false;

        @Override
        public String toString() {
            return "GsonObject [field1=" + field1 + ", field2=" + field2 + ", field3=" + field3 + ", field4=" + field4
                    + ", field5=" + field5 + ", field6=" + field6 + ", field7=" + field7 + "]";
        }
    }

    static class ModifierSample {
        final String finalField = "final";
        static String staticField = "static";
        public String publicField = "public";
        protected String protectedField = "protected";
        String defaultField = "default";
        private String privateField = "private";
    }

    @Test
    public void test2() {


    }

}

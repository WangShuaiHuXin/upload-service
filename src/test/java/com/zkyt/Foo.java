package com.zkyt;

import cn.hutool.json.JSONUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lombok.Data;

@Data
public class Foo  {
    private String name;
    private Integer id;

    public Foo(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static void main(String[] args) {
        Foo foo = new Foo("Foo", 1);
        Schema<Foo> schema = RuntimeSchema.getSchema(Foo.class);
        LinkedBuffer buffer = LinkedBuffer.allocate();
        final byte[] protostuff;
        try {
            //解析成字节数组
            protostuff = ProtostuffIOUtil.toByteArray(foo, schema, buffer);
        } finally {
            buffer.clear();
        }
        //性能对比字节数
        System.out.println(protostuff.length);
        System.out.println(JSONUtil.toJsonStr(foo).getBytes().length);

        //获取对象
        Foo fooParsed = schema.newMessage();
        //将字节的数据合并到对象里面
        ProtostuffIOUtil.mergeFrom(protostuff, fooParsed, schema);
        System.out.println(fooParsed);
    }
}



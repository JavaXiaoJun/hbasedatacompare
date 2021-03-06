package SerializableTest.Utils;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * protostuff 序列化工具类
 *
 * protostuff只要保证新字段添加在类的最后，而且用的是sun系列的JDK,可以解决增加或删除Bean中的字段的情况；
 */
public class SerializationUtil{
    private static Objenesis objenesis = new ObjenesisStd(true);
    private static <T> Schema<T> getSchema(Class<T> cls)
    {
        Schema<T> schema = RuntimeSchema.createFrom(cls);
        return schema;
    }
    public static <T> byte[] serialize(T obj)
    {
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        Schema<T> schema = getSchema(cls);
        byte[] data = ProtostuffIOUtil.toByteArray(obj,schema,buffer);
        return data;
    }
    public static <T> T deserialize(byte[] data,Class<T> cls)
    {
        T message = objenesis.newInstance(cls);
        Schema<T> schema = getSchema(cls);
        ProtostuffIOUtil.mergeFrom(data,message,schema);
        return message;
    }

    public static <T> void serialize(String path,T obj)
    {
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        Schema<T> schema = getSchema(cls);
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(path);
            ProtostuffIOUtil.writeTo(output,obj,schema,buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}

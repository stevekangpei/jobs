package com.baomidou.jobs.rpc.serialize;

import com.baomidou.jobs.rpc.serialize.impl.HessianSerializer;
import com.baomidou.jobs.rpc.serialize.impl.JacksonSerializer;
import com.baomidou.jobs.rpc.util.XxlRpcException;

/**
 * serializer
 *
 * 		Tips：模板方法模式：定义一个操作中算法的骨架（或称为顶级逻辑），将一些步骤（或称为基本方法）的执行延迟到其子类中；
 * 		Tips：基本方法：抽象方法 + 具体方法final + 钩子方法；
 * 		Tips：Enum 时最好的单例方案；枚举单例会初始化全部实现，此处改为托管Class，避免无效的实例化；
 *
 * @author xuxueli 2015-10-30 21:02:55
 */
public abstract class Serializer {
	
	public abstract <T> byte[] serialize(T obj);
	public abstract <T> Object deserialize(byte[] bytes, Class<T> clazz);
	
	public enum SerializeEnum {
		HESSIAN(HessianSerializer.class),
//		PROTOSTUFF(ProtostuffSerializer.class),
//		KRYO(KryoSerializer.class),
		JACKSON(JacksonSerializer.class);

		private Class<? extends Serializer> serializerClass;
		SerializeEnum(Class<? extends Serializer> serializerClass) {
			this.serializerClass = serializerClass;
		}

		public Serializer getSerializer() {
			try {
				return serializerClass.newInstance();
			} catch (Exception e) {
				throw new XxlRpcException(e);
			}
		}

		public static SerializeEnum match(String name, SerializeEnum defaultSerializer){
			for (SerializeEnum item : SerializeEnum.values()) {
				if (item.name().equals(name)) {
					return item;
				}
			}
			return defaultSerializer;
		}
	}
}

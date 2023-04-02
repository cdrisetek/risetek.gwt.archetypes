package ${package}.bindery.generator;

import java.io.PrintWriter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.impl.Serializer;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.gwt.user.rebind.rpc.SerializableTypeOracleBuilder;
import com.google.gwt.user.rebind.rpc.TypeSerializerCreator;

// TODO: implements IncrementalGenerator
public class WebSocketEventSerializerGenerator extends Generator {
	  private JClassType serializerType = null;
	  private static final String TYPE_SERIALIZER_SUFFIX = "Impl";

	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName)
			throws UnableToCompleteException {
	    TypeOracle typeOracle = context.getTypeOracle();

	    serializerType = typeOracle.findType(typeName);
	    if (serializerType == null || serializerType.isInterface() == null)
	      throw new UnableToCompleteException();

		String packageName = serializerType.getPackage().getName();
	    String typeSerializerClassName = serializerType.getQualifiedSourceName() + TYPE_SERIALIZER_SUFFIX;
	    String typeSerializerSimpleName = serializerType.getSimpleSourceName() + TYPE_SERIALIZER_SUFFIX;

		logger.log(Type.INFO, "packageName: " + packageName);
		logger.log(Type.INFO, "typeSerializerClassName: " + typeSerializerClassName);
		logger.log(Type.INFO, "typeSerializerSimpleName: " + typeSerializerSimpleName);
	    
		TypeOracle oracle = context.getTypeOracle();

		//Find all types that may go over the wire
		// Collect the types the server will send to the client using the Client interface
		SerializableTypeOracleBuilder serializerBuilder = new SerializableTypeOracleBuilder(logger, context);
		// Also add the wrapper object
//		serverSerializerBuilder.addRootType(logger, oracle.findType(WebsocketEvent.class.getName()));
		serializerBuilder.addRootType(logger, oracle.findType(GwtEvent.class.getName()));

		PrintWriter pw = context.tryCreate(logger, packageName, typeSerializerSimpleName);
		if (pw == null) {
			return packageName + "." + typeSerializerSimpleName;
		}

		String tsName = typeSerializerSimpleName + "_TypeSerializer";

		TypeSerializerCreator serializerCreator = new TypeSerializerCreator(logger, serializerBuilder.build(logger) /* client to server we do not care */,
				serializerBuilder.build(logger) /* server to client */, context, packageName + "." + tsName, tsName);
		serializerCreator.realize(logger);

		ClassSourceFileComposerFactory factory = new ClassSourceFileComposerFactory(packageName, typeSerializerSimpleName);
//		factory.setSuperclass(Name.getSourceNameForClass(ServerBuilderImpl.class) + "<" + serverImplType.getQualifiedSourceName() + ">");
		factory.addImplementedInterface(typeName);
		SourceWriter sw = factory.createSourceWriter(context, pw);
		// Make the newly created Serializer available at runtime
		sw.println("public %1$s __getSerializer() {", Serializer.class.getName());
		sw.indentln("return %2$s.<%1$s>create(%1$s.class);", tsName, GWT.class.getName());
		sw.println("}");

		sw.commit(logger);
		return factory.getCreatedClassName();
	}

}

package ${package}.bindery;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class BuilderStampGenerator extends Generator {
	private String implTypeName;
	private String implPackageName;
	private SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm");

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		TypeOracle typeOracle = context.getTypeOracle();
		assert (typeOracle != null);

		JClassType objectType = typeOracle.findType(typeName);
		if (objectType == null) {
			logger.log(TreeLogger.ERROR, "Unable to find metadata for type '"
					+ typeName + "'", null);
			throw new UnableToCompleteException();
		}

		if (objectType.isInterface() == null) {
			logger.log(TreeLogger.ERROR, objectType.getQualifiedSourceName()
					+ " is not an interface", null);
			throw new UnableToCompleteException();
		}

		implTypeName = objectType.getSimpleSourceName() + "Impl";
		implPackageName = objectType.getPackage().getName();

		ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(
				implPackageName, implTypeName);

		composerFactory.addImport(Map.class.getCanonicalName());
		composerFactory.addImport(List.class.getCanonicalName());
		composerFactory.addImplementedInterface(objectType
				.getQualifiedSourceName());

		PrintWriter printWriter = context.tryCreate(logger, implPackageName,
				implTypeName);
		if (printWriter != null) {

			SourceWriter sourceWriter = composerFactory.createSourceWriter(
					context, printWriter);

			sourceWriter.print("public String getBuilderStamp(){");
			sourceWriter.print("  return \"" + fmt.format(new Date()) + "\";");
			sourceWriter.print("}");

			sourceWriter.commit(logger);
		}
		return implPackageName + "." + implTypeName;
	}
}

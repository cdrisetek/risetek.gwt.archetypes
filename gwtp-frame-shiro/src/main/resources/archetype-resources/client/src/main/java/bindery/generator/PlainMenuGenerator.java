package ${package}.bindery.generator;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JPackage;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import ${package}.bindery.PlainMenu;
import ${package}.bindery.PlainMenuLoader.PlainMenuContent;
import ${package}.utils.Icons.Icon;

public class PlainMenuGenerator extends Generator {
	private String implTypeName;
	private String implPackageName;

	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName)
			throws UnableToCompleteException {

		TypeOracle typeOracle = context.getTypeOracle();
		assert (typeOracle != null);

		JClassType objectType = typeOracle.findType(typeName);
		if (objectType == null) {
			logger.log(TreeLogger.ERROR, "Unable to find metadata for type '" + typeName + "'", null);
			throw new UnableToCompleteException();
		}

		if (objectType.isInterface() == null) {
			logger.log(TreeLogger.ERROR, objectType.getQualifiedSourceName() + " is not an interface", null);
			throw new UnableToCompleteException();
		}

		implTypeName = objectType.getSimpleSourceName() + "Impl";
		implPackageName = objectType.getPackage().getName();

		ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(implPackageName,
				implTypeName);

		composerFactory.addImport(GWT.class.getCanonicalName());
		composerFactory.addImport(Collections.class.getCanonicalName());
		composerFactory.addImport(Vector.class.getCanonicalName());
		composerFactory.addImport(List.class.getCanonicalName());
		composerFactory.addImport(Icon.class.getCanonicalName());
		composerFactory.addImport(PlainMenuContent.class.getCanonicalName());
		composerFactory.addImplementedInterface(objectType.getQualifiedSourceName());

		PrintWriter printWriter = context.tryCreate(logger, implPackageName, implTypeName);

		if (printWriter != null) {
			SourceWriter sourceWriter = composerFactory.createSourceWriter(context, printWriter);
			sourceWriter.indentln("private List<PlainMenuContent> menus = null;\n");

			sourceWriter.indentln("public List<PlainMenuContent> getMenus(){");
			sourceWriter.indentln("  if(menus != null)");
			sourceWriter.indentln("    return menus;");
			sourceWriter.indentln("    PlainMenuContent p;");

			sourceWriter.println();
			sourceWriter.indentln("    menus = new Vector<>();");

			JPackage[] packages = typeOracle.getPackages();
			for (JPackage pack : packages) {
				JClassType[] types = pack.getTypes();
				for (JClassType Type : types) {
					PlainMenu menu = Type.getAnnotation(PlainMenu.class);

					if (menu == null || !menu.isEnabled())
						continue;

					System.out.println("generate plain menu for:" + pack.getName() + "." + Type.getName());
					sourceWriter.indentln("    p = new PlainMenuContent();");
					sourceWriter.indentln("    p.order = " + menu.order() + ";");
					if(menu.title() != null)
						sourceWriter.indentln("    p.title = \"" + menu.title() + "\";");
					if(menu.token() != null)
						sourceWriter.indentln("    p.token = \"" + menu.token() + "\";");
					if(menu.iconClass() != null && menu.iconClass() != Icon.class)
						sourceWriter.indentln("    p.icon = (Icon)GWT.create(" + menu.iconClass().getName().replace("$", ".") + ".class);");
					sourceWriter.indentln("    menus.add(p);");
				}
			}

			sourceWriter.indentln("    Collections.sort(menus);");
			sourceWriter.indentln("    return menus;");
			sourceWriter.indentln("}");

			sourceWriter.commit(logger);
		}
		return implPackageName + "." + implTypeName;
	}
}

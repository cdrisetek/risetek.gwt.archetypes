package ${package}.bindery.generator;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

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
	private Calendar rightNow = Calendar.getInstance();
	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {

		String git_commit_id = null;
		String git_commit_date = null;
		
		File file = Paths.get(".").toAbsolutePath().toFile();
		// nested find git repository
		for(; null != file;) {
			try(Git git = Git.open(file)) {
				Repository repository = git.getRepository();
				Ref head = repository.exactRef("refs/heads/" + repository.getBranch());
				if(null != head) {
					try(RevWalk revWalk = new RevWalk(repository)) {
						RevCommit commit = revWalk.parseCommit(head.getObjectId());
						git_commit_id = commit.name();
						git_commit_date = fmt.format(new Date(commit.getCommitTime() * 1000L));
						break;
					}
				} else {
                    logger.log(TreeLogger.WARN, "This project have no commits yet. Please make it correct.", null);
					break;
				}
			} catch (Exception e) {
                try{
                    file = Paths.get(file.getAbsoluteFile().getPath()).getParent().toFile();
                }catch(Exception e1) {
                    logger.log(TreeLogger.WARN, "This project is not a git repository. Please make it correct.", null);
                    break;
                }
			}
		}

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

			sourceWriter.print("public String getYear(){");
			sourceWriter.print("  return \"" + rightNow.get(Calendar.YEAR) + "\";");
			sourceWriter.print("}");

			sourceWriter.print("public String getCommitID(){");
            if(null == git_commit_id)
    			sourceWriter.print("  return null;");
            else
                sourceWriter.print("  return \"" + git_commit_id + "\";");
			sourceWriter.print("}");

			sourceWriter.print("public String getCommitDate(){");
            if(null == git_commit_date)
                sourceWriter.print("  return null;");
            else
                sourceWriter.print("  return \"" + git_commit_date + "\";");
			sourceWriter.print("}");

			sourceWriter.commit(logger);
		}
		return implPackageName + "." + implTypeName;
	}
}

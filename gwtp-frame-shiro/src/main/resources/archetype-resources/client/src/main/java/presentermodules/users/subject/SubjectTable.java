package ${package}.presentermodules.users.subject;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.builder.shared.DivBuilder;
import com.google.gwt.dom.builder.shared.TableCellBuilder;
import com.google.gwt.dom.builder.shared.TableRowBuilder;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTableBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DefaultCellTableBuilder;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import ${package}.share.auth.AccountEntity;
import ${package}.share.auth.accounts.EnumAccount;

public class SubjectTable extends CellTable<AccountEntity> implements HasSubjectSelected {
	private static final int DEFAULT_PAGESIZE = 15;

	private static String getPrincipal(AccountEntity entity) {
		if(null == entity)
			return null;
		if(null == entity.getDescriptions())
			return null;
		return entity.getDescriptions().get(EnumAccount.PRINCIPAL.name());
	}

	private static String getEmail(AccountEntity entity) {
		if(null == entity)
			return null;
		if(null == entity.getDescriptions())
			return null;
		return entity.getDescriptions().get(EnumAccount.EMAIL.name());
	}

	private static String getNote(AccountEntity entity) {
		if(null == entity)
			return null;
		if(null == entity.getDescriptions())
			return null;
		return entity.getDescriptions().get(EnumAccount.NOTES.name());
	}

	private static String getTelphone(AccountEntity entity) {
		if(null == entity)
			return null;
		if(null == entity.getDescriptions())
			return null;
		return entity.getDescriptions().get(EnumAccount.TELPHONE.name());
	}
	
	private static boolean getEnable(AccountEntity entity) {
		if(null == entity)
			return false;
		if(null == entity.getState())
			return false;
		return entity.getState().isEnable();
	}
	public SubjectTable() {
		super(DEFAULT_PAGESIZE, SubjectTableBundle.style, new ProvidesKey<AccountEntity>() {

			@Override
			public Object getKey(AccountEntity item) {
				return getPrincipal(item);
			}
		});
		
		addStyleName(SubjectTableBundle.style.cellTableStyle().subjectTable());

		CellTableBuilder<AccountEntity> tableBuilder = new MyCellTableBuilder(this);
//		CellTableBuilder<AccountEntity> tableBuilder = new DefaultCellTableBuilder<>(this);
		setTableBuilder(tableBuilder);

		setSize("100%", "100%");
		NoSelectionModel<AccountEntity> noSelectionModel = new NoSelectionModel<>();
		noSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				// do nothing.
				subjectSeletedEventHandler.onSelected(noSelectionModel.getLastSelectedObject());
			}
		});

		setSelectionModel(noSelectionModel);
		setTableLayoutFixed(true);

		AbstractSubjectEntityColumn nameColumn = new AbstractSubjectEntityColumn() {

			@Override
			public String getValue(AccountEntity object) {
				return getPrincipal(object);
			}

		};

		AbstractSubjectEntityColumn emailColumn = new AbstractSubjectEntityColumn() {

			@Override
			public String getValue(AccountEntity object) {
				return getEmail(object);
			}

		};

		AbstractSubjectEntityColumn telphoneColumn = new AbstractSubjectEntityColumn() {

			@Override
			public String getValue(AccountEntity object) {
				return getTelphone(object);
			}

		};

		AbstractSubjectEntityColumn notesColumn = new AbstractSubjectEntityColumn() {

			@Override
			public String getValue(AccountEntity object) {
				return getNote(object);
			}

		};

		addColumn(nameColumn);
		setColumnWidth(nameColumn, 400, Unit.PX);
		addColumn(emailColumn);
		setColumnWidth(emailColumn, 400, Unit.PX);
		addColumn(telphoneColumn);
		addColumn(notesColumn);
	}

	// Override getKeyboardSelectedElement,
	// so when we change page, the focus should be lost.
	@Override
	protected Element getKeyboardSelectedElement() {
		return null;
	}

	@Override
	protected void setKeyboardSelected(int index, boolean selected, boolean stealFocus) {
		// 这里的目的是阻止偷取焦点。
	};

	// ------------- Helper for null rows --------------
	private abstract class AbstractSubjectEntityColumn extends Column<AccountEntity, String> {

		public AbstractSubjectEntityColumn() {
			super(new TextCell());
		}

		@Override
		public void render(Context context, AccountEntity object, SafeHtmlBuilder sb) {
			if (object == null)
				sb.appendHtmlConstant("&nbsp;");
			else {
				super.render(context, object, sb);
			}
		}
	}

	class MyCellTableBuilder extends DefaultCellTableBuilder<AccountEntity> {
		SubjectTableBundle.SubjectTableStyleBundle.Style style = SubjectTableBundle.style.cellTableStyle();

		public MyCellTableBuilder(AbstractCellTable<AccountEntity> cellTable) {
			super(cellTable);
		}

		@Override
		public void buildRowImpl(AccountEntity rowValue, int absRowIndex) {

			StringBuilder trClasses = new StringBuilder(style.emptytr());
			// Build the row.
			TableRowBuilder tr = startRow(rowValue);
			if(null == rowValue)
				tr.className(trClasses.toString());

			// Build the columns.
			int columnCount = cellTable.getColumnCount();
			for (int curColumn = 0; curColumn < columnCount; curColumn++) {
				Column<AccountEntity, ?> column = cellTable.getColumn(curColumn);
				// Create the cell styles.
				StringBuilder tdClasses = new StringBuilder();
				// Add class names specific to the cell.
				Context context = new Context(absRowIndex, curColumn, cellTable.getValueKey(rowValue));
				String cellStyles = column.getCellStyleNames(context, rowValue);
				if (cellStyles != null) {
					tdClasses.append(" " + cellStyles);
				}

				// Build the cell.
				HorizontalAlignmentConstant hAlign = column.getHorizontalAlignment();
				VerticalAlignmentConstant vAlign = column.getVerticalAlignment();
				vAlign = HasVerticalAlignment.ALIGN_MIDDLE;

				TableCellBuilder td = tr.startTD();
				td.className(tdClasses.toString());
				if (hAlign != null) {
					td.align(hAlign.getTextAlignString());
				}
				if (vAlign != null) {
					td.vAlign(vAlign.getVerticalAlignString());
				}
				addCellAttributes(td);

				// Add the inner div.
				DivBuilder div = td.startDiv();
				if(getEnable(rowValue))
					div.className(style.subjectcardContainer() + " " + style.accountEnabled());
				else
					div.className(style.subjectcardContainer()+ " " + style.accountDisabled());

				DivBuilder _div = div.startDiv();
				_div.className(style.subjectcard());

				// Render the cell into the div.
				renderCell(_div, context, column, rowValue);
				_div.endDiv();
				// End the cell.
				div.endDiv();
				td.endTD();
			}

			// End the row.
			tr.endTR();
		}
	}

	static class SubjectTableBundle {
		public static SubjectTableStyleBundle style = GWT.create(SubjectTableStyleBundle.class);

		public interface SubjectTableStyleBundle extends CellTable.Resources {
			@Source({ CellTable.Style.DEFAULT_CSS, "SubjectTable.css" })
			Style cellTableStyle();

			interface Style extends CellTable.Style {
				public String subjectTable();
				public String subjectcard();
				public String subjectcardContainer();
				public String accountEnabled();
				public String accountDisabled();
				public String emptytr();
			}
		}
	}

	private SubjectSeletedEventHandler subjectSeletedEventHandler;
	@Override
	public void addSubjectSelectedHandler(SubjectSeletedEventHandler handler) {
		subjectSeletedEventHandler = handler;
	}
}

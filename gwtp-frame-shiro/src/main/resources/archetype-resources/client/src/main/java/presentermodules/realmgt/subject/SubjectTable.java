package ${package}.presentermodules.realmgt.subject;

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
import ${package}.realmgt.SubjectEntity;

public class SubjectTable extends CellTable<SubjectEntity> {
	private static final int DEFAULT_PAGESIZE = 15;

	public SubjectTable() {
		super(DEFAULT_PAGESIZE, SubjectTableBundle.style, new ProvidesKey<SubjectEntity>() {

			@Override
			public Object getKey(SubjectEntity item) {
				return item.getPrincipal().getName();
			}
		});

		CellTableBuilder<SubjectEntity> tableBuilder = new MyCellTableBuilder<>(this);
//		CellTableBuilder<SubjectEntity> tableBuilder = new DefaultCellTableBuilder<>(this);
		setTableBuilder(tableBuilder);

		setSize("100%", "100%");
		NoSelectionModel<SubjectEntity> noSelectionModel = new NoSelectionModel<>();
		noSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				// do nothing.
			}
		});

		setSelectionModel(noSelectionModel);
		setTableLayoutFixed(true);

		AbstractSubjectEntityColumn nameColumn = new AbstractSubjectEntityColumn() {

			@Override
			public String getValue(SubjectEntity object) {
				return null == object ? null : object.getPrincipal().getName();
			}

		};

		AbstractSubjectEntityColumn emailColumn = new AbstractSubjectEntityColumn() {

			@Override
			public String getValue(SubjectEntity object) {
				return null == object ? null : object.getPrincipal().getEmail();
			}

		};

		AbstractSubjectEntityColumn telphoneColumn = new AbstractSubjectEntityColumn() {

			@Override
			public String getValue(SubjectEntity object) {
				return null == object ? null : object.getPrincipal().getTelphone();
			}

		};

		addColumn(nameColumn);
		setColumnWidth(nameColumn, 200, Unit.PX);
		addColumn(emailColumn);
		setColumnWidth(emailColumn, 300, Unit.PX);
		addColumn(telphoneColumn);

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
	private abstract class AbstractSubjectEntityColumn extends Column<SubjectEntity, String> {

		public AbstractSubjectEntityColumn() {
			super(new TextCell());
		}

		@Override
		public void render(Context context, SubjectEntity object, SafeHtmlBuilder sb) {
			if (object == null)
				sb.appendHtmlConstant("&nbsp;");
			else {
				super.render(context, object, sb);
			}
		}
	}

	class MyCellTableBuilder<T> extends DefaultCellTableBuilder<T> {
		private final String cellStyle = SubjectTableBundle.style.cellTableStyle().subjectcardtd();

		public MyCellTableBuilder(AbstractCellTable<T> cellTable) {
			super(cellTable);
		}

		@Override
		public void buildRowImpl(T rowValue, int absRowIndex) {

			StringBuilder trClasses = new StringBuilder(SubjectTableBundle.style.cellTableStyle().subjectcardtr());
			// Build the row.
			TableRowBuilder tr = startRow(rowValue);
			tr.className(trClasses.toString());

			// Build the columns.
			int columnCount = cellTable.getColumnCount();
			for (int curColumn = 0; curColumn < columnCount; curColumn++) {
				Column<T, ?> column = cellTable.getColumn(curColumn);
				// Create the cell styles.
				StringBuilder tdClasses = new StringBuilder(cellStyle);
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
				div.className(SubjectTableBundle.style.cellTableStyle().subjectcardContainer());
				DivBuilder _div = div.startDiv();
				_div.className(SubjectTableBundle.style.cellTableStyle().subjectcard());

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
				public String subjectcard();

				public String subjectcardtd();

				public String subjectcardContainer();

				public String subjectcardtr();
			}
		}
	}
}

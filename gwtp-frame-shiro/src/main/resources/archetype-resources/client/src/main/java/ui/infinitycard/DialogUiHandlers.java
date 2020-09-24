package ${package}.ui.infinitycard;

import com.gwtplatform.mvp.client.UiHandlers;

public interface DialogUiHandlers<E> extends UiHandlers {
	public String getDialogTitle(IsCardWidget<E> card);
	public void onEditorCommit(IsCardWidget<E> card);
	public boolean checkCommitValid(IsCardWidget<E> card);
}

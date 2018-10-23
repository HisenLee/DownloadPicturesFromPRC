package prc.image.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class SelectPathListener implements ActionListener {
	private JTextField mPathField;
	private JFileChooser mFileChooser;
	private JFrame mMainFrame;
	
	public SelectPathListener(JTextField mPathField, JFrame mMainFrame) {
		this.mPathField = mPathField;
		this.mMainFrame = mMainFrame;
		
		mFileChooser = new JFileChooser();
		mFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY );
		mFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		int result = mFileChooser.showOpenDialog(mMainFrame);
		switch (result) {
		case JFileChooser.APPROVE_OPTION:
			mPathField.setText(mFileChooser.getSelectedFile().getAbsolutePath());
			break;
		case JFileChooser.CANCEL_OPTION:
			break;
		case JFileChooser.ERROR_OPTION:
			break;
		default:
			break;
		}
	}

}

package prc.image.ui;


import javax.swing.JTextField;

public final class UIUtils {

	/**
	 * limit TextField
	 */
	public static void setLimitTextField(JTextField mField, int maxlength, boolean isLimitNum) {
		LimitedDocument limitDoc = new LimitedDocument(maxlength);
		if(isLimitNum) {
			limitDoc.setAllowChar("0123456789");
		}
		mField.setDocument(limitDoc);
	}
}

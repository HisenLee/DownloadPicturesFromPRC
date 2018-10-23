package prc.image.listener;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import prc.image.utils.Config;

public class SelectChangeListener implements ActionListener {
	
	private JCheckBox mCbSelectAll;
	
	private JCheckBox mCbBd, mCbQh, mCbSg, mCbBy, mCbQj, mCbYt;
	private JTextField mNumBd, mNumQh, mNumSg, mNumBy, mNumQj, mNumYt;
	
	public SelectChangeListener(JCheckBox cbAll, 
			JCheckBox mCbBd, JTextField mNumBd,
			JCheckBox mCbQh, JTextField mNumQh,
			JCheckBox mCbSg, JTextField mNumSg,
			JCheckBox mCbBy, JTextField mNumBy,
			JCheckBox mCbQj, JTextField mNumQj,
			JCheckBox mCbYt, JTextField mNumYt) {
		this.mCbSelectAll = cbAll;
		this.mCbBd = mCbBd;
		this.mNumBd = mNumBd;
		
		this.mCbQh = mCbQh;
		this.mNumQh = mNumQh;
		
		this.mCbSg = mCbSg;
		this.mNumSg = mNumSg;
		
		this.mCbBy = mCbBy;
		this.mNumBy = mNumBy;
		
		this.mCbQj = mCbQj;
		this.mNumQj = mNumQj;
		
		this.mCbYt = mCbYt;
		this.mNumYt = mNumYt;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource().equals(mCbSelectAll)) {
			changeSelectAll();
		} else if(event.getSource().equals(mCbBd)) {
			changeSelectBaidu();
		} else if(event.getSource().equals(mCbQh)) {
			changeSelectQihoo();
		} else if(event.getSource().equals(mCbSg)) {
			changeSelectSogou();
		} else if(event.getSource().equals(mCbBy)) {
			changeSelectBiying();
		} else if(event.getSource().equals(mCbQj)) {
			changeSelectQuanjing();
		} else if(event.getSource().equals(mCbYt)) {
			changeSelectYiTu();
		}
		
	}
	
	private void changeSelectAll() {
		boolean selectAll = this.mCbSelectAll.isSelected();
		if(!selectAll) {
			mCbBd.setSelected(false);
			mCbQh.setSelected(false);
			mCbSg.setSelected(false);
			mCbBy.setSelected(false);
			mCbQj.setSelected(false);
			mCbYt.setSelected(false);
			
			this.mNumBd.setText("");
			this.mNumQh.setText("");
			this.mNumSg.setText("");
			this.mNumBy.setText("");
			this.mNumQj.setText("");
			this.mNumYt.setText("");
		} else {
			mCbBd.setSelected(true);
			mCbQh.setSelected(true);
			mCbSg.setSelected(true);
			mCbBy.setSelected(true);
			mCbQj.setSelected(true);
			mCbYt.setSelected(true);
			
			this.mNumBd.setText(Config.DEF_NUM_BAIDU+"");
			this.mNumQh.setText(Config.DEF_NUM_QIHOO+"");
			this.mNumSg.setText(Config.DEF_NUM_SOGOU+"");
			this.mNumBy.setText(Config.DEF_NUM_BIYING+"");
			this.mNumQj.setText(Config.DEF_NUM_QUANJING+"");
			this.mNumYt.setText(Config.DEF_NUM_YITU+"");
		}
	}

	private void changeSelectBaidu() {
		boolean select = this.mCbBd.isSelected();
		if(!select) {
			mCbBd.setSelected(false);
			this.mNumBd.setText("");
		} else {
			mCbBd.setSelected(true);
			this.mNumBd.setText(Config.DEF_NUM_BAIDU+"");
		}
		doSelectAll();
	}
	
	private void changeSelectQihoo() {
		boolean select = this.mCbQh.isSelected();
		if(!select) {
			mCbQh.setSelected(false);
			this.mNumQh.setText("");
		} else {
			mCbQh.setSelected(true);
			this.mNumQh.setText(Config.DEF_NUM_QIHOO+"");
		}
		doSelectAll();
	}
	
	private void changeSelectSogou() {
		boolean select = this.mCbSg.isSelected();
		if(!select) {
			mCbSg.setSelected(false);
			this.mNumSg.setText("");
		} else {
			mCbSg.setSelected(true);
			this.mNumSg.setText(Config.DEF_NUM_SOGOU+"");
		}
		doSelectAll();
	}

	private void changeSelectBiying() {
		boolean select = this.mCbBy.isSelected();
		if(!select) {
			mCbBy.setSelected(false);
			this.mNumBy.setText("");
		} else {
			mCbBy.setSelected(true);
			this.mNumBy.setText(Config.DEF_NUM_BIYING+"");
		}
		doSelectAll();
	}
	
	private void changeSelectQuanjing() {
		boolean select = this.mCbQj.isSelected();
		if(!select) {
			mCbQj.setSelected(false);
			this.mNumQj.setText("");
		} else {
			mCbQj.setSelected(true);
			this.mNumQj.setText(Config.DEF_NUM_QUANJING+"");
		}
		doSelectAll();
	}

	private void changeSelectYiTu() {
		boolean select = this.mCbYt.isSelected();
		if(!select) {
			mCbYt.setSelected(false);
			this.mNumYt.setText("");
		} else {
			mCbYt.setSelected(true);
			this.mNumYt.setText(Config.DEF_NUM_YITU+"");
		}
		doSelectAll();
	}
	
	private void doSelectAll() {
		boolean bd = this.mCbBd.isSelected();
		boolean qh = this.mCbQh.isSelected();
		boolean sg = this.mCbSg.isSelected();
		boolean by = this.mCbBy.isSelected();
		boolean qj = this.mCbQj.isSelected();
		boolean yt = this.mCbYt.isSelected();
		
		if(bd && qh && sg && by && qj && yt) {
			mCbSelectAll.setSelected(true);
		} else {
			mCbSelectAll.setSelected(false);
		}
	}
	
	
}

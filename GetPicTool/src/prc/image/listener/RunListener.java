package prc.image.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import prc.image.utils.DialogUtils;
import prc.image.utils.RequestUtils;
import prc.image.utils.StringUtils;

public class RunListener implements ActionListener {
	
	private JTextField mPathField = null;
	private JTextField mKeywordField = null;
	
	private JCheckBox mCheckBoxBaidu = null;
	private JTextField mFieldBdNum = null;
	private JCheckBox mCheckBoxQihoo = null;
	private JTextField mFieldQhNum = null;
	private JCheckBox mCheckBoxSogou = null;
	private JTextField mFieldSgNum = null;
	private JCheckBox mCheckBoxBiying = null;
	private JTextField mFieldByNum = null;
	private JCheckBox mCheckBoxQuanjing = null;
	private JTextField mFieldQjNum = null;
	private JCheckBox mCheckBoxYitu = null;
	private JTextField mFieldYtNum = null;
	
	private ICheckCallBack mCheckCallBack;
	private boolean isNeedProxy=false; // 当前网路环境是否需要Intel代理,默认不需要
	
	public interface ICheckCallBack {
		public void isValid(boolean valid, boolean needProxy);
	}
	
	public RunListener(JTextField mPathField, JTextField mKeywordField, 
					JCheckBox mCheckBoxBaidu, JTextField mFieldBdNum,
					JCheckBox mCheckBoxQihoo, JTextField mFieldQhNum,
					JCheckBox mCheckBoxSogou, JTextField mFieldSgNum,
					JCheckBox mCheckBoxBiying, JTextField mFieldByNum,
					JCheckBox mCheckBoxQuanjing, JTextField mFieldQjNum,
					JCheckBox mCheckBoxYitu, JTextField mFieldYtNum,
					ICheckCallBack mCheckCallBack) {
		this.mPathField = mPathField;
		this.mKeywordField = mKeywordField;
		this.mCheckBoxBaidu = mCheckBoxBaidu;
		this.mFieldBdNum = mFieldBdNum;
		this.mCheckBoxQihoo = mCheckBoxQihoo;
		this.mFieldQhNum = mFieldQhNum;
		this.mCheckBoxSogou = mCheckBoxSogou;
		this.mFieldSgNum = mFieldSgNum;
		this.mCheckBoxBiying = mCheckBoxBiying;
		this.mFieldByNum = mFieldByNum;
		this.mCheckBoxQuanjing = mCheckBoxQuanjing;
		this.mFieldQjNum = mFieldQjNum;
		this.mCheckBoxYitu = mCheckBoxYitu;
		this.mFieldYtNum = mFieldYtNum;
		
		this.mCheckCallBack = mCheckCallBack;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		boolean valid = checkValid();
		boolean needProxy = isNeedProxy;
		mCheckCallBack.isValid(valid, needProxy);
	}
	
	/**
	 * checkValid
	 */
	private boolean checkValid() {
		// 1. check basic info
		String path = mPathField.getText().toString().trim();
		String key = mKeywordField.getText().toString().trim();
		if (StringUtils.isEmptyStr(path)) {
			DialogUtils.showErrorMsg("Please select local path to save images");
			return false;
		}
		if (StringUtils.isEmptyStr(key)) {
			DialogUtils.showErrorMsg("Please input keyword");
			return false;
		}
		// 2. check network
		int netType = RequestUtils.checkNetwork();
		if(netType==RequestUtils.NET_UNAVAILABLE) {
			// 网络不通
			DialogUtils.showErrorMsg("Network is unavailable...");
			return false;
		}  
		if(netType==RequestUtils.NET_AVAILABLE_WITHPROXY) {
			// 需要Intel代理
			isNeedProxy = true;
		}
		// 3. check platform
		boolean bdSelect = mCheckBoxBaidu.isSelected();
		boolean qhSelect = mCheckBoxQihoo.isSelected();
		boolean sgSelect = mCheckBoxSogou.isSelected();
		boolean bySelect = mCheckBoxBiying.isSelected();
		boolean qjSelect = mCheckBoxQuanjing.isSelected();
		boolean ytSelect = mCheckBoxYitu.isSelected();
		
		if(!bdSelect && !qhSelect && !sgSelect && !bySelect && !qjSelect && !ytSelect) {
			DialogUtils.showErrorMsg("Please select at least one source");
			return false;
		}
		String bd_num = mFieldBdNum.getText().toString().trim();
		if (bdSelect) {
			if (StringUtils.isEmptyStr(bd_num)) {
				DialogUtils.showErrorMsg("Please input number of baidu");
				return false;
			}
			if (Integer.parseInt(bd_num) < 1) {
				DialogUtils.showErrorMsg("the number of baidu is invalid ");
				return false;
			}
		}
		String qh_num = mFieldQhNum.getText().toString().trim();
		if (qhSelect) {
			if (StringUtils.isEmptyStr(qh_num)) {
				DialogUtils.showErrorMsg("Please input number of qihoo");
				return false;
			}
			if (Integer.parseInt(qh_num) < 1) {
				DialogUtils.showErrorMsg("the number of qihoo is invalid ");
				return false;
			}
		}
		String sg_num = mFieldSgNum.getText().toString().trim();
		if (sgSelect) {
			if(isNeedProxy) {
				// Intel内网不支持搜狗
				DialogUtils.showErrorMsg("Sogou is unavailable with local network");
				return false;
			}
			if (StringUtils.isEmptyStr(sg_num)) {
				DialogUtils.showErrorMsg("Please input number of sogou");
				return false;
			}
			if (Integer.parseInt(sg_num) < 1) {
				DialogUtils.showErrorMsg("the number of sogou is invalid ");
				return false;
			}
		}
		String by_num = mFieldByNum.getText().toString().trim();
		if (bySelect) {
			if (StringUtils.isEmptyStr(by_num)) {
				DialogUtils.showErrorMsg("Please input number of biying");
				return false;
			}
			if (Integer.parseInt(by_num) < 1) {
				DialogUtils.showErrorMsg("the number of biying is invalid ");
				return false;
			}
		}
		String qj_num = mFieldQjNum.getText().toString().trim();
		if (qjSelect) {
			if (StringUtils.isEmptyStr(qj_num)) {
				DialogUtils.showErrorMsg("Please input number of quanjing");
				return false;
			}
			if (Integer.parseInt(qj_num) < 1) {
				DialogUtils.showErrorMsg("the number of quanjing is invalid ");
				return false;
			}
		}
		String yt_num = mFieldYtNum.getText().toString().trim();
		if (ytSelect) {
			if (StringUtils.isEmptyStr(yt_num)) {
				DialogUtils.showErrorMsg("Please input number of 1Tu");
				return false;
			}
			if (Integer.parseInt(yt_num) < 1) {
				DialogUtils.showErrorMsg("the number of 1Tu is invalid ");
				return false;
			}
		}
		return true;
	}
	

}

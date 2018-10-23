package prc.image.ui;



import java.awt.Desktop;
import java.awt.Font;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import prc.image.listener.RunListener;
import prc.image.listener.RunListener.ICheckCallBack;
import prc.image.listener.SelectChangeListener;
import prc.image.listener.SelectPathListener;
import prc.image.platform.Baidu;
import prc.image.platform.Biying;
import prc.image.platform.IPlatformCallback;
import prc.image.platform.Platform;
import prc.image.platform.Platform.Source;
import prc.image.platform.Qihoo;
import prc.image.platform.Quanjing;
import prc.image.platform.Sogou;
import prc.image.platform.Yitu;
import prc.image.utils.Config;
import prc.image.utils.DialogUtils;
import prc.image.utils.FileUtils;
import prc.image.utils.Log;

public class MainUI {
	private JFrame mMainFrame = null;
	private JPanel mMainPanel = null;
	
	// SAVE PATH
	private JLabel mPathLabel = null;
	private JTextField mPathField = null;
	private JButton mPathBtn = null;
	// KEY WORD
	private JLabel mKeywordLabel = null;
	private JTextField mKeywordField = null;
	// Platform
	private JLabel mSourceLabel = null;
	private JCheckBox mCheckBoxSelectAll = null;
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
	private JCheckBox mCheckBoxYiTu = null;
	private JTextField mFieldYtNum = null;
	// Bottom
	private JButton mStartBtn = null;
	private JScrollPane mScrollPane = null;
	private JTextArea mTextArea = null;
	private StringBuilder textAreaBuilder;
	
	private int compHeight = 20, divHeight = 10;
	private boolean isNeedProxy = false;
	
	public MainUI() {
		initView();
	}

	public void launchUI() {
		this.mMainFrame.setVisible(true);
	}
	
	/**
	 * initialize MainPane
	 */
	private void initMainPane() {
		mMainPanel = new JPanel();
		mMainPanel.setLayout(null);
		mMainPanel.add(mKeywordLabel);
		mMainPanel.add(mKeywordField);
		mMainPanel.add(mPathLabel);
		mMainPanel.add(mPathField);
		mMainPanel.add(mPathBtn);
		
		mMainPanel.add(mSourceLabel);
		mMainPanel.add(mCheckBoxSelectAll);
		mMainPanel.add(mCheckBoxBaidu);
		mMainPanel.add(mFieldBdNum);
		mMainPanel.add(mCheckBoxQihoo);
		mMainPanel.add(mFieldQhNum);
		mMainPanel.add(mCheckBoxSogou);
		mMainPanel.add(mFieldSgNum);
		mMainPanel.add(mCheckBoxBiying);
		mMainPanel.add(mFieldByNum);
		mMainPanel.add(mCheckBoxQuanjing);
		mMainPanel.add(mFieldQjNum);
		mMainPanel.add(mCheckBoxYiTu);
		mMainPanel.add(mFieldYtNum);
		
		mMainPanel.add(mStartBtn);
		mMainPanel.add(mScrollPane);
	}
	
	/**
	 * initialize MainFrame
	 */
	private void initMainFrame() {
		mMainFrame = new JFrame();
		mMainFrame.setTitle("GetPicTool");
		mMainFrame.setLocation(600,120);
        mMainFrame.setSize(670, 780);
		mMainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mMainFrame.setResizable(false);
		mMainFrame.add(mMainPanel);
		
		mPathBtn.addActionListener(new SelectPathListener(mPathField, mMainFrame));
	}

	private void initView() {
		initLittleComps();
		// init MainPane
		initMainPane();
		// init MainFrame
		initMainFrame();
		textAreaBuilder = new StringBuilder();
	}
	
	/**
	 * initialize LittleComp
	 */
	private void initLittleComps() {
		mPathLabel = new JLabel("SAVE PATH: ");
		mPathLabel.setBounds(10,20,80,compHeight);
		
		mPathField = new JTextField(20);
		mPathField.setBounds(90,20,360,compHeight);
		
		mPathBtn = new JButton("SELECT");
		mPathBtn.setBounds(560,20, 90, compHeight);
		
		mKeywordLabel = new JLabel("  KEYWORD:");
		mKeywordLabel.setBounds(10, 20+compHeight+divHeight, 80, compHeight);
		
		mKeywordField = new JTextField();
		mKeywordField.setBounds(90, 20+compHeight+divHeight, 280, compHeight);
		UIUtils.setLimitTextField(mKeywordField, 16, false);
		
		mSourceLabel = new JLabel("      SOURCE:");
		mSourceLabel.setBounds(10,20+compHeight*2+divHeight*2,80,compHeight);
		mCheckBoxSelectAll = new JCheckBox("SelectAll", true);
		mCheckBoxSelectAll.setBounds(100,20+compHeight*2+divHeight*2,80,compHeight);
		
		// baidu
		mCheckBoxBaidu = new JCheckBox("Baidu", true);
		mCheckBoxBaidu.setBounds(100, 20+compHeight*3+divHeight*3, 75, compHeight);
		mFieldBdNum = new JTextField();
		mFieldBdNum.setBounds(190, 20+compHeight*3+divHeight*3, 60, compHeight);
		UIUtils.setLimitTextField(mFieldBdNum, 4, true);
		// Qihoo
		mCheckBoxQihoo = new JCheckBox("Qihoo", true);
		mCheckBoxQihoo.setBounds(300, 20+compHeight*3+divHeight*3, 70, compHeight);
		mFieldQhNum = new JTextField();
		mFieldQhNum.setBounds(370, 20+compHeight*3+divHeight*3, 60, compHeight);
		UIUtils.setLimitTextField(mFieldQhNum, 4, true);
		// Sogou
		mCheckBoxSogou = new JCheckBox("Sogou", true);
		mCheckBoxSogou.setBounds(100, 20+compHeight*4+divHeight*3, 75, compHeight);
		mFieldSgNum = new JTextField();
		mFieldSgNum.setBounds(190, 20+compHeight*4+divHeight*3, 60, compHeight);
		UIUtils.setLimitTextField(mFieldSgNum, 4, true);
		// Biying
		mCheckBoxBiying = new JCheckBox("Biying", true);
		mCheckBoxBiying.setBounds(300, 20+compHeight*4+divHeight*3, 70, compHeight);
		mFieldByNum = new JTextField();
		mFieldByNum.setBounds(370, 20+compHeight*4+divHeight*3, 60, compHeight);
		UIUtils.setLimitTextField(mFieldByNum, 4, true);
		// Quanjing
		mCheckBoxQuanjing = new JCheckBox("Quanjing", true);
		mCheckBoxQuanjing.setBounds(100, 20+compHeight*5+divHeight*3, 75, compHeight);
		mFieldQjNum = new JTextField();
		mFieldQjNum.setBounds(190, 20+compHeight*5+divHeight*3, 60, compHeight);
		UIUtils.setLimitTextField(mFieldQjNum, 6, true);
		// Yitu
		mCheckBoxYiTu = new JCheckBox("1Tu", true);
		mCheckBoxYiTu.setBounds(300, 20+compHeight*5+divHeight*3, 70, compHeight);
		mFieldYtNum = new JTextField();
		mFieldYtNum.setBounds(370, 20+compHeight*5+divHeight*3, 60, compHeight);
		UIUtils.setLimitTextField(mFieldYtNum, 6, true);
		
		// setDefault
		mFieldBdNum.setText(Config.DEF_NUM_BAIDU+"");
		mFieldQhNum.setText(Config.DEF_NUM_QIHOO+"");
		mFieldSgNum.setText(Config.DEF_NUM_SOGOU+"");
		mFieldByNum.setText(Config.DEF_NUM_BIYING+"");
		mFieldQjNum.setText(Config.DEF_NUM_QUANJING+"");
		mFieldYtNum.setText(Config.DEF_NUM_YITU+"");
		
		mCheckBoxBaidu.addActionListener(new SelectChangeListener(mCheckBoxSelectAll, 
				mCheckBoxBaidu, mFieldBdNum, 
				mCheckBoxQihoo, mFieldQhNum, 
				mCheckBoxSogou, mFieldSgNum, 
				mCheckBoxBiying, mFieldByNum, 
				mCheckBoxQuanjing, mFieldQjNum, 
				mCheckBoxYiTu, mFieldYtNum));
		
		mCheckBoxQihoo.addActionListener(new SelectChangeListener(mCheckBoxSelectAll, 
				mCheckBoxBaidu, mFieldBdNum, 
				mCheckBoxQihoo, mFieldQhNum, 
				mCheckBoxSogou, mFieldSgNum, 
				mCheckBoxBiying, mFieldByNum, 
				mCheckBoxQuanjing, mFieldQjNum, 
				mCheckBoxYiTu, mFieldYtNum));
		
		mCheckBoxSogou.addActionListener(new SelectChangeListener(mCheckBoxSelectAll, 
				mCheckBoxBaidu, mFieldBdNum, 
				mCheckBoxQihoo, mFieldQhNum, 
				mCheckBoxSogou, mFieldSgNum, 
				mCheckBoxBiying, mFieldByNum, 
				mCheckBoxQuanjing, mFieldQjNum, 
				mCheckBoxYiTu, mFieldYtNum));
		
		mCheckBoxBiying.addActionListener(new SelectChangeListener(mCheckBoxSelectAll, 
				mCheckBoxBaidu, mFieldBdNum, 
				mCheckBoxQihoo, mFieldQhNum, 
				mCheckBoxSogou, mFieldSgNum, 
				mCheckBoxBiying, mFieldByNum, 
				mCheckBoxQuanjing, mFieldQjNum, 
				mCheckBoxYiTu, mFieldYtNum));
		
		mCheckBoxQuanjing.addActionListener(new SelectChangeListener(mCheckBoxSelectAll, 
				mCheckBoxBaidu, mFieldBdNum, 
				mCheckBoxQihoo, mFieldQhNum, 
				mCheckBoxSogou, mFieldSgNum, 
				mCheckBoxBiying, mFieldByNum, 
				mCheckBoxQuanjing, mFieldQjNum, 
				mCheckBoxYiTu, mFieldYtNum));
		
		mCheckBoxYiTu.addActionListener(new SelectChangeListener(mCheckBoxSelectAll, 
				mCheckBoxBaidu, mFieldBdNum, 
				mCheckBoxQihoo, mFieldQhNum, 
				mCheckBoxSogou, mFieldSgNum, 
				mCheckBoxBiying, mFieldByNum, 
				mCheckBoxQuanjing, mFieldQjNum, 
				mCheckBoxYiTu, mFieldYtNum));
		
		mCheckBoxSelectAll.addActionListener(new SelectChangeListener(mCheckBoxSelectAll, 
				mCheckBoxBaidu, mFieldBdNum, 
				mCheckBoxQihoo, mFieldQhNum, 
				mCheckBoxSogou, mFieldSgNum, 
				mCheckBoxBiying, mFieldByNum, 
				mCheckBoxQuanjing, mFieldQjNum, 
				mCheckBoxYiTu, mFieldYtNum));
		
		mStartBtn = new JButton("RUN");
		mStartBtn.setBounds(560, 200 ,90,compHeight);
		mStartBtn.addActionListener(new RunListener(mPathField, mKeywordField, 
				mCheckBoxBaidu, mFieldBdNum, 
				mCheckBoxQihoo, mFieldQhNum, 
				mCheckBoxSogou, mFieldSgNum, 
				mCheckBoxBiying, mFieldByNum, 
				mCheckBoxQuanjing, mFieldQjNum, 
				mCheckBoxYiTu, mFieldYtNum,
				mCheckCallBack ));
		
		mTextArea = new JTextArea();
		mTextArea.setEditable(false);
		mTextArea.setLineWrap(true);  
		mTextArea.setWrapStyleWord(true); 
		mTextArea.setFont(new Font("标楷体", Font.BOLD, 16));
		mScrollPane = new JScrollPane(mTextArea);
		mScrollPane.setBounds(10,240, 640, 480);
	}
	
	private void refrushTextArea(String text) {
		textAreaBuilder.append(Log.I(text)).append("\n");
		mTextArea.setText(textAreaBuilder.toString());
		
		mScrollPane.getViewport().setViewPosition(new Point(0, mScrollPane.getVerticalScrollBar().getMaximum()));
	}
	
	private ICheckCallBack mCheckCallBack = new ICheckCallBack() {
		
		@Override
		public void isValid(boolean valid, boolean needProxy) {
			isNeedProxy = needProxy;
			if(valid) {
				// create folder to save images
				String basicPath = mPathField.getText().toString()+"\\DownloadImages";
				/// 按关键字分别存储
				FileUtils.createDir(basicPath+"\\"+mKeywordField.getText().toString().trim());
				Config.BASIC_PATH = basicPath;
				mStartBtn.setEnabled(false); 
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						execBaidu();
						mStartBtn.setEnabled(true); 
						execQihoo();
						mStartBtn.setEnabled(true); 
						execSogou();
						mStartBtn.setEnabled(true); 
						execBiying();
						mStartBtn.setEnabled(true); 
						execQuanjing();
						mStartBtn.setEnabled(true); 
						execYitu();
					}// end run
				}).start();
			} // end valid
		}
	};
	
	private void execBaidu() {
		String keyword = mKeywordField.getText().toString().trim();
		boolean baiduSelected = mCheckBoxBaidu.isSelected();
		if(baiduSelected) {
			// 得到max
			int baiduNeed = Integer.parseInt(mFieldBdNum.getText().toString().trim());
			Platform baidu = new Baidu(keyword, isNeedProxy, baiduNeed, platformCallback);
			int baiduMax = baidu.getMaxCount(); 
			if(baiduNeed>=baiduMax) {
				DialogUtils.showErrorMsg("The number of baidu must less than "+baiduMax);
				return;
			}
			if(!new File(Config.BASIC_PATH + "\\" + keyword + "\\Baidu\\Baidu.xlsx").exists()) {
				// 生成全部数据的表格
				baidu.generateExcel();
			} 
			// 下载
			baidu.download();
		}// end baiduSelected
	}
	
	private void execQihoo() {
		String keyword = mKeywordField.getText().toString().trim();
		boolean qihooSelected = mCheckBoxQihoo.isSelected();
		if(qihooSelected) {
			// 得到max
			int qihooNeed = Integer.parseInt(mFieldQhNum.getText().toString().trim());
			Platform qihoo = new Qihoo(keyword, isNeedProxy, qihooNeed, platformCallback);
			int qihooMax = qihoo.getMaxCount(); 
			if(qihooNeed>=qihooMax) {
				DialogUtils.showErrorMsg("The number of qihoo must less than "+qihooMax);
				return;
			}
			if(!new File(Config.BASIC_PATH + "\\" + keyword + "\\Qihoo\\Qihoo.xlsx").exists()) {
				// 生成全部数据的表格
				qihoo.generateExcel();
			} 
			// 下载
			qihoo.download();
		}
	}

	private void execSogou() {
		String keyword = mKeywordField.getText().toString().trim();
		boolean sogouSelected = mCheckBoxSogou.isSelected();
		if(sogouSelected) {
			// 得到max
			int sogouNeed = Integer.parseInt(mFieldSgNum.getText().toString().trim());
			Platform sogou = new Sogou(keyword, isNeedProxy, sogouNeed, platformCallback);
			int sogouMax = sogou.getMaxCount(); 
			if(sogouNeed>=sogouMax) {
				DialogUtils.showErrorMsg("The number of sogou must less than "+sogouMax);
				return;
			}
			if(!new File(Config.BASIC_PATH + "\\" + keyword + "\\Sogou\\Sogou.xlsx").exists()) {
				// 生成全部数据的表格
				sogou.generateExcel();
			} 
			// 下载
			sogou.download();
		}
	}
	
	private void execBiying() {
		String keyword = mKeywordField.getText().toString().trim();
		boolean biyingSelected = mCheckBoxBiying.isSelected();
		if(biyingSelected) {
			// 得到max
			int biyingNeed = Integer.parseInt(mFieldByNum.getText().toString().trim());
			Platform biying = new Biying(keyword, isNeedProxy, biyingNeed, platformCallback);
			int biyingMax = biying.getMaxCount(); 
			if(biyingNeed>=biyingMax) {
				DialogUtils.showErrorMsg("The number of biying must less than "+biyingMax);
				return;
			}
			if(!new File(Config.BASIC_PATH + "\\" + keyword + "\\Biying\\Biying.xlsx").exists()) {
				// 生成全部数据的表格
				biying.generateExcel();
			} 
			// 下载
			biying.download();
		}
	}
	
	private void execQuanjing() {
		String keyword = mKeywordField.getText().toString().trim();
		boolean quanjingSelected = mCheckBoxQuanjing.isSelected();
		if(quanjingSelected) {
			// 得到max
			int quanjingNeed = Integer.parseInt(mFieldQjNum.getText().toString().trim());
			Platform quanjing = new Quanjing(keyword, isNeedProxy, quanjingNeed, platformCallback);
			int quanjingMax = quanjing.getMaxCount(); 
			if(quanjingNeed>=quanjingMax) {
				DialogUtils.showErrorMsg("The number of quanjing must less than "+quanjingMax);
				return;
			}
			if(!new File(Config.BASIC_PATH + "\\" + keyword + "\\Quanjing\\Quanjing.xlsx").exists()) {
				// 生成全部数据的表格
				quanjing.generateExcel();
			} 
			// 下载
			quanjing.download();
		}
	}
	
	private void execYitu() {
		String keyword = mKeywordField.getText().toString().trim();
		boolean yituSelected = mCheckBoxYiTu.isSelected();
		if(yituSelected) {
			// 得到max
			int yituNeed = Integer.parseInt(mFieldYtNum.getText().toString().trim());
			Platform yitu = new Yitu(keyword, isNeedProxy, yituNeed, platformCallback);
			int yituMax = yitu.getMaxCount(); 
			if(yituNeed>=yituMax) {
				DialogUtils.showErrorMsg("The number of 1tu must less than "+yituMax);
				return;
			}
			if(!new File(Config.BASIC_PATH + "\\" + keyword + "\\Yitu\\Yitu.xlsx").exists()) {
				// 生成全部数据的表格
				yitu.generateExcel();
			} 
			// 下载
			yitu.download();
		}
	}
	
	// 存储下载缺失的数据
	private List<List<String>> missDatas = new ArrayList<>();
	
	private IPlatformCallback platformCallback = new IPlatformCallback() {
		
		@Override
		public void actionProcess(Source platform, String process, boolean isFinish, int missNum) {
			refrushTextArea(process);
			
			if(isFinish) {
				// 计算Fail的个数
				if(missNum==0) {
					refrushTextArea(platform.toString()+" download finished");
				} else {
					List<String> missItem = new ArrayList<>();
					missItem.add(platform.toString());
					missItem.add(missNum+"");
					missDatas.add(missItem);
					
					refrushTextArea(platform.toString()+" download finished, "+missNum+" images missed");
				}
				
				Source theLast = getTheLastPlatform();
				if(theLast.toString().equals(platform.toString())) {
					// 开启下载按钮
					mStartBtn.setEnabled(true);
					// 检查上次下载的数据有没有缺失数据
					if(missDatas!=null && missDatas.size()>0) {
						String msg = "";
						for (List<String> itemList : missDatas) {
							msg += itemList.get(0) + " missed "+ itemList.get(1) + " images. \n";
						}
						
						int flag = DialogUtils.showConfirmMsg(msg+" \n Do you wanna continue to download ?");
						if(flag == 0) {
							// 继续下载
							missDatas.clear();
						} else {
							// 不想下载了，打开下载文件夹
							missDatas.clear();
							File file = new File(Config.BASIC_PATH+"\\"+mKeywordField.getText().toString());
							try {
								Desktop.getDesktop().open(file);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} else {
						missDatas.clear();
						// 打开文件夹的提示框
						int flag = DialogUtils.showConfirmMsg(" All images downloaded finished. \n Open folder now......");
						if(flag==0) {
							File file = new File(Config.BASIC_PATH+"\\"+mKeywordField.getText().toString());
							try {
								Desktop.getDesktop().open(file);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} // end missMaps != null
					
				}// end theLast.tostring
				
				
			} // end if(isFinish) 
			
		}// end actionProcess
	};// end platformCallback
	
	
	private Source getTheLastPlatform() {
		Source platform = null;
		boolean bdSelected = mCheckBoxBaidu.isSelected();
		boolean qhSelected = mCheckBoxQihoo.isSelected();
		boolean sgSelected = mCheckBoxSogou.isSelected();
		boolean bySelected = mCheckBoxBiying.isSelected();
		boolean qjSelected = mCheckBoxQuanjing.isSelected();
		boolean ytSelected = mCheckBoxYiTu.isSelected();
		if(ytSelected) {
			// last platform is yitu
			platform = Source.Yitu;
		} else if(qjSelected) {
			// last platform is quanjing
			platform = Source.Quanjing;
		} else if(bySelected) {
			// last platform is biying
			platform = Source.Biying;
		} else if(sgSelected) {
			// last platform is sogou
			platform = Source.Sogou;
		} else if(qhSelected) {
			// last platform is qihoo
			platform = Source.Qihoo;
		} else if(bdSelected) {
			// last platform is baidu
			platform = Source.Baidu;
		}
		return platform;
	}
	
}

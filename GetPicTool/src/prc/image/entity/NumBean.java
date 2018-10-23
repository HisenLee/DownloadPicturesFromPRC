package prc.image.entity;

public class NumBean implements Comparable<NumBean> {

	private String platform;
	private int count;
	private int need;

	public NumBean(String platform, int count, int need) {
		super();
		this.platform = platform;
		this.count = count;
		this.need = need;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getNeed() {
		return need;
	}

	public void setNeed(int need) {
		this.need = need;
	}

	@Override
	public String toString() {
		return "NumBean [platform=" + platform + ", count=" + count + ", need=" + need + "]";
	}

	@Override
	public int compareTo(NumBean arg) {
		return (this.getCount() < arg.getCount() ? -1 :
			 (this.getCount() == arg.getCount() ? 0 : 1));
	}

}

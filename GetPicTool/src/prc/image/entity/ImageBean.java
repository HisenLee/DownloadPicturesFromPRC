package prc.image.entity;



public class ImageBean implements Comparable<ImageBean> {
	
	private String name;
	private String url;
	
	public ImageBean() {
	}

	public ImageBean(String name, String url) {
		super();
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "ImageBean [name=" + name + ", url=" + url + "]";
	}

	@Override
	public int compareTo(ImageBean img) {
		String name = img!=null ? img.getName() : "";
		String thisName = this.getName();
		int rank = Integer.parseInt(name.substring(
				name.lastIndexOf("_")+1, name.length()));
		int thisRank = Integer.parseInt(thisName.substring(
				thisName.lastIndexOf("_")+1, thisName.length()));
		return (thisRank < rank ? -1 :
			 (thisRank == rank ? 0 : 1));
	}


}

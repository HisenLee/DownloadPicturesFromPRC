package prc.image.entity;

import prc.image.platform.Platform.Source;




public class LinkEntity {
	private String url;
	private Source platform;
	
	public LinkEntity() {
		// TODO Auto-generated constructor stub
	}

	public LinkEntity(String url, Source platform) {
		super();
		this.url = url;
		this.platform = platform;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Source getPlatform() {
		return platform;
	}

	public void setPlatform(Source platform) {
		this.platform = platform;
	}

	@Override
	public String toString() {
		return "LinkEntity [url=" + url + ", platform=" + platform + "]";
	}
	
	

}

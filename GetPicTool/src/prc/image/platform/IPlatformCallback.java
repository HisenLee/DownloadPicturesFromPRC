package prc.image.platform;

import prc.image.platform.Platform.Source;

public interface IPlatformCallback {
	public void actionProcess(Source platform, String process, boolean isFinish, int missNum);
}

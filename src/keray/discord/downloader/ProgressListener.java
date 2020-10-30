package keray.discord.downloader;

public interface ProgressListener {
	public void setMax(int max);
	public void setCurrent(int current);
}

package particles;

public class ParticleTexture {
	private int textureID;
	private int numberOfRows;
	private boolean useAdditiveBlending=false;
	
	public ParticleTexture(int textureID, int numberOfRows,boolean useAdditiveBlending) {
		this.useAdditiveBlending=useAdditiveBlending;
		this.textureID = textureID;
		this.numberOfRows = numberOfRows;
	}

	public int getTextureID() {
		return textureID;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public boolean isUseAdditiveBlending() {
		return useAdditiveBlending;
	}
	
	
	
}

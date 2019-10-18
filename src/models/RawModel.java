package models;

public class RawModel {
	
	private int vaoID;
	private int vertexCount;
	private int indicesVboID;
	public RawModel(int vaoID , int vertexCount,int indicesVboID){
		this.vaoID=vaoID;
		this.vertexCount=vertexCount;
		this.indicesVboID=indicesVboID;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	public int getindicesVboID() {
		return indicesVboID;
	}
	
	
	
	
}

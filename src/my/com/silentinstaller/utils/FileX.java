package my.com.silentinstaller.utils;

import java.io.File;
import java.io.Serializable;
import java.net.URI;



public class FileX implements Serializable{

	private boolean Hashed = false;
	private String Hash_String=null;
	private File file;
	
	
	public FileX(File file0){
		file = file0;
	}
	public File getFile(){
		return file;
	}
	public boolean isHashed() {
		return Hashed;
	}
	public void setHashed(boolean has_Hash) {
		Hashed = has_Hash;
	}
	public String getHash_String() {
		return Hash_String;
	}
	public void setHash_String(String hash_String) {
		Hash_String = hash_String;
	}
	
	public long getSize(){
		return file.length();
	}

}

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class EloFile {
	private String fileName;
	
	public EloFile(String fileName) {
		this.fileName = fileName;
	}
	
	public League read() {
		League l = null;
		try {
			FileInputStream f = new FileInputStream(new File(fileName));
			ObjectInputStream o = new ObjectInputStream(f);
			
			l = (League) o.readObject();

			o.close();
			f.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("File "+fileName+" not found");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return l;
	}
	
	public void write(League l) {
		try {
			FileOutputStream f = new FileOutputStream(new File(fileName));
			ObjectOutputStream o = new ObjectOutputStream(f);

			o.writeObject(l);

			o.close();
			f.close();
		} catch (FileNotFoundException e) {
			System.out.println("File "+fileName+" not found");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

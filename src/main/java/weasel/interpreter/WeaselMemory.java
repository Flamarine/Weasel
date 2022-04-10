package weasel.interpreter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class WeaselMemory implements WeaselSaveable {

	private final WeaselInterpreter interpreter;
	private final WeaselObject[] objects;
	
	public WeaselMemory(WeaselInterpreter interpreter, int size){
		this.interpreter = interpreter;
		objects = new WeaselObject[size];
	}
	
	public WeaselMemory(WeaselInterpreter interpreter, DataInputStream dataInputStream) throws IOException{
		this.interpreter = interpreter;
		objects = new WeaselObject[dataInputStream.readInt()];
		for(int i=0; i<objects.length; i++){
			if(dataInputStream.readBoolean()){
				objects[i] = new WeaselObject(interpreter, dataInputStream);
			}
		}
	}
	
	public WeaselObject newObject(int parent, WeaselClass weaselClass){
		for(int i=0; i<objects.length; i++){
			if(objects[i]==null){
				return objects[i] = new WeaselObject(interpreter, parent, weaselClass);
			}
		}
		interpreter.gc();
		for(int i=0; i<objects.length; i++){
			if(objects[i]==null){
				return objects[i] = new WeaselObject(interpreter, parent, weaselClass);
			}
		}
		return null;
	}
	
	public void setAllObjectsInvisible(){
        for (WeaselObject object : objects) {
            object.setVisible(false);
        }
	}
	
	public void deleteInvisibleObjects(){
		for(int i=0; i<objects.length; i++){
			if(!objects[i].isVisible()){
				objects[i] = null;
			}
		}
	}

	@Override
	public void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(objects.length);
        for (WeaselObject object : objects) {
            if (object == null) {
                dataOutputStream.writeBoolean(false);
            } else {
                dataOutputStream.writeBoolean(true);
                object.saveToDataStream(dataOutputStream);
            }
        }
	}

	public WeaselObject getObject(int pointer) {
		return objects[pointer];
	}
	
}

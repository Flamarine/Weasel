package weasel.interpreter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class WeaselClass implements WeaselModifier, WeaselNameable, WeaselSaveable {

	protected WeaselClassBuffer interpreter;
	protected WeaselClass parentClass;
	protected int modifier;
	protected String className;
	protected WeaselClass superClass;
	protected WeaselClass[] interfaces;
	protected WeaselMethod[] methods;
	protected WeaselClass[] childClasses;
	protected int[] staticDataBuffer;
	protected WeaselMethod[] staticMethods;
	protected WeaselClass[] staticChildClasses;
	protected int dataBufferSize;
	protected int initialized;
	
	public WeaselClass(WeaselClassBuffer interpreter, WeaselClass parentClass){
		this.interpreter = interpreter;
		this.parentClass = parentClass;
	}
	
	public WeaselClass(WeaselClassBuffer interpreter, WeaselClass parentClass, String className)  {
		this.interpreter = interpreter;
		this.parentClass = parentClass;
		this.className = className;
	}

	public int getDataBufferSize() {
		return dataBufferSize;
	}
	
	public int[] getStaticDataBuffer(){
		return staticDataBuffer;
	}
	
	public boolean canBeCastTo(WeaselClass weaselClass){
		if(weaselClass==this)
			return true;
		if(superClass!=null){
			if(superClass.canBeCastTo(weaselClass)){
				return true;
			}
		}
		if(interfaces!=null){
			for (WeaselClass anInterface : interfaces) {
				if (anInterface.canBeCastTo(weaselClass)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int getModifier() {
		return modifier;
	}

	@Override
	public String getName() {
		if(parentClass==null)
			return className;
		return parentClass.getName()+"."+className;
	}

	@Override
	public void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		for (int j : staticDataBuffer) {
			dataOutputStream.writeInt(j);
		}
	}

	public void loadFromDataStream(DataInputStream dataInputStream) throws IOException {
		for(int i=0; i<staticDataBuffer.length; i++){
			staticDataBuffer[i] = dataInputStream.readInt();
		}
	}

	public void loadClassFormInputStream(DataInputStream dataInputStream) throws IOException {
		if(initialized == 0){
			initialized = 1;
			modifier = dataInputStream.readInt();
			dataBufferSize = dataInputStream.readInt();
			int staticDataBufferSize = dataInputStream.readInt();
			staticDataBuffer = new int[staticDataBufferSize];
			superClass = interpreter.getClassByName(dataInputStream.readUTF());
			int interfaceCount = dataInputStream.readInt();
			interfaces = new WeaselClass[interfaceCount];
			for(int i=0; i<interfaceCount; i++){
				interfaces[i] = interpreter.getClassByName(dataInputStream.readUTF());
			}
			int methodCount = dataInputStream.readInt();
			methods = new WeaselMethod[methodCount];
			for(int i=0; i<methodCount; i++){
				methods[i] = new WeaselMethod(interpreter, this, dataInputStream);
			}
			int childClassCount = dataInputStream.readInt();
			childClasses = new WeaselClass[methodCount];
			for(int i=0; i<childClassCount; i++){
				childClasses[i] = new WeaselClass(interpreter, this);
				childClasses[i].loadClassFormInputStream(dataInputStream);
			}
			int staticMethodCount = dataInputStream.readInt();
			staticMethods = new WeaselMethod[methodCount];
			for(int i=0; i<staticMethodCount; i++){
				staticMethods[i] = new WeaselMethod(interpreter, this, dataInputStream);
			}
			int staticChildClassCount = dataInputStream.readInt();
			staticChildClasses = new WeaselClass[methodCount];
			for(int i=0; i<staticChildClassCount; i++){
				staticChildClasses[i] = new WeaselClass(interpreter, this);
				staticChildClasses[i].loadClassFormInputStream(dataInputStream);
			}
			initialized = 2;
		}
	}

	public WeaselClass getChildClass(String className) {
		String[] classes = className.split("\\.", 2);
		WeaselClass wClass = null;
		for (WeaselClass childClass : childClasses) {
			if (childClass.getName().equals(classes[0])) {
				wClass = childClass;
				break;
			}
		}
		if(wClass==null){
			for (WeaselClass staticChildClass : staticChildClasses) {
				if (staticChildClass.getName().equals(classes[0])) {
					wClass = staticChildClass;
					break;
				}
			}
		}
		if(classes.length==1)
			return wClass;
		return Objects.requireNonNull(wClass).getChildClass(classes[1]);
	}

	public WeaselMethod getMethod(String name, String desk) {
		int openBraket = desk.indexOf('(');
		int closeBraket = desk.indexOf(')');
		String paramDesk = desk.substring(openBraket+1, closeBraket);
		String[] paramDesks = paramDesk.split(",");
		WeaselClass[] paramClasses = new WeaselClass[paramDesks.length];
		for(int i=0; i<paramDesks.length; i++){
			paramClasses[i] = interpreter.getClassByName(paramDesks[i].trim());
		}

		return getMethod(name, paramClasses);
	}
	
	private static boolean isMethodGood(WeaselMethod method, String name, WeaselClass[] paramClasses){
		if(!method.getName().equals(name))
			return false;
		WeaselClass[] weaselClasses = method.getParamWeaselClasses();
		if(paramClasses.length!=weaselClasses.length)
			return false;
		for(int j=0; j<paramClasses.length; j++){
			if(paramClasses[j]!=weaselClasses[j]){
				return false;
			}
		}
		return true;
	}
	
	public WeaselMethod getMethod(String name, WeaselClass...paramClasses) {
		for (WeaselMethod method : methods) {
			if (isMethodGood(method, name, paramClasses)) {
				return method;
			}
		}
		for (WeaselMethod staticMethod : staticMethods) {
			if (isMethodGood(staticMethod, name, paramClasses)) {
				return staticMethod;
			}
		}
		return null;
		
	}

	public String getFileName() {
		return null;
	}
	
}

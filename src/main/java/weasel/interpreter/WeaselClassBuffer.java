package weasel.interpreter;

public interface WeaselClassBuffer {

	WeaselClass getClassByName(String className);

	WeaselNativeMethod getNativeMethod(String nameAndDesk);
	
}

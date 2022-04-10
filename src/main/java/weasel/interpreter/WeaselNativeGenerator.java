package weasel.interpreter;

public interface WeaselNativeGenerator {

	WeaselNativeMethod createNativeMethod(WeaselInterpreter interpreter, String methodName);
	
}
